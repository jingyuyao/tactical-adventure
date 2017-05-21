package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import java.io.Reader;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Wraps {@link Gson} to make mocking easier and provide some convenience methods.
 */
@Singleton
class MyGson {

  private final Gson gson;

  @Inject
  MyGson(Gson gson) {
    this.gson = gson;
  }

  <T> T fromJson(Reader reader, Class<T> clazz) {
    return gson.fromJson(reader, clazz);
  }

  void toJson(Object src, Appendable writer) {
    gson.toJson(src, writer);
  }
}
