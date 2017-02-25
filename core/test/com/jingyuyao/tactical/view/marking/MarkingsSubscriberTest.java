package com.jingyuyao.tactical.view.marking;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectObject;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.terrain.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingsSubscriberTest {

  @Mock
  private Markings markings;
  @Mock
  private MarkerSprites markerSprites;
  @Mock
  private MapObject mapObject;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;
  @Mock
  private SelectObject<MapObject> selectObject;
  @Mock
  private PlayerState playerState;
  @Mock
  private ActivatedEnemy activatedEnemy;
  @Mock
  private Moving moving;
  @Mock
  private Movement movement;
  @Mock
  private SelectingTarget selectingTarget;
  @Mock
  private Battling battling;
  @Mock
  private ExitState exitState;
  @Mock
  private Target target;
  @Mock
  private Sprite sprite1;
  @Mock
  private Sprite sprite2;

  private MarkingsSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new MarkingsSubscriber(markings, markerSprites);
  }

  @Test
  public void select_object() {
    when(selectObject.getObject()).thenReturn(mapObject);

    subscriber.selectObject(selectObject);

    verify(markings).highlight(mapObject);
  }

  @Test
  public void player_state() {
    when(playerState.getPlayer()).thenReturn(player);

    subscriber.playerState(playerState);

    verify(markings).activate(player);
  }

  @Test
  public void activated_enemy() {
    when(activatedEnemy.getObject()).thenReturn(enemy);

    subscriber.activatedEnemy(activatedEnemy);

    verify(markings).activate(enemy);
  }

  @Test
  public void moving() {
    when(moving.getMovement()).thenReturn(movement);
    when(movement.getTerrains()).thenReturn(ImmutableList.of(terrain));
    when(markerSprites.getMove()).thenReturn(sprite1);

    subscriber.moving(moving);

    verify(markings).mark(terrain, sprite1);
  }

  @Test
  public void selecting_target() {
    when(selectingTarget.getTargets()).thenReturn(ImmutableList.of(target));
    when(target.getTargetTerrains()).thenReturn(ImmutableList.of(terrain));
    when(target.getSelectTerrains()).thenReturn(ImmutableList.of(terrain2));
    when(markerSprites.getAttack()).thenReturn(sprite1);
    when(markerSprites.getTargetSelect()).thenReturn(sprite2);

    subscriber.selectingTarget(selectingTarget);

    InOrder inOrder = Mockito.inOrder(markings);
    inOrder.verify(markings).mark(terrain, sprite1);
    inOrder.verify(markings).mark(terrain2, sprite2);
  }

  @Test
  public void battling() {
    when(battling.getTarget()).thenReturn(target);
    when(target.getTargetTerrains()).thenReturn(ImmutableList.of(terrain));
    when(target.getSelectTerrains()).thenReturn(ImmutableList.of(terrain2));
    when(markerSprites.getAttack()).thenReturn(sprite1);
    when(markerSprites.getTargetSelect()).thenReturn(sprite2);

    subscriber.battling(battling);

    InOrder inOrder = Mockito.inOrder(markings);
    inOrder.verify(markings).mark(terrain, sprite1);
    inOrder.verify(markings).mark(terrain2, sprite2);
  }

  @Test
  public void exit_state() {
    subscriber.exitState(exitState);

    verify(markings).clearMarked();
    verify(markings).activate(null);
  }
}