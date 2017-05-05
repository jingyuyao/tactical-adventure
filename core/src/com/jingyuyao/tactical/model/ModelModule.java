package com.jingyuyao.tactical.model;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import com.jingyuyao.tactical.model.character.CharacterModule;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.state.StateModule;
import com.jingyuyao.tactical.model.terrain.TerrainModule;
import com.jingyuyao.tactical.model.world.WorldModule;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;

public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new WorldModule());
    install(new CharacterModule());
    install(new TerrainModule());
    install(new ItemModule());
    install(new StateModule());

    bindListener(Matchers.any(), new ModelBusRegisterer(getProvider(ModelBus.class)));
  }

  @Provides
  @Singleton
  @ModelEventBus
  EventBus providesModelEventBus() {
    return new EventBus("model");
  }

  @Qualifier
  @Target({FIELD, PARAMETER, METHOD})
  @Retention(RUNTIME)
  @interface ModelEventBus {

  }

  /**
   * Registers all classes annotated with {@link ModelBusListener} and {@link Singleton} with
   * {@link ModelBus}.
   */
  private static class ModelBusRegisterer implements TypeListener {

    private final Provider<ModelBus> modelBusProvider;

    private ModelBusRegisterer(Provider<ModelBus> modelBusProvider) {
      this.modelBusProvider = modelBusProvider;
    }

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
      Class<?> clazz = type.getRawType();
      if (clazz.isAnnotationPresent(ModelBusListener.class)) {
        if (clazz.isAnnotationPresent(Singleton.class)) {
          encounter.register(new InjectionListener<I>() {
            @Override
            public void afterInjection(I injectee) {
              modelBusProvider.get().register(injectee);
            }
          });
        } else {
          encounter.addError("@ModelBusListener should be used with @Singleton");
        }
      }
    }
  }
}
