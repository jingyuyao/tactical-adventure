package com.jingyuyao.tactical.data;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Adapted from http://stackoverflow.com/a/8683689
 */
class DataSerializer<T> implements JsonSerializer<T>, InstanceCreator<T> {

  private static final String CLASS_NAME = "className";
  private static final String DATA = "data";

  private final Provider<T> provider;

  @Inject
  DataSerializer(Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty(CLASS_NAME, src.getClass().getName());
    jsonObject.add(DATA, context.serialize(src));
    return jsonObject;
  }

  @Override
  public T createInstance(Type type) {
    return provider.get();
  }
}
