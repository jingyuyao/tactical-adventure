package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
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
  private Files files;
  @Mock
  private InitLoader initLoader;
  @Mock
  private KryoSerializer kryoSerializer;
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
  private InputStream inputStream;
  @Mock
  private OutputStream outputStream;

  private SaveManager saveManager;

  @Before
  public void setUp() {
    saveManager = new SaveManager(dataConfig, files, initLoader, kryoSerializer);
  }

  @Test
  public void load_main_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.read()).thenReturn(inputStream);
    when(kryoSerializer.deserialize(inputStream, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
  }

  @Test
  public void load_init_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(dataConfig.getInitFileName()).thenReturn(START);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(files.internal(START)).thenReturn(fileHandle2);
    when(fileHandle1.exists()).thenReturn(false);
    when(fileHandle1.write(false)).thenReturn(outputStream);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.reader()).thenReturn(reader);
    when(initLoader.fromHocon(reader, GameSave.class)).thenReturn(gameSave);

    assertThat(saveManager.loadGameSave()).isSameAs(gameSave);
    verify(kryoSerializer).serialize(gameSave, outputStream);
  }

  @Test
  public void load_level_save() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.read()).thenReturn(inputStream);
    when(kryoSerializer.deserialize(inputStream, LevelSave.class)).thenReturn(levelSave);

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
    when(fileHandle1.write(false)).thenReturn(outputStream);

    saveManager.save(gameSave);

    verify(kryoSerializer).serialize(gameSave, outputStream);
  }

  @Test
  public void save_level() {
    when(dataConfig.getMainLevelSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.write(false)).thenReturn(outputStream);

    saveManager.save(levelSave);

    verify(kryoSerializer).serialize(levelSave, outputStream);
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