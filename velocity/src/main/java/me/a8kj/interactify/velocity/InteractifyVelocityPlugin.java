package me.a8kj.interactify.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformProvider;
import org.slf4j.Logger;

/**
 * Main Velocity plugin class responsible for initializing the Interactify API.
 * <p>
 * This class sets up the platform provider, initializes the API singleton,
 * and registers the required event listeners for handling player input.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
@Plugin(id = "interactify", name = "Interactify", version = "1.0.1", authors = {"a8kj7sea"})
public class InteractifyVelocityPlugin {

    /**
     * The Velocity proxy server instance.
     */
    private final ProxyServer server;

    /**
     * Logger instance for plugin logging.
     */
    private final Logger logger;

    /**
     * Constructs the Velocity plugin with injected dependencies.
     *
     * @param server the proxy server instance
     * @param logger the logger instance
     */
    @Inject
    public InteractifyVelocityPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    /**
     * Called when the proxy is initialized.
     * <p>
     * Initializes the Interactify API and registers the Velocity input listener.
     * </p>
     *
     * @param event the proxy initialization event
     */
    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        logger.info("Initializing Interactify (Velocity Implementation)...");

        PlatformProvider<Player> provider = new VelocityProvider(server, this);
        InteractifyAPI.initialize(provider);

        VelocityInputListener listener = new VelocityInputListener(InteractifyAPI.getInstance(), provider);
        server.getEventManager().register(this, listener);

        logger.info("Interactify API for Velocity is ready!");
    }
}