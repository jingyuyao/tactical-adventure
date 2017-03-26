package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.character.PassiveEnemy;
import com.jingyuyao.tactical.model.item.DirectionalWeapon;
import com.jingyuyao.tactical.model.item.Grenade;
import com.jingyuyao.tactical.model.state.Waiting;
import com.jingyuyao.tactical.model.world.CellFactory;
import com.jingyuyao.tactical.model.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataModuleTest {

  @Bind
  @Mock
  private Model model;
  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private CellFactory cellFactory;
  @Bind
  @Mock
  private Provider<Waiting> waitingProvider;
  @Bind
  @Mock
  private Provider<PassiveEnemy> passiveEnemyProvider;
  @Bind
  @Mock
  private Provider<DirectionalWeapon> directionalWeaponProvider;
  @Bind
  @Mock
  private Provider<Grenade> grenadeProvider;
  @Bind
  @Mock
  private AssetManager assetManager;
  @Bind
  @Mock
  private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;
  @Mock
  private Files files;

  @Inject
  private ModelManager modelManager;
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