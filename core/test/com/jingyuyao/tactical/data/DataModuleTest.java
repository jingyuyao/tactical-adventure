package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataModuleTest {

  @Bind
  @Mock
  private AssetManager assetManager;
  @Bind
  @Mock
  private Provider<PassiveEnemy> passiveEnemyProvider;
  @Bind
  @Mock
  private Provider<DirectionalWeapon> directionalWeaponProvider;
  @Bind
  @Mock
  private Provider<Grenade> grenadeProvider;
  @Mock
  private Files files;

  @Inject
  private GameSaveManager gameSaveManager;
  @Inject
  private LevelDataManager levelDataManager;
  @Inject
  private LevelMapManager levelMapManager;

  @Before
  public void setUp() {
    Gdx.files = files;
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new DataModule()).injectMembers(this);
  }
}