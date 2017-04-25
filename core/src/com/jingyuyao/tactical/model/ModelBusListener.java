package com.jingyuyao.tactical.model;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

/**
 * Annotate a class to be registered with {@link ModelBus}.
 */
@Qualifier
@Target({TYPE})
@Retention(RUNTIME)
public @interface ModelBusListener {

}
