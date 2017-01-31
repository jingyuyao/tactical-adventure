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
      GuiceProvider<BasePlayer> basePlayerGuiceProvider,
      GuiceProvider<PassiveEnemy> passiveEnemyGuiceProvider,
      GuiceProvider<DirectionalWeapon> directionalWeaponGuiceProvider,
      GuiceProvider<Grenade> grenadeGuiceProvider,
      GuiceProvider<Heal> healGuiceProvider
  ) {
    return new GsonBuilder()
        .registerTypeAdapter(Character.class, new PolymorphicAdapter<Character>())
        .registerTypeAdapter(Item.class, new PolymorphicAdapter<Item>())
        .registerTypeAdapter(BasePlayer.class, basePlayerGuiceProvider)
        .registerTypeAdapter(PassiveEnemy.class, passiveEnemyGuiceProvider)
        .registerTypeAdapter(DirectionalWeapon.class, directionalWeaponGuiceProvider)
        .registerTypeAdapter(Grenade.class, grenadeGuiceProvider)
        .registerTypeAdapter(Heal.class, healGuiceProvider)
        .setPrettyPrinting()
        .create();
  }
}
