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
public class LevelLoaderTest {

  private static final String LEVEL_DIR = "levels/1/";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private DialogueLoader dialogueLoader;
  @Mock
  private FileHandle fileHandle;

  private LevelLoader levelLoader;

  @Before
  public void setUp() {
    levelLoader = new LevelLoader(dataConfig, myGson, files, dialogueLoader);
  }

  @Test
  public void has_level() {
    when(dataConfig.getLevelDir(2)).thenReturn(LEVEL_DIR);
    when(files.internal(LEVEL_DIR)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);

    assertThat(levelLoader.hasLevel(2)).isTrue();
  }
}