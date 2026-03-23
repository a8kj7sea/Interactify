package me.a8kj.interactify.api.task;

/**
 * Represents a scheduled or running task associated with an input context.
 * <p>
 * This abstraction allows cancellation and status checking of delayed or asynchronous tasks
 * used for timeout handling within input sessions.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
public interface InputTask {

    /**
     * Cancels the execution of this task if it is still running or scheduled.
     */
    void cancel();

    /**
     * Checks whether the task is still running or pending execution.
     *
     * @return true if the task is running or scheduled, false otherwise
     */
    boolean isRunning();
}