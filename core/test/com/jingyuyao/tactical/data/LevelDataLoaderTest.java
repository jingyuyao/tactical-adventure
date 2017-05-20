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
public class LevelDataLoaderTest {

  private static final String LEVEL_DIR = "levels/1/";
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
  private LevelWorld levelWorld;
  @Mock
  private LevelScript levelScript;

  private LevelDataLoader levelDataLoader;

  @Before
  public void setUp() {
    levelDataLoader = new LevelDataLoader(dataConfig, myGson, files);
  }

  @Test
  public void has_level() {
    when(dataConfig.getLevelDir(2)).thenReturn(LEVEL_DIR);
    when(files.internal(LEVEL_DIR)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);

    assertThat(levelDataLoader.hasLevel(2)).isTrue();
  }

  @Test
  public void load_init() {
    when(dataConfig.getLevelWorldFileName(2)).thenReturn(LEVEL);
    when(files.internal(LEVEL)).thenReturn(fileHandle);
    when(fileHandle.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, LevelWorld.class)).thenReturn(levelWorld);

    assertThat(levelDataLoader.loadWorld(2)).isSameAs(levelWorld);
  }

  @Test
  public void load_script() {
    when(dataConfig.getLevelScriptFileName(2)).thenReturn(LEVEL);
    when(files.internal(LEVEL)).thenReturn(fileHandle);
    when(fileHandle.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, LevelScript.class)).thenReturn(levelScript);

    assertThat(levelDataLoader.loadScript(2)).isSameAs(levelScript);
  }
}