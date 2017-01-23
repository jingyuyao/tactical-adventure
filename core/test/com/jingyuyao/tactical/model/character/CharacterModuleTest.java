package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.character.CharacterModule.DefaultRetaliation;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.retaliation.PassiveRetaliation;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
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
  private EventBus eventBus;
  @Bind
  @Mock
  private MovementFactory movementFactory;

  @Inject
  private CharacterFactory characterFactory;
  @Inject
  @DefaultRetaliation
  private Retaliation defaultRetaliation;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }

  @Test
  public void create_characters() {
    characterFactory.createPlayer(
        new Coordinate(0, 0),
        new Stats("yolo", 1, 1, Collections.<Type>emptySet()),
        Collections.<Item>emptyList());
    characterFactory.createPassiveEnemy(
        new Coordinate(0, 0),
        new Stats("holo", 1, 1, Collections.<Type>emptySet()),
        Collections.<Item>emptyList());
  }

  @Test
  public void default_retaliation() {
    assertThat(defaultRetaliation).isInstanceOf(PassiveRetaliation.class);
  }
}