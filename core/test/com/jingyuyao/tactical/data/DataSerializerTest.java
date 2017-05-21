package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataSerializerTest {

  private static final String JSON = "{\"s\":\"hello\",\"i\":2}";

  private DataSerializer dataSerializer;

  @Before
  public void setUp() {
    dataSerializer = new DataSerializer(new Gson());
  }

  @Test
  public void from_to() {
    InstStringReader reader = new InstStringReader(JSON);
    InstStringWriter writer = new InstStringWriter();

    dataSerializer.serialize(dataSerializer.deserialize(reader, Dummy.class), writer);

    assertThat(JSON).isEqualTo(writer.toString());
    assertThat(reader.closed).isTrue();
    assertThat(writer.closed).isTrue();
  }

  private static class Dummy {

    private String s;
    private int i;
  }

  private static class InstStringReader extends StringReader {

    private boolean closed;

    private InstStringReader(String s) {
      super(s);
    }

    @Override
    public void close() {
      super.close();
      closed = true;
    }
  }

  private static class InstStringWriter extends StringWriter {

    private boolean closed;

    @Override
    public void close() throws IOException {
      super.close();
      closed = true;
    }
  }
}