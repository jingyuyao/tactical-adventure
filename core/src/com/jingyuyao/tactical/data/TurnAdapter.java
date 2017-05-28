package com.jingyuyao.tactical.data;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
import java.util.List;

class TurnAdapter extends TypeAdapter<Turn> {

  private static final char DELIMITER = '-';
  private static final Splitter SPLITTER = Splitter.on(DELIMITER).omitEmptyStrings().trimResults();
  private static final Joiner JOINER = Joiner.on(DELIMITER);

  @Override
  public Turn read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    List<String> numStage = SPLITTER.splitToList(in.nextString());
    if (numStage.size() != 2) {
      throw new IOException("Turn need to have a number and a stage");
    }
    int num = Integer.valueOf(numStage.get(0));
    TurnStage stage = TurnStage.valueOf(numStage.get(1));
    return new Turn(num, stage);
  }

  @Override
  public void write(JsonWriter out, Turn value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(JOINER.join(value.getNumber(), value.getStage()));
  }
}
