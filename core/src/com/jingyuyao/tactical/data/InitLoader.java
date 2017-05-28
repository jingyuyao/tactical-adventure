package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Loads initial configuration data as Java objects, backed by {@link Gson} and {@link
 * ConfigFactory}. Only deserialization methods are provided. Serialization should use {@link
 * KryoSerializer} instead.
 */
@Singleton
class InitLoader {

  private final Gson gson;

  @Inject
  InitLoader(Gson gson) {
    this.gson = gson;
  }

  /**
   * Deserialize an object from a {@link Reader} containing JSON then closes it. See {@link
   * Gson#fromJson(Reader, Type)}.
   */
  <T> T fromJson(Reader reader, Class<T> clazz) {
    T result = gson.fromJson(reader, clazz);
    try {
      reader.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close: " + reader);
    }
    return result;
  }

  /**
   * Deserialize an object from a {@link Reader} containing HOCON then closes it. See {@link
   * ConfigFactory#parseReader(Reader)} and {@link Gson#fromJson(Reader, Type)}.
   */
  <T> T fromHocon(Reader reader, Class<T> clazz) {
    Config config = ConfigFactory.parseReader(reader);
    try {
      reader.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close: " + reader);
    }
    JsonElement jsonElement = gson.toJsonTree(config.root().unwrapped());
    return gson.fromJson(jsonElement, clazz);
  }
}
