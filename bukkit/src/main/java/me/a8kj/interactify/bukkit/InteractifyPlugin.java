package me.a8kj.interactify.bukkit;

import me.a8kj.interactify.api.InteractifyAPI;
import me.a8kj.interactify.api.platform.PlatformProvider;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main Bukkit plugin class responsible for initializing the Interactify API.
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
public class InteractifyPlugin extends JavaPlugin {

    /**
     * Static instance of the plugin.
     */
    private static InteractifyPlugin instance;

    /**
     * Called when the plugin is enabled.
     * <p>
     * Initializes the Interactify API and registers the Bukkit input listener.
     * </p>
     */
    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Starting Interactify (Bukkit Implementation)...");

        PlatformProvider<Player> provider = new BukkitProvider(this);

        InteractifyAPI.initialize(provider);

        BukkitInputListener listener = new BukkitInputListener(InteractifyAPI.getInstance(), provider);
        getServer().getPluginManager().registerEvents(listener, this);

        getLogger().info("Interactify API has been initialized successfully.");
    }

    /**
     * Called when the plugin is disabled.
     * <p>
     * Cleans up the plugin instance reference.
     * </p>
     */
    @Override
    public void onDisable() {
        instance = null;
        getLogger().info("Interactify API has been disabled.");
    }

    /**
     * Retrieves the plugin instance.
     *
     * @return the plugin instance
     */
    public static InteractifyPlugin getInstance() {
        return instance;
    }
}