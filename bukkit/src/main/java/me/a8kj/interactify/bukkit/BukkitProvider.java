package me.a8kj.interactify.bukkit;

import me.a8kj.interactify.api.platform.PlatformProvider;
import me.a8kj.interactify.api.task.InputTask;
import me.a8kj.interactify.api.util.GenericRenderable;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

/**
 * Bukkit implementation of {@link PlatformProvider} that bridges Interactify with the Bukkit API.
 * <p>
 * This class provides functionality for sending messages, scheduling tasks, executing synchronous tasks,
 * and retrieving unique player identifiers using the Bukkit platform.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public class BukkitProvider implements PlatformProvider<Player> {

    /**
     * The owning plugin instance used for scheduling tasks.
     */
    private final Plugin plugin;

    /**
     * Creates a new Bukkit platform provider.
     *
     * @param plugin the plugin instance
     */
    public BukkitProvider(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Sends a message to a player.
     * <p>
     * The message is converted to plain text and supports legacy color codes using '&'.
     * </p>
     *
     * @param player  the target player
     * @param message the renderable message
     */
    @Override
    public void sendMessage(Player player, GenericRenderable<String> message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message.asPlain()));
    }

    /**
     * Executes a task on the main server thread.
     *
     * @param runnable the task to execute
     */
    @Override
    public void runTask(Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
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
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delaySeconds * 20L);
        return new InputTask() {

            /**
             * Cancels the scheduled task.
             */
            @Override
            public void cancel() {
                task.cancel();
            }

            /**
             * Checks whether the task is still queued for execution.
             *
             * @return true if the task is queued, false otherwise
             */
            @Override
            public boolean isRunning() {
                return Bukkit.getScheduler().isQueued(task.getTaskId());
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
    public UUID getIdentifier(Player player) {
        return player.getUniqueId();
    }
}