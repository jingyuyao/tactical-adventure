package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemModuleTest {

  @Bind
  @Mock
  private EventBus eventBus;
  @Bind
  @Mock
  private Characters characters;
  @Bind
  @Mock
  private Terrains terrains;

  @Inject
  private ItemFactory itemFactory;
  @Inject
  private TargetFactory targetFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new ItemModule()).injectMembers(this);
  }

  @Test
  public void item_factory() {
    itemFactory.createHeal("hello", 2);
    itemFactory.createMelee("axe", 1, 3);
    itemFactory.createPiercingLaser("lazor", 10, 5);
  }
}