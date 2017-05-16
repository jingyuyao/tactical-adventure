package com.jingyuyao.tactical.data;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.ship.PassiveEnemy;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataModuleTest {

  @Bind
  @Mock
  private Provider<PassiveEnemy> passiveEnemyProvider;
  @Bind
  @Mock
  private Provider<DirectionalWeapon> directionalWeaponProvider;
  @Bind
  @Mock
  private Provider<Grenade> grenadeProvider;

  @Inject
  private DataManager dataManager;
  @Inject
  private GameSaveManager gameSaveManager;
  @Inject
  private LevelDataManager levelDataManager;
  @Inject
  private LevelMapManager levelMapManager;
  @Inject
  private LevelProgressManager levelProgressManager;
  @Inject
  private MessageLoader messageLoader;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new DataModule()).injectMembers(this);
  }
}