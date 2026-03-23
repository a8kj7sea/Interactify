package me.a8kj.interactify.api.platform;

import me.a8kj.interactify.api.task.InputTask;
import me.a8kj.interactify.api.util.GenericRenderable;

import java.util.UUID;

/**
 * Interface representing a platform abstraction for messaging, task scheduling, and source identification.
 * <p>
 * Implementations of this interface allow Interactify to interact with different platforms
 * (e.g., Minecraft servers, chat platforms) without coupling to platform-specific APIs.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
public interface PlatformProvider<S> {

    /**
     * Sends a message to the specified source.
     *
     * @param source  the recipient of the message
     * @param message the message to send
     */
    void sendMessage(S source, GenericRenderable<String> message);

    /**
     * Runs a task asynchronously or on the main platform thread, depending on the implementation.
     *
     * @param runnable the task to execute
     */
    void runTask(Runnable runnable);

    /**
     * Schedules a task to run after a delay in seconds.
     *
     * @param runnable     the task to execute
     * @param delaySeconds the delay before execution in seconds
     * @return a handle to the scheduled task
     */
    InputTask scheduleTask(Runnable runnable, long delaySeconds);

    /**
     * Returns a unique identifier for a given source.
     *
     * @param source the source
     * @return the UUID uniquely identifying the source
     */
    UUID getIdentifier(S source);
}