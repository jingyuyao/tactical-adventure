package com.jingyuyao.tactical.model.object;

import com.google.common.eventbus.EventBus;
import com.google.inject.BindingAnnotation;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Set;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Singleton
public class EnemyContainer extends CharacterContainer<Enemy> {
    @Inject
    EnemyContainer(EventBus eventBus, @InitialEnemySet Set<Enemy> objects) {
        super(eventBus, objects);
    }

    @BindingAnnotation @Target({FIELD, PARAMETER, METHOD}) @Retention(RUNTIME)
    @interface InitialEnemySet {}
}