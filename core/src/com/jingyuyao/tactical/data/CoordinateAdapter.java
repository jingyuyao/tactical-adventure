package com.jingyuyao.tactical.data;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.io.IOException;

class CoordinateAdapter extends TypeAdapter<Coordinate> {

  @Override
  public Coordinate read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    String xy = in.nextString();
    if (xy.length() < 3) {
      throw new IOException(
          "Coordinate in JSON should be at least five characters long i.e \"1,2\"");
    }
    String[] parts = xy.split(",");
    int x = Integer.parseInt(parts[0]);
    int y = Integer.parseInt(parts[1]);
    return new Coordinate(x, y);
  }

  @Override
  public void write(JsonWriter out, Coordinate value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    String xy = value.getX() + "," + value.getY();
    out.value(xy);
  }
}
