package com.jingyuyao.tactical.model.item;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.battle.BattleModule.Immediate;
import com.jingyuyao.tactical.model.battle.BattleModule.Piercing;
import com.jingyuyao.tactical.model.battle.Target;
import com.jingyuyao.tactical.model.battle.TargetFactory;
import com.jingyuyao.tactical.model.character.Character;
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
  @Piercing
  private TargetFactory piercingFactory;
  @Bind
  @Mock
  @Immediate
  private TargetFactory immediateFactory;
  @Mock
  private Character attacker;
  @Mock
  private ImmutableList<Target> targets;
  @Inject
  private ItemFactory itemFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new ItemModule()).injectMembers(this);
  }

  @Test
  public void item_factory() {
    itemFactory.createHeal("hello", 2);
    itemFactory.createLaser("nihao", 1, 1);
    itemFactory.createMelee("nihao", 1, 1);
  }

  @Test
  public void laser_create_targets() {
    Weapon laser = itemFactory.createLaser("abc", 1, 1);
    when(piercingFactory.create(attacker, laser)).thenReturn(targets);

    assertThat(laser.createTargets(attacker)).isSameAs(targets);
    verify(piercingFactory).create(attacker, laser);
  }

  @Test
  public void melee_create_targets() {
    Weapon melee = itemFactory.createMelee("abc", 1, 1);
    when(immediateFactory.create(attacker, melee)).thenReturn(targets);

    assertThat(melee.createTargets(attacker)).isSameAs(targets);
    verify(immediateFactory).create(attacker, melee);
  }
}