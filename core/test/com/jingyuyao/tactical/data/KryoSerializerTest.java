package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.esotericsoftware.kryo.Kryo;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class KryoSerializerTest {

  private KryoSerializer kryoSerializer;

  @Before
  public void setUp() {
    kryoSerializer = new KryoSerializer(new Kryo());
  }

  @Test
  public void serialize_deserialize() throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    kryoSerializer.serialize(new Dummy(), outputStream);

    InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    Dummy dummy = kryoSerializer.deserialize(inputStream, Dummy.class);
    assertThat(dummy.abc).isEqualTo("123");
  }

  private static class Dummy {

    private String abc = "123";
  }
}