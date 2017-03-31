package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GameSaveManagerTest {

  private static final String MAIN_SAVE = "main.save.json";
  private static final String START_SAVE = "main.json";
  private static final String DATA = "hello world!";
  private static final int LEVEL_ID = 10;

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private GameSave gameSave;
  @Mock
  private FileHandle fileHandle1;
  @Mock
  private FileHandle fileHandle2;
  @Mock
  private LevelData levelData;
  @Mock
  private Enemy enemy1;
  @Mock
  private Enemy enemy2;
  @Mock
  private Enemy enemy3;
  @Mock
  private Enemy cEnemy1;
  @Mock
  private Enemy cEnemy2;
  @Mock
  private Player player1;
  @Mock
  private Player player2;
  @Mock
  private Player player3;
  @Mock
  private Player cPlayer1;
  @Mock
  private Player cPlayer2;

  private GameSaveManager gameSaveManager;

  @Before
  public void setUp() {
    gameSaveManager = new GameSaveManager(dataConfig, myGson, files);
  }

  @Test
  public void load_main_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(gameSaveManager.load()).isSameAs(gameSave);
  }

  @Test
  public void load_start_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(dataConfig.getStartSaveFileName()).thenReturn(START_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(files.local(START_SAVE)).thenReturn(fileHandle2);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameSave.class)).thenReturn(gameSave);

    assertThat(gameSaveManager.load()).isSameAs(gameSave);
  }

  @Test
  public void save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN_SAVE);
    when(files.local(MAIN_SAVE)).thenReturn(fileHandle1);
    when(myGson.toJson(gameSave)).thenReturn(DATA);

    gameSaveManager.save(gameSave);

    verify(fileHandle1).writeString(DATA, false);
  }
}