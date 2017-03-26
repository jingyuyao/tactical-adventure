package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Just wraps {@link Gson} so we can mock it.
 */
@Singleton
class MyGson {

  private final Gson gson;

  @Inject
  MyGson(Gson gson) {
    this.gson = gson;
  }

  <T> T fromJson(String json, Class<T> clazz) {
    return gson.fromJson(json, clazz);
  }

  String toJson(Object src) {
    return gson.toJson(src);
  }
}
