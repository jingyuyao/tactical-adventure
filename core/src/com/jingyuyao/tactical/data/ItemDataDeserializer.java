package com.jingyuyao.tactical.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.jingyuyao.tactical.model.item.ItemData;
import java.lang.reflect.Type;

public class ItemDataDeserializer implements JsonDeserializer<ItemData> {

  private static final String CLASS_NAME = "className";
  private static final String DATA = "data";

  @Override
  public ItemData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
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
