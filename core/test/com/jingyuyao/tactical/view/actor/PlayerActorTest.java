package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Coordinate;
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

  @Mock
  private Player player;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<WorldTexture> markers;
  @Mock
  private LoopAnimation loopAnimation;
  @Mock
  private TextureRegion textureRegion;
  @Mock
  private Batch batch;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private PlayerActor playerActor;

  @Before
  public void setUp() {
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    playerActor = new PlayerActor(player, actorConfig, markers, loopAnimation);

    assertThat(playerActor.getX()).isEqualTo(INITIAL_WORLD_X);
    assertThat(playerActor.getY()).isEqualTo(INITIAL_WORLD_Y);
    assertThat(playerActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(playerActor.getHeight()).isEqualTo(ACTOR_SIZE);
    verify(player).registerListener(playerActor);
  }

  @Test
  public void draw_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(texture1, texture2).iterator());
    when(player.isActionable()).thenReturn(true);
    when(loopAnimation.getCurrentFrame()).thenReturn(textureRegion);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.WHITE);
    InOrder inOrder = Mockito.inOrder(batch, texture1, texture2);
    inOrder.verify(batch).setColor(playerActor.getColor());
    inOrder
        .verify(batch)
        .draw(
            textureRegion, playerActor.getX(), playerActor.getY(),
            playerActor.getWidth(), playerActor.getHeight());
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(texture1).draw(batch, playerActor);
    inOrder.verify(texture2).draw(batch, playerActor);
  }

  @Test
  public void draw_not_actionable() {
    when(markers.iterator()).thenReturn(ImmutableList.of(texture1, texture2).iterator());
    when(player.isActionable()).thenReturn(false);
    when(loopAnimation.getCurrentFrame()).thenReturn(textureRegion);

    playerActor.draw(batch, 0);

    assertThat(playerActor.getColor()).isEqualTo(Color.GRAY);
    InOrder inOrder = Mockito.inOrder(batch, texture1, texture2);
    inOrder.verify(batch).setColor(playerActor.getColor());
    inOrder
        .verify(batch)
        .draw(
            textureRegion, playerActor.getX(), playerActor.getY(),
            playerActor.getWidth(), playerActor.getHeight());
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(texture1).draw(batch, playerActor);
    inOrder.verify(texture2).draw(batch, playerActor);
  }
}