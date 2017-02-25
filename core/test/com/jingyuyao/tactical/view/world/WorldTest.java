package com.jingyuyao.tactical.view.world;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.controller.ControllerFactory;
import com.jingyuyao.tactical.controller.WorldActorController;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorFactory;
import com.jingyuyao.tactical.view.actor.EnemyActor;
import com.jingyuyao.tactical.view.actor.PlayerActor;
import com.jingyuyao.tactical.view.actor.TerrainActor;
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
public class WorldTest {

  private static final String NAME = "popcorn";

  @Mock
  private Stage stage;
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
  private Map<String, Sprite> characterSprites;
  @Mock
  private Viewport viewport;
  @Mock
  private OrthographicCamera camera;
  @Mock
  private MapObject mapObject;
  @Mock
  private WorldActor<?> worldActor;
  @Mock
  private Sprite sprite;
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
  private WorldActorController controller;

  private World world;

  @Before
  public void setUp() {
    world = new World(stage, characterGroup, terrainGroup, actorMap, mapRenderer, actorFactory,
        controllerFactory, characterSprites);
  }

  @Test
  public void act() {
    world.act(10f);

    verify(stage).act(10f);
  }

  @Test
  public void draw() {
    when(stage.getCamera()).thenReturn(camera);
    when(stage.getViewport()).thenReturn(viewport);

    world.draw();

    InOrder inOrder = Mockito.inOrder(stage, viewport, camera, mapRenderer);
    inOrder.verify(viewport).apply();
    inOrder.verify(mapRenderer).setView(camera);
    inOrder.verify(mapRenderer).render();
    inOrder.verify(stage).draw();
  }

  @Test
  public void resize() {
    when(stage.getViewport()).thenReturn(viewport);

    world.resize(10, 15);

    verify(viewport).update(10, 15);
  }

  @Test
  public void dispose() {
    world.dispose();

    verify(stage).dispose();
  }

  @Test
  public void get() {
    // black magic to qualify return type as wildcard
    Mockito.<WorldActor<?>>when(actorMap.get(mapObject)).thenReturn(worldActor);

    assertThat(world.get(mapObject)).isSameAs(worldActor);
  }

  @Test
  public void add_player() {
    when(characterSprites.get(NAME)).thenReturn(sprite);
    when(player.getName()).thenReturn(NAME);
    when(actorFactory.create(player, sprite)).thenReturn(playerActor);
    when(controllerFactory.create(player)).thenReturn(controller);

    world.add(player);

    verify(playerActor).addListener(controller);
    verify(characterGroup).addActor(playerActor);
    verify(actorMap).put(player, playerActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_enemy() {
    when(characterSprites.get(NAME)).thenReturn(sprite);
    when(enemy.getName()).thenReturn(NAME);
    when(actorFactory.create(enemy, sprite)).thenReturn(enemyActor);
    when(controllerFactory.create(enemy)).thenReturn(controller);

    world.add(enemy);

    verify(enemyActor).addListener(controller);
    verify(characterGroup).addActor(enemyActor);
    verify(actorMap).put(enemy, enemyActor);
    verifyZeroInteractions(terrainGroup);
  }

  @Test
  public void add_terrain() {
    when(actorFactory.create(terrain)).thenReturn(terrainActor);
    when(controllerFactory.create(terrain)).thenReturn(controller);

    world.add(terrain);

    verify(terrainActor).addListener(controller);
    verify(terrainGroup).addActor(terrainActor);
    verify(actorMap).put(terrain, terrainActor);
    verifyZeroInteractions(characterGroup);
  }

  @Test
  public void remove() {
    when(actorMap.containsKey(mapObject)).thenReturn(true);
    Mockito.<WorldActor<?>>when(actorMap.remove(mapObject)).thenReturn(worldActor);

    world.remove(mapObject);

    verify(actorMap).remove(mapObject);
    verify(worldActor).remove();
  }

  @Test(expected = IllegalArgumentException.class)
  public void remove_not_found() {
    when(actorMap.containsKey(mapObject)).thenReturn(false);

    world.remove(mapObject);
  }
}