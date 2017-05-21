package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.jingyuyao.tactical.model.ship.ShipGroup;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ShipGroupAdapterTest {

  private static final String JSON = "best team";
  private static final ShipGroup GROUP = new ShipGroup(JSON);

  @Mock
  private JsonReader reader;
  @Mock
  private JsonWriter writer;

  private ShipGroupAdapter shipGroupAdapter;

  @Before
  public void setUp() {
    shipGroupAdapter = new ShipGroupAdapter();
  }

  @Test
  public void read() throws IOException {
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(JSON);

    assertThat(shipGroupAdapter.read(reader)).isEqualTo(GROUP);
  }

  @Test
  public void write() throws IOException {
    shipGroupAdapter.write(writer, GROUP);

    verify(writer).value(JSON);
  }
}