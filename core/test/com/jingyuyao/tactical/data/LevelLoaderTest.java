package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.Terrain;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LevelLoaderTest {

  private static final String LEVEL_DIR = "levels/1/";
  private static final String LEVEL_WORLD = "levels/1/world";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private DataSerializer dataSerializer;
  @Mock
  private Files files;
  @Mock
  private TerrainsLoader terrainsLoader;
  @Mock
  private ScriptLoader scriptLoader;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private FileHandle fileHandle2;
  @Mock
  private Reader reader1;
  @Mock
  private LevelWorld levelWorld;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Ship ship3;
  @Mock
  private Terrain terrain1;
  @Mock
  private Terrain terrain2;
  @Mock
  private Script script;

  private LevelLoader levelLoader;

  @Before
  public void setUp() {
    levelLoader = new LevelLoader(dataConfig, dataSerializer, files, terrainsLoader, scriptLoader);
  }

  @Test
  public void has_level() {
    when(dataConfig.getLevelDir(2)).thenReturn(LEVEL_DIR);
    when(files.internal(LEVEL_DIR)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);

    assertThat(levelLoader.hasLevel(2)).isTrue();
  }

  @Test
  public void create_new_save() {
    when(dataConfig.getLevelDir(2)).thenReturn(LEVEL_DIR);
    when(dataConfig.getLevelWorldFileName(2)).thenReturn(LEVEL_WORLD);
    when(files.internal(LEVEL_DIR)).thenReturn(fileHandle);
    when(files.internal(LEVEL_WORLD)).thenReturn(fileHandle2);
    when(fileHandle.exists()).thenReturn(true);
    when(fileHandle2.reader()).thenReturn(reader1);
    when(dataSerializer.deserialize(reader1, LevelWorld.class)).thenReturn(levelWorld);
    when(levelWorld.getPlayerSpawns()).thenReturn(Collections.singletonList(C0_0));
    when(levelWorld.getActiveShips()).thenReturn(ImmutableMap.of(C0_1, ship1));
    when(terrainsLoader.load(2)).thenReturn(ImmutableMap.of(C0_0, terrain1, C0_1, terrain2));
    when(terrain1.canHoldShip()).thenReturn(true);
    when(terrain2.canHoldShip()).thenReturn(true);
    when(scriptLoader.load(2)).thenReturn(script);

    LevelSave levelSave = levelLoader.createNewSave(2, Arrays.asList(ship2, ship3));

    assertThat(levelSave.getLevel()).isEqualTo(2);
    assertThat(levelSave.getWorldCells()).hasSize(2);
    Cell cell1 = levelSave.getWorldCells().get(0);
    assertThat(cell1.getCoordinate()).isEqualTo(C0_0);
    assertThat(cell1.getTerrain()).isSameAs(terrain1);
    assertThat(cell1.ship()).hasValue(ship2);
    Cell cell2 = levelSave.getWorldCells().get(1);
    assertThat(cell2.getCoordinate()).isEqualTo(C0_1);
    assertThat(cell2.getTerrain()).isSameAs(terrain2);
    assertThat(cell2.ship()).hasValue(ship1);
    assertThat(levelSave.getInactiveShips()).containsExactly(ship3);
    assertThat(levelSave.getTurn()).isEqualTo(new Turn());
    assertThat(levelSave.getScript()).isSameAs(script);
  }
}