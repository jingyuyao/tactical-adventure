package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Coordinate;
import java.util.LinkedHashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;
  private static final float INITIAL_WORLD_X = COORDINATE.getX() * ACTOR_SIZE;
  private static final float INITIAL_WORLD_Y = COORDINATE.getY() * ACTOR_SIZE;

  @Mock
  private Player player;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<Sprite> markers;
  @Mock
  private Sprite sprite;
  @Mock
  private Batch batch;
  @Mock
  private Sprite mSprite1;
  @Mock
  private Sprite mSprite2;

  private PlayerActor playerActor;

  @Before
  public void setUp() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    playerActor = new PlayerActor(player, actorConfig, markers, sprite);

    assertThat(playerActor.getX()).isEqualTo(INITIAL_WORLD_X);
    assertThat(playerActor.getY()).isEqualTo(INITIAL_WORLD_Y);
    assertThat(playerActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(playerActor.getHeight()).isEqualTo(ACTOR_SIZE);
    verify(player).registerListener(playerActor);
  }

  @Test
  public void draw_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(mSprite1, mSprite2).iterator());
    when(player.isActionable()).thenReturn(true);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.WHITE);
    InOrder inOrder = Mockito.inOrder(sprite, mSprite1, mSprite2);
    inOrder
        .verify(sprite)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(sprite).draw(batch);
    inOrder
        .verify(mSprite1)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(mSprite1).draw(batch);
    inOrder
        .verify(mSprite2)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(mSprite2).draw(batch);
  }

  @Test
  public void draw_not_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(mSprite1, mSprite2).iterator());
    when(player.isActionable()).thenReturn(false);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.GRAY);
    InOrder inOrder = Mockito.inOrder(sprite, mSprite1, mSprite2);
    inOrder
        .verify(sprite)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(sprite).draw(batch);
    inOrder
        .verify(mSprite1)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(mSprite1).draw(batch);
    inOrder
        .verify(mSprite2)
        .setBounds(
            playerActor.getX(), playerActor.getY(), playerActor.getWidth(),
            playerActor.getHeight());
    inOrder.verify(mSprite2).draw(batch);
  }
}