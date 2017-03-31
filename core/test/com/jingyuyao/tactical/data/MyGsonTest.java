package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyGsonTest {

  private MyGson myGson;

  @Before
  public void setUp() {
    myGson = new MyGson(new Gson());
  }

  @Test
  public void from_to() {
    String input = "{\"s\":\"hello\",\"i\":2}";

    Dummy dummy = myGson.fromJson(input, Dummy.class);
    String output = myGson.toJson(dummy);

    assertThat(input).isEqualTo(output);
  }

  private static class Dummy {

    private String s;
    private int i;
  }
}