package me.a8kj.interactify.test;

import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.context.InputContext;
import me.a8kj.interactify.api.context.InputContextBuilder;
import me.a8kj.interactify.api.result.InputValidationResult;
import me.a8kj.interactify.api.util.StringRenderable;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class InteractifyExample extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {

        getCommand("transfer").setExecutor(this);

        getLogger().info("Interactify Example Plugin Enabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1) {
            player.sendMessage("§cUsage: /transfer <player>");
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage("§cPlayer not found.");
            return true;
        }

        startTransferFlow(player, target);
        return true;
    }

    private void startTransferFlow(Player sender, Player receiver) {
        InputContext<Player> context = new InputContextBuilder<>(sender, (p, input) -> {
            double amount = Double.parseDouble(input);
            p.sendMessage("§aSent §f$" + amount + " §ato §f" + receiver.getName());
            receiver.sendMessage("§aReceived §f$" + amount + " §afrom §f" + p.getName());
        })
                .name("transfer-logic")
                .prompt("§b[Transfer] §7Enter the amount to send to §f" + receiver.getName() + " §7(or type 'exit'):")
                .cancelKeyword("exit")
                .validator((p, input) -> {
                    try {
                        double val = Double.parseDouble(input);
                        if (val <= 0)
                            return InputValidationResult.fail(new StringRenderable("reason", "§cAmount must be positive."));
                        return InputValidationResult.ok();
                    } catch (NumberFormatException e) {
                        return InputValidationResult.fail(new StringRenderable("reason", "§cPlease enter a valid number."));
                    }
                })
                .canceller(p -> p.sendMessage("§eTransfer cancelled."))
                .timeout(20, "§cRequest expired.")
                .build();

        InteractifyAPI.<Player>getInstance().registerContext(context);
    }
}