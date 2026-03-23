package me.a8kj.interactify.api.handlers;

/**
 * Functional interface representing a handler that is invoked when an input context is cancelled.
 * <p>
 * Implementations of this interface define the behavior that should occur when a user
 * cancels an active input session or when cancellation is triggered programmatically.
 * </p>
 *
 * <p>
 * This interface is a functional interface and can be used as a lambda expression.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <Source> the type of the input source (e.g., Player, CommandSender, etc.)
 */
@FunctionalInterface
public interface InputCanceller<Source> {

    /**
     * Invoked when the input context is cancelled.
     *
     * @param source the source associated with the cancelled context
     */
    void onCancel(Source source);
}