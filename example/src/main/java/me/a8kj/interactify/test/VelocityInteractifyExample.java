package me.a8kj.interactify.test;

import com.google.inject.Inject;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.context.InputContext;
import me.a8kj.interactify.api.context.InputContextBuilder;
import org.slf4j.Logger;

@Plugin(id = "velocity-test", name = "VelocityTest", version = "1.0.0", authors = {"a8kj"})
public class VelocityInteractifyExample {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public VelocityInteractifyExample(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        server.getCommandManager().register("vtest", new TestCommand());
        logger.info("Velocity Interactify Example Loaded!");
    }

    private class TestCommand implements SimpleCommand {
        @Override
        public void execute(Invocation invocation) {
            if (!(invocation.source() instanceof Player player)) return;

            InputContext<Player> context = new InputContextBuilder<>(player, (p, input) -> {
                p.sendMessage(Component.text("You entered: " + input, NamedTextColor.GREEN));
            })
                    .name("velocity-input")
                    .prompt("&bPlease type something in chat:")
                    .build();

            InteractifyAPI.<Player>getInstance().registerContext(context);
        }
    }
}