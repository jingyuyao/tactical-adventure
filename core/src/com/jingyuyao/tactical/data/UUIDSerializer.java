package com.jingyuyao.tactical.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.util.UUID;

/**
 * Source: https://github.com/magro/kryo-serializers/blob/master/src/main/java/de/javakaffee/kryoserializers/UUIDSerializer.java
 */
class UUIDSerializer extends Serializer<UUID> {

  UUIDSerializer() {
    setImmutable(true);
  }

  @Override
  public void write(Kryo kryo, Output output, UUID uuid) {
    output.writeLong(uuid.getMostSignificantBits());
    output.writeLong(uuid.getLeastSignificantBits());
  }

  @Override
  public UUID read(Kryo kryo, Input input, Class<UUID> type) {
    return new UUID(input.readLong(), input.readLong());
  }
}
