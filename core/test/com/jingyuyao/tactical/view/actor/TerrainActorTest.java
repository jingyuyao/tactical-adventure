package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.LinkedHashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;

  @Mock
  private Terrain terrain;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<Sprite> markers;

  @Test
  public void creation() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    TerrainActor terrainActor = new TerrainActor(terrain, actorConfig, markers);

    assertThat(terrainActor.getX()).isEqualTo(COORDINATE.getX() * ACTOR_SIZE);
    assertThat(terrainActor.getY()).isEqualTo(COORDINATE.getY() * ACTOR_SIZE);
    assertThat(terrainActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(terrainActor.getHeight()).isEqualTo(ACTOR_SIZE);
  }
}