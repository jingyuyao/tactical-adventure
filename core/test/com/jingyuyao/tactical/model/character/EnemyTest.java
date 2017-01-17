package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Path;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.map.Terrain;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.retaliation.Retaliation;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnemyTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
  private static final Coordinate COORDINATE2 = new Coordinate(0, 1);
  private static final String NAME = "me";

  @Mock
  private EventBus eventBus;
  @Mock
  private Stats stats;
  @Mock
  private Items items;
  @Mock
  private TargetsFactory targetsFactory;
  @Mock
  private MapState mapState;
  @Mock
  private Move move;
  @Mock
  private InstantMove instantMove;
  @Mock
  private RemoveCharacter removeCharacter;
  @Mock
  private Terrain terrain;
  @Mock
  private Player player;
  @Mock
  private Weapon weapon;
  @Mock
  private Path path;
  @Mock
  private Retaliation retaliation;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private List<Marker> markers;
  private Enemy enemy;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    enemy =
        new Enemy(
            eventBus, COORDINATE, markers, NAME, stats, items, targetsFactory, retaliation);
  }

  @Test
  public void select() {
    enemy.select(mapState);

    verify(mapState).select(enemy);
  }

  @Test
  public void highlight() {
    enemy.highlight(mapState);

    verify(mapState).highlight(enemy);
  }

  @Test
  public void move() {
    when(path.getDestination()).thenReturn(COORDINATE2);

    ListenableFuture<Void> future = enemy.move(path);

    verify(eventBus).post(argumentCaptor.capture());
    Move move = TestHelpers.isInstanceOf(argumentCaptor.getValue(), Move.class);
    assertThat(move.getPath()).isSameAs(path);
    assertThat(future.isDone()).isFalse();

    move.done();
    assertThat(future.isDone()).isTrue();
  }

  @Test
  public void name() {
    assertThat(enemy.getName()).isEqualTo(NAME);
  }

  @Test
  public void retaliation() {
    enemy.retaliate();

    verify(retaliation).run(enemy);
  }

  @Test
  public void markers() {
    enemy.addMarker(Marker.DANGER);

    assertThat(markers).containsExactly(Marker.DANGER);

    enemy.removeMarker(Marker.DANGER);

    assertThat(markers).isEmpty();
  }
}