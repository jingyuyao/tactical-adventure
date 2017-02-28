package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import java.util.LinkedHashSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterActorTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);
  private static final float ACTOR_SIZE = 10f;
  private static final float INITIAL_WORLD_X = COORDINATE.getX() * ACTOR_SIZE;
  private static final float INITIAL_WORLD_Y = COORDINATE.getY() * ACTOR_SIZE;
  private static final Coordinate DESTINATION = new Coordinate(3, 3);
  private static final Coordinate TRACK1 = new Coordinate(2, 3);
  private static final float MOVE_TIME_PER_UNIT = 1f;

  @Mock
  private Character character;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private LinkedHashSet<Sprite> markers;
  @Mock
  private LoopAnimation loopAnimation;
  @Mock
  private TextureRegion textureRegion;
  @Mock
  private Batch batch;
  @Mock
  private Sprite mSprite1;
  @Mock
  private Sprite mSprite2;
  @Mock
  private InstantMove instantMove;
  @Mock
  private EventListener listener;
  @Mock
  private Move move;
  @Mock
  private Path path;

  private CharacterActor<Character> characterActor;

  @Before
  public void setUp() {
    when(character.getCoordinate()).thenReturn(COORDINATE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    characterActor = new CharacterActor<>(character, actorConfig, markers, loopAnimation);

    assertThat(characterActor.getX()).isEqualTo(INITIAL_WORLD_X);
    assertThat(characterActor.getY()).isEqualTo(INITIAL_WORLD_Y);
    assertThat(characterActor.getWidth()).isEqualTo(ACTOR_SIZE);
    assertThat(characterActor.getHeight()).isEqualTo(ACTOR_SIZE);
    verify(character).registerListener(characterActor);
  }

  @Test
  public void draw() {
    when(markers.iterator()).thenReturn(ImmutableList.of(mSprite1, mSprite2).iterator());
    when(loopAnimation.getCurrentFrame()).thenReturn(textureRegion);

    characterActor.draw(batch, 0);

    InOrder inOrder = Mockito.inOrder(batch, mSprite1, mSprite2);
    inOrder.verify(batch).setColor(characterActor.getColor());
    inOrder
        .verify(batch)
        .draw(
            textureRegion, characterActor.getX(), characterActor.getY(),
            characterActor.getWidth(), characterActor.getHeight());
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder
        .verify(mSprite1)
        .setBounds(
            characterActor.getX(), characterActor.getY(), characterActor.getWidth(),
            characterActor.getHeight());
    inOrder.verify(mSprite1).draw(batch);
    inOrder
        .verify(mSprite2)
        .setBounds(
            characterActor.getX(), characterActor.getY(), characterActor.getWidth(),
            characterActor.getHeight());
    inOrder.verify(mSprite2).draw(batch);
  }

  @Test
  public void instant_move() {
    when(instantMove.getDestination()).thenReturn(DESTINATION);

    characterActor.instantMove(instantMove);

    assertThat(characterActor.getX()).isEqualTo(DESTINATION.getX() * ACTOR_SIZE);
    assertThat(characterActor.getY()).isEqualTo(DESTINATION.getY() * ACTOR_SIZE);
  }

  @Test
  public void move() {
    when(move.getPath()).thenReturn(path);
    when(path.getTrack()).thenReturn(ImmutableList.of(TRACK1, DESTINATION));
    when(actorConfig.getMoveTimePerUnit()).thenReturn(MOVE_TIME_PER_UNIT);
    characterActor.addListener(listener);

    characterActor.move(move);

    verify(move, times(0)).done();
    assertThat(characterActor.getListeners()).isEmpty();
    assertThat(characterActor.getActions()).hasSize(1);
    assertThat(characterActor.getX()).isEqualTo(INITIAL_WORLD_X);
    assertThat(characterActor.getY()).isEqualTo(INITIAL_WORLD_Y);

    characterActor.act(MOVE_TIME_PER_UNIT);

    verify(move, times(0)).done();
    assertThat(characterActor.getListeners()).isEmpty();
    assertThat(characterActor.getActions()).hasSize(1);
    assertThat(characterActor.getX()).isEqualTo(TRACK1.getX() * ACTOR_SIZE);
    assertThat(characterActor.getY()).isEqualTo(TRACK1.getY() * ACTOR_SIZE);

    characterActor.act(MOVE_TIME_PER_UNIT);

    verify(move, times(0)).done();
    assertThat(characterActor.getListeners()).isEmpty();
    assertThat(characterActor.getActions()).hasSize(1);
    assertThat(characterActor.getX()).isEqualTo(DESTINATION.getX() * ACTOR_SIZE);
    assertThat(characterActor.getY()).isEqualTo(DESTINATION.getY() * ACTOR_SIZE);

    // Triggers end of sequence actions
    characterActor.act(0f);

    verify(move).done();
    assertThat(characterActor.getListeners()).containsExactly(listener);
    assertThat(characterActor.getActions()).isEmpty();
    assertThat(characterActor.getX()).isEqualTo(DESTINATION.getX() * ACTOR_SIZE);
    assertThat(characterActor.getY()).isEqualTo(DESTINATION.getY() * ACTOR_SIZE);
  }
}