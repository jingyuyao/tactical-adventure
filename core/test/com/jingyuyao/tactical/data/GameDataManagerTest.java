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
public class GameDataManagerTest {

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
  private GameData gameData;
  @Mock
  private FileHandle fileHandle1;
  @Mock
  private FileHandle fileHandle2;

  private GameDataManager gameDataManager;

  @Before
  public void setUp() {
    gameDataManager = new GameDataManager(dataConfig, myGson, files);
  }

  @Test
  public void load_init() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);
    when(fileHandle1.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameData.class)).thenReturn(gameData);

    assertThat(gameDataManager.loadData()).isSameAs(gameData);
  }

  @Test
  public void load_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(dataConfig.getInitFileName()).thenReturn(START);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(files.internal(START)).thenReturn(fileHandle2);
    when(fileHandle2.exists()).thenReturn(true);
    when(fileHandle2.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, GameData.class)).thenReturn(gameData);

    assertThat(gameDataManager.loadData()).isSameAs(gameData);
  }

  @Test
  public void save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(myGson.toJson(gameData)).thenReturn(DATA);

    gameDataManager.saveData(gameData);

    verify(fileHandle1).writeString(DATA, false);
  }

  @Test
  public void remove_save() {
    when(dataConfig.getMainSaveFileName()).thenReturn(MAIN);
    when(files.local(MAIN)).thenReturn(fileHandle1);
    when(fileHandle1.exists()).thenReturn(true);

    gameDataManager.removeSavedData();

    verify(fileHandle1).delete();
  }
}