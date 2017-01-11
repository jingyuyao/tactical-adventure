package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.MapObject.InitialMarkers;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain.Type;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import java.util.Collections;
import java.util.List;
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
  private TargetsFactory targetsFactory;
  @Bind
  @Mock
  private MarkingFactory markingFactory;
  @Bind
  @Mock
  @InitialMarkers
  private List<Marker> initialMarkers;

  @Inject
  private CharacterFactory characterFactory;

  @Before
  public void setUp() {
    Guice.createInjector(BoundFieldModule.of(this), new CharacterModule()).injectMembers(this);
  }

  @Test
  public void create_characters() {
    Items items =
        characterFactory.createItems(
            Collections.<Weapon>emptyList(), Collections.<Consumable>emptyList());
    characterFactory.createPlayer(
        new Coordinate(0, 0),
        "yolo",
        new Stats(1, 1, Collections.<Type>emptySet()),
        items);
    characterFactory.createEnemy(
        new Coordinate(0, 0),
        "yolo",
        new Stats(1, 1, Collections.<Type>emptySet()),
        items);
  }
}