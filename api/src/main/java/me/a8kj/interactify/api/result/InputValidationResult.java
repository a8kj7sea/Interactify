package me.a8kj.interactify.api.result;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.interactify.api.util.GenericRenderable;

/**
 * Represents the result of validating user input within an input context.
 * <p>
 * This class indicates whether the input is valid and, if not, provides a reason
 * that can be rendered and communicated back to the source.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 */
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class InputValidationResult {

    /**
     * Indicates whether the validation succeeded.
     */
    private final boolean valid;

    /**
     * The reason for validation failure, if any. May be null when valid.
     */
    private final GenericRenderable<String> reason;

    /**
     * Creates a successful validation result.
     *
     * @return a validation result indicating success
     */
    public static InputValidationResult ok() {
        return new InputValidationResult(true, null);
    }

    /**
     * Creates a failed validation result with a provided reason.
     *
     * @param reason the reason explaining why validation failed
     * @return a validation result indicating failure
     */
    public static InputValidationResult fail(GenericRenderable reason) {
        return new InputValidationResult(false, reason);
    }
}