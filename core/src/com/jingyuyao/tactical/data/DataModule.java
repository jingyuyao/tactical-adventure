package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.BasePlayer;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.item.Heal;
import com.jingyuyao.tactical.model.item.Item;
import javax.inject.Singleton;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MapLoader.class);
  }

  @Provides
  @Singleton
  Gson provideGson(
      RuntimeTypeAdapterFactory<Character> characterRuntimeTypeAdapterFactory,
      RuntimeTypeAdapterFactory<Item> itemRuntimeTypeAdapterFactory,
      GuiceProvider<BasePlayer> basePlayerGuiceProvider,
      GuiceProvider<PassiveEnemy> passiveEnemyGuiceProvider,
      GuiceProvider<DirectionalWeapon> directionalWeaponGuiceProvider,
      GuiceProvider<Grenade> grenadeGuiceProvider,
      GuiceProvider<Heal> healGuiceProvider
  ) {
    return new GsonBuilder()
        .registerTypeAdapterFactory(characterRuntimeTypeAdapterFactory)
        .registerTypeAdapterFactory(itemRuntimeTypeAdapterFactory)
        .registerTypeAdapter(BasePlayer.class, basePlayerGuiceProvider)
        .registerTypeAdapter(PassiveEnemy.class, passiveEnemyGuiceProvider)
        .registerTypeAdapter(DirectionalWeapon.class, directionalWeaponGuiceProvider)
        .registerTypeAdapter(Grenade.class, grenadeGuiceProvider)
        .registerTypeAdapter(Heal.class, healGuiceProvider)
        .setPrettyPrinting()
        .create();
  }

  @Provides
  @Singleton
  RuntimeTypeAdapterFactory<Character> provideRuntimeCharacterAdapterFactory() {
    return RuntimeTypeAdapterFactory
        .of(Character.class)
        .registerSubtype(BasePlayer.class)
        .registerSubtype(PassiveEnemy.class);
  }

  @Provides
  @Singleton
  RuntimeTypeAdapterFactory<Item> provideRuntimeItemAdapterFactory() {
    return RuntimeTypeAdapterFactory
        .of(Item.class)
        .registerSubtype(DirectionalWeapon.class)
        .registerSubtype(Grenade.class)
        .registerSubtype(Heal.class);
  }
}
