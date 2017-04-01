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
public class LevelProgressManagerTest {

  private static final String PROGRESS = "im.sleepy.save.json";
  private static final String DATA = "i want a new phone";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private MyGson myGson;
  @Mock
  private Files files;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private LevelProgress levelProgress;

  private LevelProgressManager levelProgressManager;

  @Before
  public void setUp() {
    levelProgressManager = new LevelProgressManager(dataConfig, myGson, files);
  }

  @Test
  public void load_exist() {
    when(dataConfig.getMainLevelProgressFileName()).thenReturn(PROGRESS);
    when(files.local(PROGRESS)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);
    when(fileHandle.readString()).thenReturn(DATA);
    when(myGson.fromJson(DATA, LevelProgress.class)).thenReturn(levelProgress);

    assertThat(levelProgressManager.load()).hasValue(levelProgress);
  }

  @Test
  public void load_not_exist() {
    when(dataConfig.getMainLevelProgressFileName()).thenReturn(PROGRESS);
    when(files.local(PROGRESS)).thenReturn(fileHandle);

    assertThat(levelProgressManager.load()).isAbsent();
  }

  @Test
  public void save() {
    when(dataConfig.getMainLevelProgressFileName()).thenReturn(PROGRESS);
    when(files.local(PROGRESS)).thenReturn(fileHandle);
    when(myGson.toJson(levelProgress)).thenReturn(DATA);

    levelProgressManager.save(levelProgress);

    verify(fileHandle).writeString(DATA, false);
  }

  @Test
  public void remove_save() {
    when(dataConfig.getMainLevelProgressFileName()).thenReturn(PROGRESS);
    when(files.local(PROGRESS)).thenReturn(fileHandle);
    when(fileHandle.exists()).thenReturn(true);

    levelProgressManager.removeSave();

    verify(fileHandle).delete();
  }
}