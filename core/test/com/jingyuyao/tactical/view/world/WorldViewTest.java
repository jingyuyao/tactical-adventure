package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.CharacterActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
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

  @Mock
  private Stage stage;
  @Mock
  private Group cellGroup;
  @Mock
  private Group characterGroup;
  @Mock
  private Group terrainGroup;
  @Mock
  private Map<Object, WorldActor> actorMap;
  @Mock
  private OrthogonalTiledMapRenderer mapRenderer;
  @Mock
  private ActorFactory actorFactory;
  @Mock
  private Viewport viewport;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private Object object;
  @Mock
  private WorldActor worldActor;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private CharacterActor characterActor;
  @Mock
  private Actor cellActor;
  @Mock
  private Cell cell;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView =
        new WorldView(
            stage, cellGroup, characterGroup, terrainGroup, actorMap, mapRenderer, actorFactory);
    InOrder inOrder = Mockito.inOrder(stage);
    inOrder.verify(stage).addActor(terrainGroup);
    inOrder.verify(stage).addActor(characterGroup);
    inOrder.verify(stage).addActor(cellGroup);
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
    when(actorMap.get(object)).thenReturn(worldActor);

    assertThat(worldView.get(object)).isSameAs(worldActor);
  }

  @Test
  public void add_cell_player() {
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(cell.hasPlayer()).thenReturn(true);
    when(cell.getPlayer()).thenReturn(player);
    when(cell.getTerrain()).thenReturn(terrain);
    when(actorFactory.create(player, COORDINATE)).thenReturn(characterActor);
    when(actorFactory.create(COORDINATE)).thenReturn(worldActor);
    when(actorFactory.create(cell)).thenReturn(cellActor);

    worldView.add(cell);

    verify(characterGroup).addActor(characterActor);
    verify(actorMap).put(player, characterActor);
    verify(terrainGroup).addActor(worldActor);
    verify(actorMap).put(terrain, worldActor);
    verify(cellGroup).addActor(cellActor);
  }

  @Test
  public void add_cell_enemy() {
    when(cell.getCoordinate()).thenReturn(COORDINATE);
    when(cell.hasPlayer()).thenReturn(false);
    when(cell.hasEnemy()).thenReturn(true);
    when(cell.getEnemy()).thenReturn(enemy);
    when(cell.getTerrain()).thenReturn(terrain);
    when(actorFactory.create(enemy, COORDINATE)).thenReturn(characterActor);
    when(actorFactory.create(COORDINATE)).thenReturn(worldActor);
    when(actorFactory.create(cell)).thenReturn(cellActor);

    worldView.add(cell);

    verify(characterGroup).addActor(characterActor);
    verify(actorMap).put(enemy, characterActor);
    verify(terrainGroup).addActor(worldActor);
    verify(actorMap).put(terrain, worldActor);
    verify(cellGroup).addActor(cellActor);
  }

  @Test
  public void add_player() {
    when(actorFactory.create(player, COORDINATE)).thenReturn(characterActor);

    worldView.add(COORDINATE, player);

    verify(characterGroup).addActor(characterActor);
    verify(actorMap).put(player, characterActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_enemy() {
    when(actorFactory.create(enemy, COORDINATE)).thenReturn(characterActor);

    worldView.add(COORDINATE, enemy);

    verify(characterGroup).addActor(characterActor);
    verify(actorMap).put(enemy, characterActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_terrain() {
    when(actorFactory.create(COORDINATE)).thenReturn(worldActor);

    worldView.add(COORDINATE, terrain);

    verify(terrainGroup).addActor(worldActor);
    verify(actorMap).put(terrain, worldActor);
    verifyZeroInteractions(characterGroup);
  }

  @Test
  public void remove() {
    when(actorMap.containsKey(object)).thenReturn(true);
    when(actorMap.remove(object)).thenReturn(worldActor);

    worldView.remove(object);

    verify(actorMap).remove(object);
    verify(worldActor).remove();
  }

  @Test(expected = IllegalArgumentException.class)
  public void remove_not_found() {
    when(actorMap.containsKey(object)).thenReturn(false);

    worldView.remove(object);
  }
}