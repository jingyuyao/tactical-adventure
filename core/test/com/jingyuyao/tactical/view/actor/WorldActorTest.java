package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;

  @Mock
  private LinkedHashSet<WorldTexture> markers;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;
  @Mock
  private Batch batch;

  private WorldActor worldActor;

  @Before
  public void setUp() {
    worldActor = new WorldActor(markers);

    worldActor.setSize(ACTOR_SIZE, ACTOR_SIZE);
  }

  @Test
  public void draw() {
    when(markers.iterator()).thenReturn(ImmutableList.of(texture1, texture2).iterator());

    worldActor.draw(batch, 0);

    InOrder inOrder = Mockito.inOrder(texture1, texture2);
    inOrder.verify(texture1).draw(batch, worldActor);
    inOrder.verify(texture2).draw(batch, worldActor);
  }

  @Test
  public void add_marker() {
    worldActor.addMarker(texture1);

    verify(markers).add(texture1);
  }

  @Test
  public void clear_markers() {
    worldActor.clearMarkers();

    markers.clear();
  }

  @Test
  public void update_coordinate() {
    worldActor.updateCoordinate(COORDINATE);

    assertThat(worldActor.getX()).isEqualTo(COORDINATE.getX() * ACTOR_SIZE);
    assertThat(worldActor.getY()).isEqualTo(COORDINATE.getY() * ACTOR_SIZE);
  }
}