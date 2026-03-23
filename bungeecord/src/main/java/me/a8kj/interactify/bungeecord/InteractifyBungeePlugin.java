package me.a8kj.interactify.bungeecord;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformProvider;

import java.util.logging.Level;

/**
 * Main BungeeCord plugin class responsible for initializing the Interactify API.
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
public class InteractifyBungeePlugin extends Plugin {

    /**
     * Called when the plugin is enabled.
     * <p>
     * Initializes the Interactify API and registers the Bungee input listener.
     * </p>
     */
    @Override
    public void onEnable() {
        getLogger().info("Starting Interactify (BungeeCord Implementation)...");

        try {
            PlatformProvider<ProxiedPlayer> provider = new BungeeProvider(this);
            InteractifyAPI.initialize(provider);

            BungeeInputListener listener = new BungeeInputListener(InteractifyAPI.getInstance(), provider);
            getProxy().getPluginManager().registerListener(this, listener);

            getLogger().info("Interactify API initialized and listener registered successfully!");
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to initialize Interactify API!", e);
        }
    }

    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        getLogger().info("Interactify API has been disabled.");
    }
}