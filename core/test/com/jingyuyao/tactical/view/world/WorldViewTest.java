package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Coordinate;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.CharacterActor;
import com.jingyuyao.tactical.view.actor.WorldActor;
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
  private MapGroup<Character, CharacterActor> characterGroup;
  @Mock
  private MapGroup<Terrain, WorldActor> terrainGroup;
  @Mock
  private OrthogonalTiledMapRenderer mapRenderer;
  @Mock
  private ActorFactory actorFactory;
  @Mock
  private Viewport viewport;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private Cell cell;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Terrain terrain;
  @Mock
  private Actor cellActor;
  @Mock
  private WorldActor worldActor;
  @Mock
  private CharacterActor characterActor;

  private WorldView worldView;

  @Before
  public void setUp() {
    worldView = new WorldView(stage, characterGroup, terrainGroup, mapRenderer, actorFactory);
    InOrder inOrder = Mockito.inOrder(terrainGroup, characterGroup);
    inOrder.verify(terrainGroup).addToStage(stage);
    inOrder.verify(characterGroup).addToStage(stage);
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

    verify(characterGroup).clear();
    verify(terrainGroup).clear();
  }

  @Test
  public void get_terrain_actor() {
    when(terrainGroup.get(terrain)).thenReturn(worldActor);

    assertThat(worldView.get(terrain)).isSameAs(worldActor);
  }

  @Test
  public void get_character_actor() {
    when(characterGroup.get(player)).thenReturn(characterActor);

    assertThat(worldView.get(player)).isSameAs(characterActor);
  }

  @Test
  public void add_terrain() {
    when(actorFactory.create(COORDINATE)).thenReturn(worldActor);

    worldView.add(COORDINATE, terrain);

    verify(terrainGroup).add(terrain, worldActor);
  }

  @Test
  public void add_player() {
    when(actorFactory.create(player, COORDINATE)).thenReturn(characterActor);

    worldView.add(COORDINATE, player);

    verify(characterGroup).add(player, characterActor);
  }

  @Test
  public void add_enemy() {
    when(actorFactory.create(enemy, COORDINATE)).thenReturn(characterActor);

    worldView.add(COORDINATE, enemy);

    verify(characterGroup).add(enemy, characterActor);
  }

  @Test
  public void remove_character() {
    worldView.remove(player);

    verify(characterGroup).remove(player);
  }
}