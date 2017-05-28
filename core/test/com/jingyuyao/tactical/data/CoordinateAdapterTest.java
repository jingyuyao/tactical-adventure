package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.world.Coordinate;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CoordinateAdapterTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final String JSON = "2-2";

  @Mock
  private JsonReader reader;
  @Mock
  private JsonWriter writer;

  private CoordinateAdapter coordinateAdapter;

  @Before
  public void setUp() {
    coordinateAdapter = new CoordinateAdapter();
  }

  @Test
  public void read() throws IOException {
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(JSON);

    assertThat(coordinateAdapter.read(reader)).isEqualTo(COORDINATE);
  }

  @Test
  public void write() throws IOException {
    coordinateAdapter.write(writer, COORDINATE);

    verify(writer).value(JSON);
  }
}