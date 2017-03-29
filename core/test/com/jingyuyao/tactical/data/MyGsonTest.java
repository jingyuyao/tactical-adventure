package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
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

  @Test
  public void deep_copy() {
    Dummy dummy = new Dummy();
    dummy.i = 100;
    dummy.s = "abc";

    Dummy dummy2 = myGson.deepCopy(dummy, Dummy.class);

    assertThat(dummy2).isNotSameAs(dummy);
    assertThat(dummy2.i).isEqualTo(dummy.i);
    assertThat(dummy2.s).isEqualTo(dummy.s);
  }

  @Test
  public void deep_copy_generics() {
    Dummy dummy = new Dummy();
    dummy.i = 100;
    dummy.s = "abc";

    Gen<Dummy> gen = new Gen<>();
    gen.data = dummy;

    Type type = new TypeToken<Gen<Dummy>>() {
    }.getType();
    Gen<Dummy> gen2 = myGson.deepCopy(gen, type);

    assertThat(gen2).isNotSameAs(gen);
    assertThat(gen2.data).isNotSameAs(dummy);
    assertThat(gen2.data.i).isEqualTo(dummy.i);
    assertThat(gen2.data.s).isEqualTo(dummy.s);
  }

  private static class Dummy {

    private String s;
    private int i;
  }

  private static class Gen<T> {

    private T data;
  }
}