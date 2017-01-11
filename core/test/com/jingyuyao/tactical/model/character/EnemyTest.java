package com.jingyuyao.tactical.model.character;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.event.InstantMove;
import com.jingyuyao.tactical.model.character.event.Move;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.map.Targets;
import com.jingyuyao.tactical.model.map.TargetsFactory;
import com.jingyuyao.tactical.model.mark.Marker;
import com.jingyuyao.tactical.model.mark.Marking;
import com.jingyuyao.tactical.model.mark.MarkingFactory;
import com.jingyuyao.tactical.model.state.MapState;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EnemyTest {

  private static final Coordinate COORDINATE = new Coordinate(0, 0);
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
  private MarkingFactory markingFactory;
  @Mock
  private Targets targets;
  @Mock
  private Marking marking;
  @Mock
  private MapState mapState;
  @Mock
  private Move move;
  @Mock
  private InstantMove instantMove;
  @Mock
  private RemoveCharacter removeCharacter;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private List<Marker> markers;
  private Enemy enemy;

  @Before
  public void setUp() {
    markers = new ArrayList<Marker>();
    enemy =
        new Enemy(
            eventBus, COORDINATE, markers, NAME, stats, items, targetsFactory, markingFactory);
    verify(eventBus).register(enemy);
  }

  @Test
  public void dispose() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(markingFactory.danger(targets)).thenReturn(marking);

    enemy.toggleDangerArea();
    enemy.dispose();

    verify(marking).clear();
    verify(eventBus).unregister(enemy);
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
  public void moved() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(markingFactory.danger(targets)).thenReturn(marking);
    enemy.toggleDangerArea();

    enemy.moved(move);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void instant_move() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(markingFactory.danger(targets)).thenReturn(marking);
    enemy.toggleDangerArea();

    enemy.instantMoved(instantMove);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void character_removed() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(markingFactory.danger(targets)).thenReturn(marking);
    enemy.toggleDangerArea();

    enemy.characterRemoved(removeCharacter);

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void name() {
    assertThat(enemy.getName()).isEqualTo(NAME);
  }

  @Test
  public void markers() {
    enemy.addMarker(Marker.DANGER);

    assertThat(markers).containsExactly(Marker.DANGER);

    enemy.removeMarker(Marker.DANGER);

    assertThat(markers).isEmpty();
  }

  @Test
  public void toggle_danger_area() {
    when(targetsFactory.create(enemy)).thenReturn(targets);
    when(markingFactory.danger(targets)).thenReturn(marking);

    enemy.toggleDangerArea();
    enemy.toggleDangerArea();
    enemy.toggleDangerArea();

    InOrder inOrder = Mockito.inOrder(marking);
    inOrder.verify(marking).apply();
    inOrder.verify(marking).clear();
    inOrder.verify(marking).apply();
  }

  @Test
  public void subscribers() {
    TestHelpers.verifyNoDeadEvents(enemy, move, instantMove, removeCharacter);
  }
}