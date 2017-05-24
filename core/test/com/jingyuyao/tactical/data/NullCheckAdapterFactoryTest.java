package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
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
  private static final String CHILD_VALID = "{\"hello\": \"nihao\", \"child\": \"hi\"}";
  private static final String CHILD_MISSING_PARENT = "{\"child\": \"hi\"}";

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

  @Test
  public void child_valid() {
    DummyChild dummyChild = gson.fromJson(CHILD_VALID, DummyChild.class);
    assertThat(dummyChild.hello).isEqualTo("nihao");
    assertThat(dummyChild.canNull).isNull();
    assertThat(dummyChild.child).isEqualTo("hi");
  }

  @Test(expected = JsonParseException.class)
  public void child_missing_parent_field() {
    gson.fromJson(CHILD_MISSING_PARENT, DummyChild.class);
  }

  private static class Dummy {

    final String hello;
    final transient String ignoreTransient;
    String canNull;

    private Dummy(String hello, String ignoreTransient) {
      this.hello = hello;
      this.ignoreTransient = ignoreTransient;
    }
  }

  private static class DummyChild extends Dummy {

    final String child;

    private DummyChild(String hello, String ignoreTransient, String child) {
      super(hello, ignoreTransient);
      this.child = child;
    }
  }
}