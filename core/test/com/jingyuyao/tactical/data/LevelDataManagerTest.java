package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LevelDataManagerTest {

  private static final String LEVEL = "levels/1.level.json";
  private static final String DATA = "goodbye world!";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private LevelData levelData;

  private LevelDataManager levelDataManager;

  @Before
  public void setUp() {
    levelDataManager = new LevelDataManager(dataConfig, myGson, files);
  }

  @Test
  public void load() {
    when(dataConfig.getLevelDataFileName(2)).thenReturn(LEVEL);
    when(files.internal(LEVEL)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);
    when(fileHandle.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, LevelData.class)).thenReturn(levelData);

    assertThat(levelDataManager.load(2)).isSameAs(levelData);
  }

  @Test
  public void has_level() {
    when(dataConfig.getLevelDataFileName(2)).thenReturn(LEVEL);
    when(files.internal(LEVEL)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);

    assertThat(levelDataManager.hasLevel(2)).isTrue();
  }
}