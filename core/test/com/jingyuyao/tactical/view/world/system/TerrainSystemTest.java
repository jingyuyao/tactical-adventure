package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jingyuyao.tactical.model.event.WorldLoaded;
import com.jingyuyao.tactical.model.resource.IntKey;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Terrain;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.TileSets;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TerrainSystemTest {

  @Mock
  private TileSets tileSets;
  @Mock
  private WorldLoaded worldLoaded;
  @Mock
  private World world;
  @Mock
  private Cell cell1;
  @Mock
  private Cell cell2;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private IntKey intKey1;
  @Mock
  private IntKey intKey2;
  @Mock
  private WorldTexture texture1;
  @Mock
  private WorldTexture texture2;

  private Engine engine;
  private TerrainSystem terrainSystem;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    terrainSystem = new TerrainSystem(tileSets);
    assertThat(terrainSystem.priority).isEqualTo(SystemPriority.TERRAIN);
    engine.addSystem(terrainSystem);
  }

  @Test
  public void world_loaded() {
    when(worldLoaded.getWorld()).thenReturn(world);
    when(world.getWorldCells()).thenReturn(Arrays.asList(cell1, cell2));
    when(cell1.getCoordinate()).thenReturn(C0_0);
    when(cell2.getCoordinate()).thenReturn(C0_1);
    when(cell1.getTerrain()).thenReturn(terrain1);
    when(cell2.getTerrain()).thenReturn(terrain2);
    when(terrain1.getTexture()).thenReturn(intKey1);
    when(terrain2.getTexture()).thenReturn(intKey2);
    when(tileSets.get(intKey1)).thenReturn(texture1);
    when(tileSets.get(intKey2)).thenReturn(texture2);

    terrainSystem.worldLoaded(worldLoaded);

    ImmutableArray<Entity> entities = engine.getEntities();
    assertThat(entities).hasSize(2);
    Entity entity1 = entities.get(0);
    Position position1 = entity1.getComponent(Position.class);
    assertThat(position1).isNotNull();
    assertThat(position1.getX()).isEqualTo((float) C0_0.getX());
    assertThat(position1.getY()).isEqualTo((float) C0_0.getY());
    assertThat(position1.getZ()).isEqualTo(WorldZIndex.TERRAIN);
    Frame frame1 = entity1.getComponent(Frame.class);
    assertThat(frame1).isNotNull();
    assertThat(frame1.texture()).hasValue(texture1);
    Entity entity2 = entities.get(1);
    Position position2 = entity2.getComponent(Position.class);
    assertThat(position2).isNotNull();
    assertThat(position2.getX()).isEqualTo((float) C0_1.getX());
    assertThat(position2.getY()).isEqualTo((float) C0_1.getY());
    assertThat(position2.getZ()).isEqualTo(WorldZIndex.TERRAIN);
    Frame frame2 = entity2.getComponent(Frame.class);
    assertThat(frame2).isNotNull();
    assertThat(frame2.texture()).hasValue(texture2);
  }
}