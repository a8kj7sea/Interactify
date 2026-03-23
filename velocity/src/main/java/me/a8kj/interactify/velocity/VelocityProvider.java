package me.a8kj.interactify.velocity;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import lombok.Getter;
import me.a8kj.interactify.api.platform.PlatformProvider;
import me.a8kj.interactify.api.task.InputTask;
import me.a8kj.interactify.api.util.GenericRenderable;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Velocity implementation of {@link PlatformProvider} that integrates Interactify with the Velocity API.
 * <p>
 * This class provides functionality for sending messages, executing tasks, scheduling delayed execution,
 * and retrieving unique player identifiers using the Velocity platform.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
@Getter
public class VelocityProvider implements PlatformProvider<Player> {

    /**
     * The Velocity proxy server instance.
     */
    private final ProxyServer server;

    /**
     * The plugin instance used for task scheduling.
     */
    private final Object plugin;

    /**
     * Creates a new Velocity platform provider.
     *
     * @param server the proxy server instance
     * @param plugin the plugin instance
     */
    public VelocityProvider(ProxyServer server, Object plugin) {
        this.server = server;
        this.plugin = plugin;
    }

    /**
     * Sends a message to a player using Adventure components.
     *
     * @param player  the target player
     * @param message the renderable message
     */
    @Override
    public void sendMessage(Player player, GenericRenderable<String> message) {
        if (message == null) return;
        player.sendMessage(message.asComponent());
    }

    /**
     * Executes a task using the Velocity scheduler.
     *
     * @param runnable the task to execute
     */
    @Override
    public void runTask(Runnable runnable) {
        server.getScheduler().buildTask(plugin, runnable).schedule();
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

        ScheduledTask task = server.getScheduler()
                .buildTask(plugin, () -> {
                    if (running.get()) {
                        runnable.run();
                        running.set(false);
                    }
                })
                .delay(Duration.ofSeconds(delaySeconds))
                .schedule();

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
    public UUID getIdentifier(Player player) {
        return player.getUniqueId();
    }
}