package me.a8kj.interactify.api.context;

import lombok.RequiredArgsConstructor;
import me.a8kj.interactify.api.handlers.InputCanceller;
import me.a8kj.interactify.api.handlers.InputReceiver;
import me.a8kj.interactify.api.handlers.InputValidator;
import me.a8kj.interactify.api.util.GenericRenderable;
import me.a8kj.interactify.api.util.QuickRenderable;
import me.a8kj.interactify.api.util.StringRenderable;

/**
 * Builder class used to construct {@link InputContext} instances in a fluent and flexible way.
 * <p>
 * This builder allows configuration of all context properties such as prompt, validator,
 * canceller, timeout behavior, and cancellation keyword before creating the final immutable context.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
@RequiredArgsConstructor
public class InputContextBuilder<S> {

    /**
     * The source associated with the context being built.
     */
    private final S source;

    /**
     * The receiver that will handle the validated input.
     */
    private final InputReceiver<S> receiver;

    /**
     * Optional name for identifying the context.
     */
    private String name;

    /**
     * Optional validator for input validation.
     */
    private InputValidator<S> validator;

    /**
     * Optional canceller invoked when the context is cancelled.
     */
    private InputCanceller<S> canceller;

    /**
     * Prompt message shown when the context starts.
     */
    private GenericRenderable<String> prompt;

    /**
     * Keyword used to cancel the context.
     */
    private String cancelKeyword = "cancel";

    /**
     * Timeout duration in seconds (-1 means no timeout).
     */
    private long timeoutSeconds = -1;

    /**
     * Message shown when the context times out.
     */
    private GenericRenderable<String> timeoutMessage;

    /**
     * Sets the name of the context.
     *
     * @param name the context name
     * @return the builder instance
     */
    public InputContextBuilder<S> name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the prompt message using a plain string.
     *
     * @param prompt the prompt message
     * @return the builder instance
     */
    public InputContextBuilder<S> prompt(String prompt) {
        this.prompt = new StringRenderable("prompt", prompt);
        return this;
    }

    /**
     * Sets the prompt message using a quick renderable.
     *
     * @param prompt the renderable prompt
     * @return the builder instance
     */
    public InputContextBuilder<S> prompt(QuickRenderable prompt) {
        this.prompt = prompt;
        return this;
    }

    /**
     * Assigns a validator to validate user input.
     *
     * @param v the validator
     * @return the builder instance
     */
    public InputContextBuilder<S> validator(InputValidator<S> v) {
        this.validator = v;
        return this;
    }

    /**
     * Assigns a canceller to handle context cancellation.
     *
     * @param c the canceller
     * @return the builder instance
     */
    public InputContextBuilder<S> canceller(InputCanceller<S> c) {
        this.canceller = c;
        return this;
    }

    /**
     * Sets the keyword used to cancel the context.
     *
     * @param k the cancel keyword
     * @return the builder instance
     */
    public InputContextBuilder<S> cancelKeyword(String k) {
        this.cancelKeyword = k;
        return this;
    }

    /**
     * Configures timeout behavior using a duration and a string message.
     *
     * @param sec the timeout duration in seconds
     * @param msg the timeout message
     * @return the builder instance
     */
    public InputContextBuilder<S> timeout(long sec, String msg) {
        this.timeoutSeconds = sec;
        this.timeoutMessage = new StringRenderable("timeout", msg);
        return this;
    }

    /**
     * Configures timeout behavior using a duration and a renderable message.
     *
     * @param sec the timeout duration in seconds
     * @param msg the timeout message renderable
     * @return the builder instance
     */
    public InputContextBuilder<S> timeout(long sec, QuickRenderable msg) {
        this.timeoutSeconds = sec;
        this.timeoutMessage = msg;
        return this;
    }

    /**
     * Builds the {@link InputContext} instance with the configured properties.
     *
     * @return a new InputContext instance
     */
    public InputContext<S> build() {
        return new InputContext<>(
                name,
                source,
                prompt,
                receiver,
                validator,
                canceller,
                cancelKeyword,
                timeoutSeconds,
                timeoutMessage
        );
    }
}