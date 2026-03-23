package me.a8kj.interactify.bungeecord;

import me.a8kj.interactify.api.platform.PlatformProvider;
import me.a8kj.interactify.api.task.InputTask;
import me.a8kj.interactify.api.util.GenericRenderable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.scheduler.ScheduledTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * BungeeCord implementation of {@link PlatformProvider} that integrates Interactify with the Bungee API.
 * <p>
 * This class provides functionality for sending messages, running asynchronous tasks,
 * scheduling delayed execution, and resolving unique player identifiers.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public class BungeeProvider implements PlatformProvider<ProxiedPlayer> {

    /**
     * The plugin instance used for scheduling tasks.
     */
    private final Plugin plugin;

    /**
     * Creates a new Bungee platform provider.
     *
     * @param plugin the plugin instance
     */
    public BungeeProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a formatted message to a player.
     * <p>
     * The message is converted to plain text and supports legacy color codes using '&'.
     * </p>
     *
     * @param player  the target player
     * @param message the renderable message
     */
    @Override
    public void sendMessage(ProxiedPlayer player, GenericRenderable<String> message) {
        player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message.asPlain())));
    }

    /**
     * Executes a task asynchronously using the Bungee scheduler.
     *
     * @param runnable the task to execute
     */
    @Override
    public void runTask(Runnable runnable) {
        ProxyServer.getInstance().getScheduler().runAsync(plugin, runnable);
    }

    /**
     * Schedules a task to run after a delay in seconds.
     *
     * @param runnable     the task to execute
     * @param delaySeconds the delay before execution in seconds
     * @return an {@link InputTask} wrapper for controlling the scheduled task
     */
    @Override
    public InputTask scheduleTask(Runnable runnable, long delaySeconds) {
        AtomicBoolean running = new AtomicBoolean(true);

        Runnable wrapped = () -> {
            if (running.get()) {
                runnable.run();
                running.set(false);
            }
        };

        ScheduledTask task = ProxyServer.getInstance().getScheduler()
                .schedule(plugin, wrapped, delaySeconds, TimeUnit.SECONDS);

        return new InputTask() {

            /**
             * Cancels the scheduled task and marks it as no longer running.
             */
            @Override
            public void cancel() {
                running.set(false);
                task.cancel();
            }

            /**
             * Checks whether the task is still pending or running.
             *
             * @return true if still active, false otherwise
             */
            @Override
            public boolean isRunning() {
                return running.get();
            }
        };
    }

    /**
     * Retrieves the unique identifier of a player.
     *
     * @param player the player
     * @return the player's UUID
     */
    @Override
    public UUID getIdentifier(ProxiedPlayer player) {
        return player.getUniqueId();
    }
}