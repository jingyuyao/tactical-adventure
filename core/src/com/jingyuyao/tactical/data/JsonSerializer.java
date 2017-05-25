package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * An object serializer backed by {@link Gson}.
 */
@Singleton
class JsonSerializer {

  private final Gson gson;

  @Inject
  JsonSerializer(Gson gson) {
    this.gson = gson;
  }

  /**
   * Deserialize an object from a {@link Reader} then closes it. See {@link Gson#fromJson(Reader,
   * Type)}.
   */
  <T> T deserialize(Reader reader, Class<T> clazz) {
    T result = gson.fromJson(reader, clazz);
    close(reader);
    return result;
  }

  /**
   * Serialize an object to a {@link Writer} then closes it. See {@link Gson#toJson(Object,
   * Appendable)}.
   */
  void serialize(Object src, Writer writer) {
    gson.toJson(src, writer);
    close(writer);
  }

  private void close(Closeable closeable) {
    try {
      closeable.close();
    } catch (IOException e) {
      throw new RuntimeException("Unable to close: " + closeable);
    }
  }
}
