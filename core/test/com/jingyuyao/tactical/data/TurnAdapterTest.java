package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TurnAdapterTest {

  private static final Turn TURN = new Turn(3, TurnStage.START);
  private static final String JSON = "3-START";

  @Mock
  private JsonReader reader;
  @Mock
  private JsonWriter writer;

  private TurnAdapter turnAdapter;

  @Before
  public void setUp() {
    turnAdapter = new TurnAdapter();
  }

  @Test
  public void read() throws IOException {
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(JSON);

    assertThat(turnAdapter.read(reader)).isEqualTo(TURN);
  }

  @Test
  public void write() throws IOException {
    turnAdapter.write(writer, TURN);

    verify(writer).value(JSON);
  }
}