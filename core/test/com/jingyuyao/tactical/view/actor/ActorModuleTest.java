package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;

import com.google.inject.Guice;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Cell;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.LinkedHashSet;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ActorModuleTest {

  private static final Coordinate COORDINATE = new Coordinate(1, 1);

  @Mock
  private Terrain terrain;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Cell cell;
  @Mock
  private LoopAnimation loopAnimation;

  @Inject
  private ActorFactory actorFactory;
  @Inject
  @InitialMarkers
  private LinkedHashSet<WorldTexture> markers1;
  @Inject
  @InitialMarkers
  private LinkedHashSet<WorldTexture> markers2;

  @Before
  public void setUp() {
    Guice.createInjector(new ActorModule()).injectMembers(this);
  }

  @Test
  public void actor_factory() {
    actorFactory.create(cell, COORDINATE);
    actorFactory.create(terrain, COORDINATE);
    actorFactory.create(enemy, COORDINATE, loopAnimation);
    actorFactory.create(player, COORDINATE, loopAnimation);
  }

  @Test
  public void initial_markers() {
    assertThat(markers1).isNotSameAs(markers2);
  }
}