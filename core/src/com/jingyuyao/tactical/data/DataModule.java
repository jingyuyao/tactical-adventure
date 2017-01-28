package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.jingyuyao.tactical.model.item.ItemData;

public class DataModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MapLoader.class);
  }

  @Provides
  @Singleton
  Gson provideGson() {
    return new GsonBuilder()
        .registerTypeAdapter(ItemData.class, new PolymorphicDeserializer<ItemData>())
        .create();
  }
}
