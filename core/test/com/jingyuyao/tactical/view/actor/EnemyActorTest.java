package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.LinkedHashSet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnemyActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;
  private static final float INITIAL_WORLD_X = COORDINATE.getX() * ACTOR_SIZE;
  private static final float INITIAL_WORLD_Y = COORDINATE.getY() * ACTOR_SIZE;

  @Mock
  private Enemy enemy;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<Sprite> markers;
  @Mock
  private Sprite sprite;

  @Test
  public void creation() {
    when(enemy.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    EnemyActor enemyActor = new EnemyActor(enemy, actorConfig, markers, sprite);

    assertThat(enemyActor.getX()).isEqualTo(INITIAL_WORLD_X);
    assertThat(enemyActor.getY()).isEqualTo(INITIAL_WORLD_Y);
    assertThat(enemyActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(enemyActor.getHeight()).isEqualTo(ACTOR_SIZE);
    verify(enemy).registerListener(enemyActor);
    assertThat(enemyActor.getColor()).isEqualTo(Color.RED);
  }
}