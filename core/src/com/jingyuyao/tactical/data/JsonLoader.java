package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Loads Json files as Java objects, backed by {@link Gson}. This should only be use to initialize
 * game state. Save data serialization should use {@link KryoSerializer}.
 */
@Singleton
class JsonLoader {

  private final Gson gson;

  @Inject
  JsonLoader(Gson gson) {
    this.gson = gson;
  }

  /**
   * Deserialize an object from a {@link Reader} then closes it. See {@link Gson#fromJson(Reader,
   * Type)}.
   */
  <T> T deserialize(Reader reader, Class<T> clazz) {
    T result = gson.fromJson(reader, clazz);
    try {
      reader.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close: " + reader);
    }
    return result;
  }
}
