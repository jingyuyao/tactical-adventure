package com.jingyuyao.tactical.view.world;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.InstantMoveCharacter;
import com.jingyuyao.tactical.model.event.MoveCharacter;
import com.jingyuyao.tactical.model.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.SpawnCharacter;
import com.jingyuyao.tactical.model.event.WorldLoad;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.model.world.Path;
import com.jingyuyao.tactical.view.actor.CharacterActor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldSubscriberTest {

  private static final Coordinate COORDINATE = new Coordinate(2, 2);

  @Mock
  private WorldView worldView;
  @Mock
  private WorldLoad worldLoad;
  @Mock
  private WorldReset worldReset;
  @Mock
  private SpawnCharacter spawnCharacter;
  @Mock
  private RemoveCharacter removeCharacter;
  @Mock
  private InstantMoveCharacter instantMoveCharacter;
  @Mock
  private MoveCharacter moveCharacter;
  @Mock
  private Cell cell;
  @Mock
  private Character character;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Path path;
  @Mock
  private CharacterActor characterActor;

  private WorldSubscriber subscriber;

  @Before
  public void setUp() {
    subscriber = new WorldSubscriber(worldView);
  }

  @Test
  public void world_load() {
    when(worldLoad.getObject()).thenReturn(ImmutableList.of(cell));
    when(cell.getTerrain()).thenReturn(terrain);
    when(cell.getCoordinate()).thenReturn(COORDINATE);

    subscriber.worldLoad(worldLoad);

    verify(worldView).add(COORDINATE, terrain);
  }

  @Test
  public void world_reset() {
    subscriber.worldReset(worldReset);

    verify(worldView).reset();
  }

  @Test
  public void spawn_player() {
    when(spawnCharacter.getObject()).thenReturn(cell);
    when(cell.hasPlayer()).thenReturn(true);
    when(cell.getPlayer()).thenReturn(player);
    when(cell.getCoordinate()).thenReturn(COORDINATE);

    subscriber.spawnCharacter(spawnCharacter);

    verify(worldView).add(COORDINATE, player);
  }

  @Test
  public void spawn_enemy() {
    when(spawnCharacter.getObject()).thenReturn(cell);
    when(cell.hasEnemy()).thenReturn(true);
    when(cell.getEnemy()).thenReturn(enemy);
    when(cell.getCoordinate()).thenReturn(COORDINATE);

    subscriber.spawnCharacter(spawnCharacter);

    verify(worldView).add(COORDINATE, enemy);
  }

  @Test
  public void remove_character() {
    when(removeCharacter.getObject()).thenReturn(character);

    subscriber.removeCharacter(removeCharacter);

    verify(worldView).remove(character);
  }

  @Test
  public void instant_move_character() {
    when(instantMoveCharacter.getCharacter()).thenReturn(character);
    when(instantMoveCharacter.getDestination()).thenReturn(cell);
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(worldView.get(character)).thenReturn(characterActor);

    subscriber.instantMoveCharacter(instantMoveCharacter);

    verify(characterActor).moveTo(COORDINATE);
  }

  @Test
  public void move_character() {
    SettableFuture<Void> future = SettableFuture.create();
    when(moveCharacter.getCharacter()).thenReturn(character);
    when(moveCharacter.getPath()).thenReturn(path);
    when(moveCharacter.getFuture()).thenReturn(future);
    when(worldView.get(character)).thenReturn(characterActor);

    subscriber.moveCharacter(moveCharacter);

    verify(characterActor).moveAlong(path, future);
  }
}