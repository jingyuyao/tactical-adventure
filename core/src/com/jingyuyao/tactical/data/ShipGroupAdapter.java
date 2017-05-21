package com.jingyuyao.tactical.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import java.io.IOException;

class ShipGroupAdapter extends TypeAdapter<ShipGroup> {

  @Override
  public ShipGroup read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return new ShipGroup(in.nextString());
  }

  @Override
  public void write(JsonWriter out, ShipGroup value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(value.getName());
  }
}
