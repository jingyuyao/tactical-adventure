package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import javax.annotation.Nullable;

/**
 * Check all deserialized objects does not contain any null fields except for when it is annotated
 * with {@link Nullable}.
 */
class NullCheckAdapterFactory implements TypeAdapterFactory {

  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
    final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
    return new TypeAdapter<T>() {
      @Override
      public void write(JsonWriter out, T value) throws IOException {
        delegate.write(out, value);
      }

      @Override
      public T read(JsonReader in) throws IOException {
        T obj = delegate.read(in);
        // obj can be null when reading a `null` json value
        if (obj != null) {
          Field[] fields = obj.getClass().getDeclaredFields();
          for (Field field : fields) {
            if (field.isAnnotationPresent(Nullable.class)) {
              continue;
            }
            try {
              field.setAccessible(true);
              if (field.get(obj) == null) {
                throw new JsonParseException(
                    "Field " + field.getName() + " cannot be null for " + delegate.toJson(obj));
              }
            } catch (IllegalArgumentException | IllegalAccessException e) {
              throw new JsonParseException(
                  "Cannot access " + field.getName() + " to check for null", e);
            }
          }
        }
        return obj;
      }
    };
  }
}
