package com.jingyuyao.tactical.data;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.state.Waiting;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Model.class);
    requireBinding(Characters.class);
    requireBinding(new Key<Provider<Waiting>>() {
    });
    requireBinding(AssetManager.class);
    requireBinding(OrthogonalTiledMapRenderer.class);
  }

  @Provides
  @Singleton
  RuntimeTypeAdapterFactory<Character> provideRuntimeCharacterAdapterFactory() {
    // register concrete character classes here
    return RuntimeTypeAdapterFactory
        .of(Character.class)
        .registerSubtype(BasePlayer.class)
        .registerSubtype(PassiveEnemy.class);
  }

  @Provides
  @Singleton
  RuntimeTypeAdapterFactory<Item> provideRuntimeItemAdapterFactory() {
    // register concrete item classes here
    return RuntimeTypeAdapterFactory
        .of(Item.class)
        .registerSubtype(DirectionalWeapon.class)
        .registerSubtype(Grenade.class)
        .registerSubtype(Heal.class);
  }

  @Provides
  @Singleton
  @Named("needCreatorClasses")
  List<Class<?>> provideNeedCreatorClasses() {
    // Add model classes that requires Guice injection here
    return ImmutableList.of(
        BasePlayer.class,
        PassiveEnemy.class,
        DirectionalWeapon.class,
        Grenade.class
    );
  }

  @Provides
  @Singleton
  Gson provideGson(
      Injector injector,
      RuntimeTypeAdapterFactory<Character> characterRuntimeTypeAdapterFactory,
      RuntimeTypeAdapterFactory<Item> itemRuntimeTypeAdapterFactory,
      @Named("needCreatorClasses") List<Class<?>> classes
  ) {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.registerTypeAdapterFactory(characterRuntimeTypeAdapterFactory);
    builder.registerTypeAdapterFactory(itemRuntimeTypeAdapterFactory);
    for (Class<?> clazz : classes) {
      final Provider<?> provider = injector.getProvider(clazz);
      builder.registerTypeAdapter(clazz, new InstanceCreator<Object>() {
        @Override
        public Object createInstance(Type type) {
          return provider.get();
        }
      });
    }
    return builder.create();
  }
}
