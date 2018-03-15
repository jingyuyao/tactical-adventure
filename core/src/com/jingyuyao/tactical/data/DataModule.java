package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.serializers.CompatibleFieldSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.item.Armor;
import com.jingyuyao.tactical.model.item.BlastArmor;
import com.jingyuyao.tactical.model.item.Bomb;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.OnAllDeath;
import com.jingyuyao.tactical.model.script.OnAnyDeath;
import com.jingyuyao.tactical.model.script.OnDeath;
import com.jingyuyao.tactical.model.script.OnNoGroup;
import com.jingyuyao.tactical.model.script.OnTurn;
import com.jingyuyao.tactical.model.ship.AggressiveAutoPilot;
import com.jingyuyao.tactical.model.ship.AutoPilot;
import com.jingyuyao.tactical.model.ship.NoAutoPilot;
import com.jingyuyao.tactical.model.ship.PassiveAutoPilot;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.util.UUID;
import javax.inject.Singleton;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    requireBinding(Files.class);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.enableComplexMapKeySerialization();

    builder.registerTypeAdapter(Coordinate.class, new CoordinateAdapter());
    builder.registerTypeAdapter(Turn.class, new TurnAdapter());
    builder.registerTypeAdapter(ShipGroup.class, new ShipGroupAdapter());

    builder.registerTypeAdapterFactory(new NullCheckAdapterFactory());

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(Condition.class)
            .registerSubtype(OnDeath.class)
            .registerSubtype(OnAnyDeath.class)
            .registerSubtype(OnAllDeath.class)
            .registerSubtype(OnTurn.class)
            .registerSubtype(OnNoGroup.class));

    builder.registerTypeAdapterFactory(
        RuntimeTypeAdapterFactory
            .of(AutoPilot.class)
            .registerSubtype(NoAutoPilot.class)
            .registerSubtype(PassiveAutoPilot.class)
            .registerSubtype(AggressiveAutoPilot.class));

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
            .registerSubtype(Armor.class)
            .registerSubtype(BlastArmor.class)
    );

    return builder.create();
  }

  @Provides
  @Singleton
  Kryo provideKryo() {
    Kryo kryo = new Kryo();
    kryo.setDefaultSerializer(CompatibleFieldSerializer.class);
    kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
    return kryo;
  }
}
