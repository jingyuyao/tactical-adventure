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

  private static final String DUMMY = "{\"s\":\"hello\",\"i\":2}";
  private static final String DUMMY2 = "{\"str\":\"other\",\"num\":3}";

  private DataSerializer dataSerializer;

  @Before
  public void setUp() {
    dataSerializer = new DataSerializer(new Gson());
  }

  @Test
  public void empty_final_fields() {
    InstStringReader reader = new InstStringReader(DUMMY);
    InstStringWriter writer = new InstStringWriter();

    Dummy dummy = dataSerializer.deserialize(reader, Dummy.class);

    assertThat(dummy.s).isEqualTo("hello");
    assertThat(dummy.i).isEqualTo(2);
    assertThat(dummy.preset).isNull();

    dataSerializer.serialize(dummy, writer);

    assertThat(DUMMY).isEqualTo(writer.toString());
    assertThat(reader.closed).isTrue();
    assertThat(writer.closed).isTrue();
  }

  @Test
  public void final_field_initialization() {
    InstStringReader reader = new InstStringReader(DUMMY2);

    Dummy2 dummy2 = dataSerializer.deserialize(reader, Dummy2.class);

    assertThat(dummy2.num).isEqualTo(2);
    assertThat(dummy2.str).isEqualTo("not replaced");
    assertThat(dummy2.preset).isEqualTo("default");
    assertThat(reader.closed).isTrue();
  }

  private static class Dummy {

    // only uninitialized final fields are loaded by Gson
    private final String s;
    private final int i;
    // preset is not set to world since Gson does not invoke the constructor
    private final String preset;

    // constructor is not invoked
    private Dummy(String s, int i) {
      this.s = s;
      this.i = i;
      preset = "world";
      throw new AssertionError();
    }
  }

  private static class Dummy2 {

    private final int num = 2; // initialized value is not replaced by json
    private final String str = "not replaced"; // initialized value is not replaced by json
    private final String preset = "default"; // field not in json is also kept
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