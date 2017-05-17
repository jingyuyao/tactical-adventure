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
import com.jingyuyao.tactical.model.person.Hero;
import com.jingyuyao.tactical.model.person.Person;
import com.jingyuyao.tactical.model.person.Villain;
import com.jingyuyao.tactical.model.ship.BasicShip;
import com.jingyuyao.tactical.model.ship.PassiveShip;
import com.jingyuyao.tactical.model.ship.Ship;
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
            .of(Person.class)
            .registerSubtype(Hero.class)
            .registerSubtype(Villain.class));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Ship.class)
            .registerSubtype(BasicShip.class)
            .registerSubtype(PassiveShip.class));

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
