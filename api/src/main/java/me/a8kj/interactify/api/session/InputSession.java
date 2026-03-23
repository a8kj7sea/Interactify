package me.a8kj.interactify.api.session;

import me.a8kj.interactify.api.context.InputContext;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Concrete implementation of {@link Session} managing active and queued input contexts for a source.
 * <p>
 * This class maintains the current active input context and a queue of pending contexts,
 * ensuring sequential handling of input interactions for the same source.
 * </p>
 *
 * <p>
 * Author: a8kj7sea
 * GitHub: https://github.com/a8kj7sea
 * </p>
 *
 * @param <S> the source type (e.g., Player, CommandSender, etc.)
 */
public class InputSession<S> implements Session<S> {

    /**
     * The source associated with this session.
     */
    private final S source;

    /**
     * Queue of pending input contexts waiting to be processed.
     */
    private final Queue<InputContext<S>> queue = new LinkedList<>();

    /**
     * The currently active input context.
     */
    private InputContext<S> activeContext;

    /**
     * Creates a new session for the specified source.
     *
     * @param source the source of this session
     */
    public InputSession(S source) {
        this.source = source;
    }

    @Override
    public S getSource() {
        return source;
    }

    @Override
    public InputContext<S> getActiveContext() {
        return activeContext;
    }

    @Override
    public void setActiveContext(InputContext<S> context) {
        this.activeContext = context;
    }

    @Override
    public void enqueue(InputContext<S> context) {
        queue.offer(context);
    }

    @Override
    public InputContext<S> pollNext() {
        return queue.poll();
    }

    @Override
    public boolean hasActiveContext() {
        return activeContext != null;
    }

    @Override
    public boolean hasQueuedContexts() {
        return !queue.isEmpty();
    }

    @Override
    public void clear() {
        if (activeContext != null) activeContext.cancelTask();
        activeContext = null;
        queue.clear();
    }
}