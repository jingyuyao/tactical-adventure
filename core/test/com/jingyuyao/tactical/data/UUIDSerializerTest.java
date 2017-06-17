package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class UUIDSerializerTest {

  private Kryo kryo;

  @Before
  public void setUp() {
    kryo = new Kryo();
    kryo.addDefaultSerializer(UUID.class, new UUIDSerializer());
  }

  @Test
  public void serialize_deserialize() {
    UUID uuid = UUID.randomUUID();
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Output output = new Output(outputStream);
    kryo.writeObject(output, uuid);
    output.close();

    InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    Input input = new Input(inputStream);
    UUID parsed = kryo.readObject(input, UUID.class);
    input.close();

    assertThat(uuid).isEqualTo(parsed);
  }
}