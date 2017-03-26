package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
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
public class PlayerActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;
  private static final float INITIAL_WORLD_X = COORDINATE.getX() * ACTOR_SIZE;
  private static final float INITIAL_WORLD_Y = COORDINATE.getY() * ACTOR_SIZE;
  private static final float MOVE_TIME_PER_UNIT = 2f;

  @Mock
  private Player player;
  @Mock
  private LinkedHashSet<WorldTexture> markers;
  @Mock
  private LoopAnimation loopAnimation;
  @Mock
  private WorldTexture animationFrame;
  @Mock
  private Batch batch;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private PlayerActor playerActor;

  @Before
  public void setUp() {
    playerActor = new PlayerActor(player, MOVE_TIME_PER_UNIT, markers, loopAnimation);

    playerActor.setSize(ACTOR_SIZE, ACTOR_SIZE);
    playerActor.setPosition(INITIAL_WORLD_X, INITIAL_WORLD_Y);
  }

  @Test
  public void draw_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(texture1, texture2).iterator());
    when(player.isActionable()).thenReturn(true);
    when(loopAnimation.getCurrentFrame()).thenReturn(animationFrame);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.WHITE);
    InOrder inOrder = Mockito.inOrder(batch, animationFrame, texture1, texture2);
    inOrder.verify(batch).setColor(playerActor.getColor());
    inOrder.verify(animationFrame).draw(batch, playerActor);
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(texture1).draw(batch, playerActor);
    inOrder.verify(texture2).draw(batch, playerActor);
  }

  @Test
  public void draw_not_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(texture1, texture2).iterator());
    when(player.isActionable()).thenReturn(false);
    when(loopAnimation.getCurrentFrame()).thenReturn(animationFrame);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.GRAY);
    InOrder inOrder = Mockito.inOrder(batch, animationFrame, texture1, texture2);
    inOrder.verify(batch).setColor(playerActor.getColor());
    inOrder.verify(animationFrame).draw(batch, playerActor);
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(texture1).draw(batch, playerActor);
    inOrder.verify(texture2).draw(batch, playerActor);
  }
}