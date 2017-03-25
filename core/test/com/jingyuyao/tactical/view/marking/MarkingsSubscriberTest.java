package com.jingyuyao.tactical.view.marking;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.ActivatedEnemy;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.Moving;
import com.jingyuyao.tactical.model.state.PlayerState;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.CharacterActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.WorldView;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingsSubscriberTest {

  private static final String WEAPON_NAME = "axe";

  @Mock
  private WorldView worldView;
  @Mock
  private Markings markings;
  @Mock
  private Markers markers;
  @Mock
  private Animations animations;
  @Mock
  private Cell cell;
  @Mock
  private Cell cell2;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Terrain terrain2;
  @Mock
  private SelectCell selectCell;
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
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;
  @Mock
  private Attack attack;
  @Mock
  private Weapon weapon;
  @Mock
  private SingleAnimation singleAnimation;
  @Mock
  private Actor actor;
  @Mock
  private WorldActor worldActor;
  @Mock
  private WorldActor worldActor2;
  @Mock
  private CharacterActor characterActor;

  private MarkingsSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new MarkingsSubscriber(worldView, markings, markers, animations);
  }

  @Test
  public void select_cell() {
    when(worldView.get(cell)).thenReturn(actor);
    when(selectCell.getObject()).thenReturn(cell);

    subscriber.selectCell(selectCell);

    verify(markings).highlight(actor);
  }

  @Test
  public void player_state() {
    when(worldView.get(player)).thenReturn(characterActor);
    when(playerState.getPlayer()).thenReturn(player);

    subscriber.playerState(playerState);

    verify(markings).activate(characterActor);
  }

  @Test
  public void activated_enemy() {
    when(worldView.get(enemy)).thenReturn(characterActor);
    when(activatedEnemy.getObject()).thenReturn(enemy);

    subscriber.activatedEnemy(activatedEnemy);

    verify(markings).activate(characterActor);
  }

  @Test
  public void moving() {
    when(cell.getTerrain()).thenReturn(terrain);
    when(worldView.get(terrain)).thenReturn(worldActor);
    when(moving.getMovement()).thenReturn(movement);
    when(movement.getCells()).thenReturn(ImmutableList.of(cell));
    when(markers.getMove()).thenReturn(texture1);

    subscriber.moving(moving);

    verify(markings).mark(worldActor, texture1);
  }

  @Test
  public void selecting_target() {
    when(selectingTarget.getTargets()).thenReturn(ImmutableList.of(target));
    when(target.getTargetCells()).thenReturn(ImmutableList.of(cell));
    when(target.getSelectCells()).thenReturn(ImmutableList.of(cell2));
    when(cell.getTerrain()).thenReturn(terrain);
    when(cell2.getTerrain()).thenReturn(terrain2);
    when(worldView.get(terrain)).thenReturn(worldActor);
    when(worldView.get(terrain2)).thenReturn(worldActor2);
    when(markers.getAttack()).thenReturn(texture1);
    when(markers.getTargetSelect()).thenReturn(texture2);

    subscriber.selectingTarget(selectingTarget);

    InOrder inOrder = Mockito.inOrder(markings);
    inOrder.verify(markings).mark(worldActor, texture1);
    inOrder.verify(markings).mark(worldActor2, texture2);
  }

  @Test
  public void battling() {
    when(battling.getTarget()).thenReturn(target);
    when(target.getTargetCells()).thenReturn(ImmutableList.of(cell));
    when(target.getSelectCells()).thenReturn(ImmutableList.of(cell2));
    when(cell.getTerrain()).thenReturn(terrain);
    when(cell2.getTerrain()).thenReturn(terrain2);
    when(worldView.get(terrain)).thenReturn(worldActor);
    when(worldView.get(terrain2)).thenReturn(worldActor2);
    when(markers.getAttack()).thenReturn(texture1);
    when(markers.getTargetSelect()).thenReturn(texture2);

    subscriber.battling(battling);

    InOrder inOrder = Mockito.inOrder(markings);
    inOrder.verify(markings).mark(worldActor, texture1);
    inOrder.verify(markings).mark(worldActor2, texture2);
  }

  @Test
  public void attack() {
    SettableFuture<Void> future = SettableFuture.create();
    when(attack.getWeapon()).thenReturn(weapon);
    when(attack.getObject()).thenReturn(target);
    when(target.getSelectCells()).thenReturn(ImmutableList.of(cell2));
    when(cell2.getTerrain()).thenReturn(terrain);
    when(worldView.get(terrain)).thenReturn(worldActor);
    when(weapon.getName()).thenReturn(WEAPON_NAME);
    when(animations.getWeapon(WEAPON_NAME)).thenReturn(singleAnimation);
    when(singleAnimation.getFuture()).thenReturn(future);

    subscriber.attack(attack);

    markings.addSingleAnimation(worldActor, singleAnimation);
    verify(attack, never()).done();

    future.set(null);

    verify(attack).done();
  }

  @Test
  public void exit_state() {
    subscriber.exitState(exitState);

    verify(markings).clearMarked();
    verify(markings).activate(null);
  }
}