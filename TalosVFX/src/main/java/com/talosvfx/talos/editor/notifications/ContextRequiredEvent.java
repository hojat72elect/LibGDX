package com.talosvfx.talos.editor.notifications;

public interface ContextRequiredEvent<T> extends TalosEvent {

    T getContext();

    void setContext(T context);
}
