package com.jingyuyao.tactical.model.item;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
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
  @Bind
  @Mock
  private MarkingFactory markingFactory;

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
    itemFactory.createHeal(new ItemStats("pot", 5));
    itemFactory.createMelee(new WeaponStats("axe", 1, 100));
    itemFactory.createPiercingLaser(new WeaponStats("lazor", 10, 5));
  }
}