package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import javax.annotation.Nullable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class NullCheckAdapterFactoryTest {

  private static final String VALID1 = "{\"hello\": \"nihao\"}";
  private static final String VALID2 = "{\"hello\": \"nihao\", \"canNull\": null}";
  private static final String INVALID1 = "{\"canNull\": null}";
  private static final String INVALID2 = "{\"hello\": null}";

  private Gson gson;

  @Before
  public void setUp() {
    gson = new GsonBuilder().registerTypeAdapterFactory(new NullCheckAdapterFactory()).create();
  }

  @Test
  public void valid1() {
    Dummy dummy = gson.fromJson(VALID1, Dummy.class);
    assertThat(dummy.hello).isEqualTo("nihao");
    assertThat(dummy.canNull).isNull();
  }

  @Test
  public void valid2() {
    Dummy dummy = gson.fromJson(VALID2, Dummy.class);
    assertThat(dummy.hello).isEqualTo("nihao");
    assertThat(dummy.canNull).isNull();
  }

  @Test(expected = JsonParseException.class)
  public void invalid1() {
    gson.fromJson(INVALID1, Dummy.class);
  }

  @Test(expected = JsonParseException.class)
  public void invalid2() {
    gson.fromJson(INVALID2, Dummy.class);
  }

  private static class Dummy {

    private final String hello;
    @Nullable
    private final String canNull;

    private Dummy(String hello, String canNull) {
      this.hello = hello;
      this.canNull = canNull;
    }
  }
}