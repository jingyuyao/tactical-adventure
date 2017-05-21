package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SaveManagerTest {

  private static final String MAIN = "main.save.json";
  private static final String START = "start.json";

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
  @Mock
  private Reader reader;
  @Mock
  private Writer writer;

  private SaveManager saveManager;

  @Before
  public void setUp() {
    saveManager = new SaveManager(dataConfig, myGson, files);
  }

  @Test
  public void load_main_save() throws IOException {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.reader()).thenReturn(reader);
    when(myGson.fromJson(reader, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
    verify(reader).close();
  }

  @Test
  public void load_init_save() throws IOException {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(dataConfig.getInitFileName()).thenReturn(START);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(files.internal(START)).thenReturn(fileHandle2);
    when(fileHandle1.exists()).thenReturn(false);
    when(fileHandle1.writer(false)).thenReturn(writer);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.reader()).thenReturn(reader);
    when(myGson.fromJson(reader, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
    verify(reader).close();
    verify(myGson).toJson(gameSave, writer);
    verify(writer).close();
  }

  @Test
  public void load_level_save() throws IOException {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.reader()).thenReturn(reader);
    when(myGson.fromJson(reader, LevelSave.class)).thenReturn(levelSave);

    assertThat(saveManager.loadLevelSave()).hasValue(levelSave);
    verify(reader).close();
  }

  @Test
  public void load_level_save_not_found() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(false);

    assertThat(saveManager.loadLevelSave()).isAbsent();
  }

  @Test
  public void save_game() throws IOException {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.writer(false)).thenReturn(writer);

    saveManager.save(gameSave);

    verify(myGson).toJson(gameSave, writer);
    verify(writer).close();
  }

  @Test
  public void save_level() throws IOException {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.writer(false)).thenReturn(writer);

    saveManager.save(levelSave);

    verify(myGson).toJson(levelSave, writer);
    verify(writer).close();
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