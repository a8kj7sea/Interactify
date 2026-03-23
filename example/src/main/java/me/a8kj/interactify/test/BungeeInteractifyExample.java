package me.a8kj.interactify.test;

import me.a8kj.interactify.api.util.StringRenderable;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.context.InputContext;
import me.a8kj.interactify.api.context.InputContextBuilder;
import me.a8kj.interactify.api.result.InputValidationResult;

public class BungeeInteractifyExample extends Plugin {

    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerCommand(this, new GlobalAnnounceCommand());
        getLogger().info("Bungee Interactify Example Enabled!");
    }

    public class GlobalAnnounceCommand extends Command {
        public GlobalAnnounceCommand() {
            super("gannounce");
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            if (!(sender instanceof ProxiedPlayer player)) {
                sender.sendMessage(new TextComponent("§cOnly players can use this!"));
                return;
            }

            startAnnounceFlow(player);
        }
    }

    private void startAnnounceFlow(ProxiedPlayer player) {
        InputContext<ProxiedPlayer> context = new InputContextBuilder<>(player, (p, input) -> {
            String message = "§6§l[Global Announcement] §f" + input;
            ProxyServer.getInstance().broadcast(new TextComponent(message));
        })
                .name("bungee-announce")
                .prompt("§e[Bungee] §7Type your global message (or 'cancel' to stop):")
                .cancelKeyword("cancel")
                .validator((p, input) -> {
                    if (input.length() < 5) {
                        return InputValidationResult.fail(new StringRenderable("reason" , "§cMessage too short! Minimum 5 characters."));
                    }
                    return InputValidationResult.ok();
                })
                .canceller(p -> p.sendMessage(new TextComponent("§cAnnouncement cancelled.")))
                .timeout(30, "§cTime is up!")
                .build();

        InteractifyAPI.<ProxiedPlayer>getInstance().registerContext(context);
    }
}