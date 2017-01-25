package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import java.util.Iterator;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharactersTest {

  private static final Coordinate COORDINATE1 = new Coordinate(0, 1);
  private static final Coordinate COORDINATE2 = new Coordinate(0, 2);

  @Mock
  private EventBus eventBus;
  @Mock
  private Set<Character> characterSet;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Iterator<Character> characterIterator;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Characters characters;

  @Before
  public void setUp() {
    characters = new Characters(eventBus, characterSet);
  }

  @Test
  public void add_player() {
    characters.add(player);

    verify(characterSet).add(player);
    verify(eventBus).post(argumentCaptor.capture());
    assertThat(TestHelpers.isInstanceOf(argumentCaptor.getValue(), AddPlayer.class).getObject())
        .isSameAs(player);
  }

  @Test
  public void add_enemy() {
    characters.add(enemy);

    verify(characterSet).add(enemy);
    verify(eventBus).post(argumentCaptor.capture());
    assertThat(TestHelpers.isInstanceOf(argumentCaptor.getValue(), AddEnemy.class).getObject())
        .isSameAs(enemy);
  }

  @Test
  public void remove_character() {
    characters.removeCharacter(player);

    verify(characterSet).remove(player);
  }

  @Test
  public void coordinates() {
    when(characterSet.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, true, false);
    when(characterIterator.next()).thenReturn(player, enemy);
    when(player.getCoordinate()).thenReturn(COORDINATE1);
    when(enemy.getCoordinate()).thenReturn(COORDINATE2);

    assertThat(characters.coordinates()).containsExactly(COORDINATE1, COORDINATE2);
  }

  @Test
  public void iterator() {
    when(characterSet.iterator()).thenReturn(characterIterator);

    assertThat(characters.iterator()).isSameAs(characterIterator);
  }
}