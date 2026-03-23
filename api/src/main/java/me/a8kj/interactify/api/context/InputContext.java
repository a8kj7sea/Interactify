package me.a8kj.interactify.api.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.interactify.api.handlers.*;
import me.a8kj.interactify.api.task.InputTask;
import me.a8kj.interactify.api.util.GenericRenderable;

/**
 * Represents a single input interaction context bound to a specific source.
 * <p>
 * This class encapsulates all components required to handle an input flow,
 * including prompt rendering, validation, cancellation, timeout handling,
 * and the final receiver logic.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
@Getter
@RequiredArgsConstructor
public class InputContext<S> {

    /**
     * Unique name of the context used for identification.
     */
    private final String name;

    /**
     * The source associated with this context.
     */
    private final S source;

    /**
     * The prompt message sent to the source when the context starts.
     */
    private final GenericRenderable<String> prompt;

    /**
     * The receiver responsible for handling valid input.
     */
    private final InputReceiver<S> receiver;

    /**
     * Optional validator used to validate incoming input.
     */
    private final InputValidator<S> validator;

    /**
     * Optional canceller invoked when the context is cancelled.
     */
    private final InputCanceller<S> canceller;

    /**
     * Keyword used to cancel the current context.
     */
    private final String cancelKeyword;

    /**
     * Timeout duration in seconds before automatic cancellation.
     */
    private final long timeoutSeconds;

    /**
     * Message sent when the context times out.
     */
    private final GenericRenderable<String> timeoutMessage;

    /**
     * Scheduled task responsible for timeout handling.
     */
    private InputTask scheduledTask;

    /**
     * Assigns the scheduled timeout task.
     *
     * @param task the task to assign
     */
    public void setScheduledTask(InputTask task) {
        this.scheduledTask = task;
    }

    /**
     * Cancels the scheduled timeout task if present.
     */
    public void cancelTask() {
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
    }
}