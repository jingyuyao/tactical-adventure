package com.jingyuyao.tactical.data;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.jingyuyao.tactical.MockGameModule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataModuleTest {

  @Inject
  private DataManager dataManager;
  @Inject
  private GameSaveManager gameSaveManager;
  @Inject
  private LevelInitLoader levelInitLoader;
  @Inject
  private LevelTerrainsLoader levelTerrainsLoader;
  @Inject
  private LevelProgressManager levelProgressManager;
  @Inject
  private TextLoader textLoader;

  @Test
  public void can_create_module() {
    Guice.createInjector(new MockGameModule(), new DataModule()).injectMembers(this);
  }
}