package me.a8kj.interactify.api.handlers;

/**
 * Functional interface representing a handler that processes received input from a source.
 * <p>
 * Implementations of this interface define the logic executed when a user successfully
 * provides input for an active input context.
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
public interface InputReceiver<Source> {

    /**
     * Invoked when input is received from the source.
     *
     * @param source the source providing the input
     * @param input  the input string provided by the source
     */
    void onReceive(Source source, String input);
}