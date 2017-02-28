package com.jingyuyao.tactical.view.actor;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.google.inject.Guice;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.view.actor.ActorModule.InitialMarkers;
import com.jingyuyao.tactical.view.resource.LoopAnimation;
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
  private LoopAnimation loopAnimation;

  @Inject
  private ActorFactory actorFactory;
  @Inject
  @InitialMarkers
  private LinkedHashSet<Sprite> markers1;
  @Inject
  @InitialMarkers
  private LinkedHashSet<Sprite> markers2;

  @Before
  public void setUp() {
    Guice.createInjector(new ActorModule()).injectMembers(this);
  }

  @Test
  public void actor_factory() {
    when(terrain.getCoordinate()).thenReturn(COORDINATE);
    when(player.getCoordinate()).thenReturn(COORDINATE);
    when(enemy.getCoordinate()).thenReturn(COORDINATE);

    actorFactory.create(terrain);
    actorFactory.create(enemy, loopAnimation);
    actorFactory.create(player, loopAnimation);
  }

  @Test
  public void initial_markers() {
    assertThat(markers1).isNotSameAs(markers2);
  }
}