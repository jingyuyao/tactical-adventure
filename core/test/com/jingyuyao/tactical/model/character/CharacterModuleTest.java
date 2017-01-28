package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Multiset;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.TerrainGraphs;
import java.util.Collections;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterModuleTest {

  @Bind
  @Mock
  private TerrainGraphs terrainGraphs;
  @Bind
  @Mock
  private Characters characters;
  @Bind
  @Mock
  @InitialMarkers
  private Multiset<Marker> markers;
  @Bind
  @Mock
  private MovementFactory movementFactory;

  @Inject
  private CharacterFactory characterFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }

  @Test
  public void create_characters() {
    characterFactory.create(new BasePlayerData(), Collections.<Item>emptyList());
    characterFactory.create(new PassiveEnemyData(), Collections.<Item>emptyList());
  }
}