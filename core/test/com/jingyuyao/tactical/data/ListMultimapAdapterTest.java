package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.base.Objects;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ListMultimapAdapterTest {

  private static final String JSON = "[[{\"k\":\"key\"},[{\"v\":\"value\"}]]]";

  private Gson gson;

  @Before
  public void setUp() {
    gson = new GsonBuilder()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(ListMultimap.class, new ListMultimapAdapter())
        .create();
  }

  @Test
  public void serialize() {
    ListMultimap<Key, Value> listMultimap = ArrayListMultimap.create();
    listMultimap.put(new Key(), new Value());

    String json = gson.toJson(listMultimap, new TypeToken<ListMultimap<Key, Value>>() {
    }.getType());

    assertThat(json).isEqualTo(JSON);
  }

  @Test
  public void deserialize() {
    ListMultimap<Key, Value> listMultimap = gson
        .fromJson(JSON, new TypeToken<ListMultimap<Key, Value>>() {
        }.getType());

    assertThat(listMultimap).containsEntry(new Key(), new Value());
  }

  @Test
  public void serialize_deserialize() {
    ListMultimap<Key, Value> listMultimap = ArrayListMultimap.create();
    listMultimap.put(new Key(), new Value());

    String serialized = gson.toJson(listMultimap, new TypeToken<ListMultimap<Key, Value>>() {
    }.getType());

    ListMultimap<Key, Value> deserialized = gson
        .fromJson(serialized, new TypeToken<ListMultimap<Key, Value>>() {
        }.getType());

    assertThat(listMultimap).isEqualTo(deserialized);

    String serializedAgain = gson.toJson(deserialized, new TypeToken<ListMultimap<Key, Value>>() {
    }.getType());

    assertThat(serialized).isEqualTo(serializedAgain);
  }

  @Test
  public void with_container() {
    Container container = new Container();

    String json = gson.toJson(container);

    Container deserialized = gson.fromJson(json, Container.class);

    assertThat(container.map).isEqualTo(deserialized.map);
  }

  private static class Container {

    private ListMultimap<Key, Value> map = ImmutableListMultimap.of(new Key(), new Value());
  }

  private static class Key {

    private String k = "key";

    @Override
    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }
      if (object == null || getClass() != object.getClass()) {
        return false;
      }
      Key key = (Key) object;
      return Objects.equal(k, key.k);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(k);
    }
  }

  private static class Value {

    private String v = "value";

    @Override
    public boolean equals(Object object) {
      if (this == object) {
        return true;
      }
      if (object == null || getClass() != object.getClass()) {
        return false;
      }
      Value value = (Value) object;
      return Objects.equal(v, value.v);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(v);
    }
  }
}