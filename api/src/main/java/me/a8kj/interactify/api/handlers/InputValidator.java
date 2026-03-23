package me.a8kj.interactify.api.handlers;

import me.a8kj.interactify.api.result.InputValidationResult;

/**
 * Functional interface representing a validator for user input within an input context.
 * <p>
 * Implementations of this interface define rules to validate incoming input and determine
 * whether it is acceptable or should be rejected with a specific reason.
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
public interface InputValidator<Source> {

    /**
     * Validates the provided input from a source.
     *
     * @param source the source providing the input
     * @param input  the input string to validate
     * @return the validation result indicating whether the input is valid and, if not, the reason
     */
    InputValidationResult validate(Source source, String input);
}