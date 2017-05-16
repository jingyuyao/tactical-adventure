package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.Bomb;
import com.jingyuyao.tactical.model.item.Bulkheads;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Hull;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.ship.BasePlayer;
import com.jingyuyao.tactical.model.ship.Enemy;
import com.jingyuyao.tactical.model.ship.PassiveEnemy;
import com.jingyuyao.tactical.model.ship.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
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
            .of(Consumable.class)
            .registerSubtype(Heal.class)
    );
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Weapon.class)
            .registerSubtype(DirectionalWeapon.class)
            .registerSubtype(Bomb.class)
    );
    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Armor.class)
            .registerSubtype(Hull.class)
            .registerSubtype(Bulkheads.class)
    );
    return builder.create();
  }
}
