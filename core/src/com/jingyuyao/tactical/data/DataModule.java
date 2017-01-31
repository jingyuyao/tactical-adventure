package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Item;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MapLoader.class);
  }

  @Provides
  @Singleton
  Gson provideGson(
      DataSerializer<BasePlayer> basePlayerDataSerializer,
      DataSerializer<PassiveEnemy> passiveEnemyDataSerializer,
      DataSerializer<DirectionalWeapon> directionalWeaponDataSerializer,
      DataSerializer<Grenade> grenadeDataSerializer,
      DataSerializer<Heal> healDataSerializer
  ) {
    return new GsonBuilder()
        .registerTypeAdapter(Character.class, new PolymorphicDeserializer<Character>())
        .registerTypeAdapter(Item.class, new PolymorphicDeserializer<Item>())
        .registerTypeAdapter(BasePlayer.class, basePlayerDataSerializer)
        .registerTypeAdapter(PassiveEnemy.class, passiveEnemyDataSerializer)
        .registerTypeAdapter(DirectionalWeapon.class, directionalWeaponDataSerializer)
        .registerTypeAdapter(Grenade.class, grenadeDataSerializer)
        .registerTypeAdapter(Heal.class, healDataSerializer)
        .create();
  }
}
