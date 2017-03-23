package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.controller.CellController;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.CellActor;
import com.jingyuyao.tactical.view.actor.EnemyActor;
import com.jingyuyao.tactical.view.actor.PlayerActor;
import com.jingyuyao.tactical.view.actor.TerrainActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldViewTest {

  private static final Coordinate COORDINATE = new Coordinate(10, 10);
  private static final String NAME = "popcorn";

  @Mock
  private Stage stage;
  @Mock
  private Group cellGroup;
  @Mock
  private Group characterGroup;
  @Mock
  private Group terrainGroup;
  @Mock
  private Map<MapObject, WorldActor<?>> actorMap;
  @Mock
  private OrthogonalTiledMapRenderer mapRenderer;
  @Mock
  private ActorFactory actorFactory;
  @Mock
  private ControllerFactory controllerFactory;
  @Mock
  private CellController cellController;
  @Mock
  private Animations animations;
  @Mock
  private Viewport viewport;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private MapObject mapObject;
  @Mock
  private WorldActor<?> worldActor;
  @Mock
  private LoopAnimation loopAnimation;
  @Mock
  private Player player;
  @Mock
  private PlayerActor playerActor;
  @Mock
  private Enemy enemy;
  @Mock
  private EnemyActor enemyActor;
  @Mock
  private Terrain terrain;
  @Mock
  private TerrainActor terrainActor;
  @Mock
  private CellActor cellActor;
  @Mock
  private Cell cell;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(stage, cellGroup, characterGroup, terrainGroup, actorMap, mapRenderer,
        actorFactory,
        controllerFactory, animations);
    InOrder inOrder = Mockito.inOrder(stage);
    inOrder.verify(stage).addActor(terrainGroup);
    inOrder.verify(stage).addActor(characterGroup);
  }

  @Test
  public void act() {
    worldView.act(10f);

    verify(stage).act(10f);
  }

  @Test
  public void draw() {
    when(stage.getCamera()).thenReturn(camera);
    when(stage.getViewport()).thenReturn(viewport);

    worldView.draw();

    InOrder inOrder = Mockito.inOrder(stage, viewport, camera, mapRenderer);
    inOrder.verify(viewport).apply();
    inOrder.verify(mapRenderer).setView(camera);
    inOrder.verify(mapRenderer).render();
    inOrder.verify(stage).draw();
  }

  @Test
  public void resize() {
    when(stage.getViewport()).thenReturn(viewport);

    worldView.resize(10, 15);

    verify(viewport).update(10, 15);
  }

  @Test
  public void dispose() {
    worldView.dispose();

    verify(stage).dispose();
  }

  @Test
  public void reset() {
    worldView.reset();

    verify(cellGroup).clear();
    verify(characterGroup).clear();
    verify(terrainGroup).clear();
  }

  @Test
  public void get() {
    // black magic to qualify return type as wildcard
    Mockito.<WorldActor<?>>when(actorMap.get(mapObject)).thenReturn(worldActor);

    assertThat(worldView.get(mapObject)).isSameAs(worldActor);
  }

  @Test
  public void add_cell_player() {
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(cell.hasPlayer()).thenReturn(true);
    when(cell.getPlayer()).thenReturn(player);
    when(cell.getTerrain()).thenReturn(terrain);
    when(animations.getCharacter(NAME)).thenReturn(loopAnimation);
    when(player.getName()).thenReturn(NAME);
    when(actorFactory.create(player, COORDINATE, loopAnimation)).thenReturn(playerActor);
    when(actorFactory.create(terrain, COORDINATE)).thenReturn(terrainActor);
    when(actorFactory.create(cell, COORDINATE)).thenReturn(cellActor);
    when(controllerFactory.create(cell)).thenReturn(cellController);

    worldView.add(cell);

    verify(characterGroup).addActor(playerActor);
    verify(actorMap).put(player, playerActor);
    verify(terrainGroup).addActor(terrainActor);
    verify(actorMap).put(terrain, terrainActor);
    verify(cellGroup).addActor(cellActor);
    verify(cellActor).addListener(cellController);
  }

  @Test
  public void add_cell_enemy() {
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(cell.hasPlayer()).thenReturn(false);
    when(cell.hasEnemy()).thenReturn(true);
    when(cell.getEnemy()).thenReturn(enemy);
    when(cell.getTerrain()).thenReturn(terrain);
    when(animations.getCharacter(NAME)).thenReturn(loopAnimation);
    when(enemy.getName()).thenReturn(NAME);
    when(actorFactory.create(enemy, COORDINATE, loopAnimation)).thenReturn(enemyActor);
    when(actorFactory.create(terrain, COORDINATE)).thenReturn(terrainActor);
    when(actorFactory.create(cell, COORDINATE)).thenReturn(cellActor);
    when(controllerFactory.create(cell)).thenReturn(cellController);

    worldView.add(cell);

    verify(characterGroup).addActor(enemyActor);
    verify(actorMap).put(enemy, enemyActor);
    verify(terrainGroup).addActor(terrainActor);
    verify(actorMap).put(terrain, terrainActor);
    verify(cellGroup).addActor(cellActor);
    verify(cellActor).addListener(cellController);
  }

  @Test
  public void add_player() {
    when(animations.getCharacter(NAME)).thenReturn(loopAnimation);
    when(player.getName()).thenReturn(NAME);
    when(actorFactory.create(player, COORDINATE, loopAnimation)).thenReturn(playerActor);

    worldView.add(COORDINATE, player);

    verify(characterGroup).addActor(playerActor);
    verify(actorMap).put(player, playerActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_enemy() {
    when(animations.getCharacter(NAME)).thenReturn(loopAnimation);
    when(enemy.getName()).thenReturn(NAME);
    when(actorFactory.create(enemy, COORDINATE, loopAnimation)).thenReturn(enemyActor);

    worldView.add(COORDINATE, enemy);

    verify(characterGroup).addActor(enemyActor);
    verify(actorMap).put(enemy, enemyActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_terrain() {
    when(actorFactory.create(terrain, COORDINATE)).thenReturn(terrainActor);

    worldView.add(COORDINATE, terrain);

    verify(terrainGroup).addActor(terrainActor);
    verify(actorMap).put(terrain, terrainActor);
    verifyZeroInteractions(characterGroup);
  }

  @Test
  public void remove() {
    when(actorMap.containsKey(mapObject)).thenReturn(true);
    Mockito.<WorldActor<?>>when(actorMap.remove(mapObject)).thenReturn(worldActor);

    worldView.remove(mapObject);

    verify(actorMap).remove(mapObject);
    verify(worldActor).remove();
  }

  @Test(expected = IllegalArgumentException.class)
  public void remove_not_found() {
    when(actorMap.containsKey(mapObject)).thenReturn(false);

    worldView.remove(mapObject);
  }
}