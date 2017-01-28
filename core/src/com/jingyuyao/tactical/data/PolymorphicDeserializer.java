package com.jingyuyao.tactical.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Type;

/**
 * Adapted from http://stackoverflow.com/a/8683689
 */
public class PolymorphicDeserializer<T> implements JsonDeserializer<T> {

  private static final String CLASS_NAME = "className";
  private static final String DATA = "data";

  @Override
  public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASS_NAME);
    String className = prim.getAsString();

    Class<?> clazz;
    try {
      clazz = Class.forName(className);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      throw new JsonParseException(e.getMessage());
    }
    return context.deserialize(jsonObject.get(DATA), clazz);
  }
}
