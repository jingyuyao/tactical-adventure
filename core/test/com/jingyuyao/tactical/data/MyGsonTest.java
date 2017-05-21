package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyGsonTest {

  private static final String JSON = "{\"s\":\"hello\",\"i\":2}";

  private MyGson myGson;

  @Before
  public void setUp() {
    myGson = new MyGson(new Gson());
  }

  @Test
  public void from_to() {
    Reader reader = new StringReader(JSON);
    Writer writer = new StringWriter();

    myGson.toJson(myGson.fromJson(reader, Dummy.class), writer);

    assertThat(JSON).isEqualTo(writer.toString());
  }

  private static class Dummy {

    private String s;
    private int i;
  }
}