package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SaveManagerTest {

  private static final String MAIN = "main.save.json";
  private static final String START = "start.json";
  private static final String DATA = "hello world!";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private GameSave gameSave;
  @Mock
  private LevelSave levelSave;
  @Mock
  private FileHandle fileHandle1;
  @Mock
  private FileHandle fileHandle2;

  private SaveManager saveManager;

  @Before
  public void setUp() {
    saveManager = new SaveManager(dataConfig, myGson, files);
  }

  @Test
  public void load_init_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
  }

  @Test
  public void load_main_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(dataConfig.getInitFileName()).thenReturn(START);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(files.internal(START)).thenReturn(fileHandle2);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
  }

  @Test
  public void load_level_save() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, LevelSave.class)).thenReturn(levelSave);

    assertThat(saveManager.loadLevelSave()).hasValue(levelSave);
  }

  @Test
  public void load_level_save_not_found() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(false);

    assertThat(saveManager.loadLevelSave()).isAbsent();
  }

  @Test
  public void save_game() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(myGson.toJson(gameSave)).thenReturn(DATA);

    saveManager.save(gameSave);

    verify(fileHandle1).writeString(DATA, false);
  }

  @Test
  public void save_level() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(myGson.toJson(levelSave)).thenReturn(DATA);

    saveManager.save(levelSave);

    verify(fileHandle1).writeString(DATA, false);
  }

  @Test
  public void remove_game_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);

    saveManager.removeGameSave();

    verify(fileHandle1).delete();
  }

  @Test
  public void remove_level_save() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);

    saveManager.removeLevelSave();

    verify(fileHandle1).delete();
  }
}