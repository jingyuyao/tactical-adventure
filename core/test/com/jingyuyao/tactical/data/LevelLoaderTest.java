package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_0;
import static com.jingyuyao.tactical.model.world.CoordinateTest.C0_1;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.ship.Ship;
import com.jingyuyao.tactical.model.state.Turn;
import java.io.Reader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LevelLoaderTest {

  private static final String LEVEL_DIR = "levels/1/";
  private static final String LEVEL_WORLD = "levels/1/world";
  private static final String LEVEL_SCRIPT = "levels/1/script";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private DataSerializer dataSerializer;
  @Mock
  private Files files;
  @Mock
  private DialogueLoader dialogueLoader;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private FileHandle fileHandle2;
  @Mock
  private FileHandle fileHandle3;
  @Mock
  private Reader reader1;
  @Mock
  private Reader reader2;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelWorld levelWorld;
  @Mock
  private LevelScript levelScript;
  @Mock
  private Ship ship1;
  @Mock
  private Ship ship2;
  @Mock
  private Ship ship3;
  @Mock
  private Condition condition1;
  @Mock
  private Condition condition2;
  @Mock
  private Condition condition3;
  @Mock
  private Condition condition4;
  @Mock
  private ActivateGroup activateGroup1;
  @Mock
  private Dialogue dialogue;

  private LevelLoader levelLoader;

  @Before
  public void setUp() {
    levelLoader = new LevelLoader(dataConfig, dataSerializer, files, dialogueLoader);
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
    when(dataConfig.getLevelScriptFileName(2)).thenReturn(LEVEL_SCRIPT);
    when(files.internal(LEVEL_DIR)).thenReturn(fileHandle);
    when(files.internal(LEVEL_WORLD)).thenReturn(fileHandle2);
    when(files.internal(LEVEL_SCRIPT)).thenReturn(fileHandle3);
    when(fileHandle.exists()).thenReturn(true);
    when(fileHandle2.reader()).thenReturn(reader1);
    when(fileHandle3.reader()).thenReturn(reader2);
    when(dataSerializer.deserialize(reader1, LevelWorld.class)).thenReturn(levelWorld);
    when(dataSerializer.deserialize(reader2, LevelScript.class)).thenReturn(levelScript);
    when(levelWorld.getPlayerSpawns()).thenReturn(ImmutableList.of(C0_0));
    when(levelWorld.getActiveShips()).thenReturn(ImmutableMap.of(C0_1, ship1));
    when(gameSave.getPlayerShips()).thenReturn(ImmutableList.of(ship2, ship3));
    when(levelScript.getWinConditions()).thenReturn(ImmutableList.of(condition1));
    when(levelScript.getLoseConditions()).thenReturn(ImmutableList.of(condition2));
    when(levelScript.getGroupActivations())
        .thenReturn(ImmutableMap.of(condition4, activateGroup1));
    when(dialogueLoader.getDialogues(2)).thenReturn(ImmutableListMultimap.of(condition3, dialogue));

    LevelSave levelSave = levelLoader.createNewSave(2, gameSave);

    assertThat(levelSave.getActiveShips()).containsExactly(C0_0, ship2, C0_1, ship1);
    assertThat(levelSave.getInactiveShips()).containsExactly(ship3);
    assertThat(levelSave.getTurn()).isEqualTo(new Turn());
    assertThat(levelSave.getScript().getWinConditions()).containsExactly(condition1);
    assertThat(levelSave.getScript().getLoseConditions()).containsExactly(condition2);
    assertThat(levelSave.getScript().getGroupActivations())
        .containsExactly(condition4, activateGroup1);
    assertThat(levelSave.getScript().getDialogues()).containsEntry(condition3, dialogue);
  }
}