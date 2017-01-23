package com.jingyuyao.tactical.model.common;

/**
 * Marks a {@link javax.inject.Singleton} class to be registered with the {@link
 * com.google.common.eventbus.EventBus} once the model initializes. It is used in conjunction with
 * {@link com.google.inject.multibindings.Multibinder} to collect the various classes that needs to
 * be registered with the {@link com.google.common.eventbus.EventBus} upon model initialization.
 */
public interface EventSubscriber {

}
