package com.jingyuyao.tactical.data;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.io.IOException;
import java.util.List;

class CoordinateAdapter extends TypeAdapter<Coordinate> {

  private static final Splitter SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
  private static final Joiner JOINER = Joiner.on(',');

  @Override
  public Coordinate read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    List<String> xy = SPLITTER.splitToList(in.nextString());
    int x = Integer.parseInt(xy.get(0));
    int y = Integer.parseInt(xy.get(1));
    return new Coordinate(x, y);
  }

  @Override
  public void write(JsonWriter out, Coordinate value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(JOINER.join(value.getX(), value.getY()));
  }
}
