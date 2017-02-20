package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
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
  private MapObject mapObject;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<Sprite> markers;
  @Mock
  private Sprite sprite1;
  @Mock
  private Sprite sprite2;
  @Mock
  private Batch batch;

  private WorldActor<MapObject> worldActor;

  @Before
  public void setUp() {
    when(mapObject.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    worldActor = new WorldActor<>(mapObject, actorConfig, markers);

    assertThat(worldActor.getX()).isEqualTo(COORDINATE.getX() * ACTOR_SIZE);
    assertThat(worldActor.getY()).isEqualTo(COORDINATE.getY() * ACTOR_SIZE);
    assertThat(worldActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(worldActor.getHeight()).isEqualTo(ACTOR_SIZE);
  }

  @Test
  public void draw() {
    when(markers.iterator()).thenReturn(ImmutableList.of(sprite1, sprite2).iterator());

    worldActor.draw(batch, 0);

    InOrder inOrder = Mockito.inOrder(sprite1, sprite2);
    inOrder
        .verify(sprite1)
        .setBounds(
            worldActor.getX(), worldActor.getY(), worldActor.getWidth(), worldActor.getHeight());
    inOrder.verify(sprite1).draw(batch);
    inOrder
        .verify(sprite2)
        .setBounds(
            worldActor.getX(), worldActor.getY(), worldActor.getWidth(), worldActor.getHeight());
    inOrder.verify(sprite2).draw(batch);
  }

  @Test
  public void add_marker() {
    worldActor.addMarker(sprite1);

    verify(markers).add(sprite1);
  }

  @Test
  public void clear_markers() {
    worldActor.clearMarkers();

    markers.clear();
  }
}