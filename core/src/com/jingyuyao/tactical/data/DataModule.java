package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.lang.reflect.Type;
import java.util.List;
import javax.inject.Provider;
import javax.inject.Singleton;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(AssetManager.class);
    requireBinding(Files.class);
  }

  @Provides
  @Singleton
  Gson provideGson(Injector injector) {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.enableComplexMapKeySerialization();
    builder.registerTypeAdapter(Coordinate.class, new CoordinateAdapter());
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Player.class)
            .registerSubtype(BasePlayer.class));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Enemy.class)
            .registerSubtype(PassiveEnemy.class));
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Item.class)
            .registerSubtype(DirectionalWeapon.class)
            .registerSubtype(Grenade.class)
            .registerSubtype(Heal.class)
    );
    // TODO: maybe we should pass in the things they need to use instead of providing at creation
    List<Class<?>> needCreator = ImmutableList.of(
        PassiveEnemy.class,
        DirectionalWeapon.class,
        Grenade.class
    );
    for (Class<?> clazz : needCreator) {
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
