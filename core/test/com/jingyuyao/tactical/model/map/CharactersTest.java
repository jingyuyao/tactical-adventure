package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.character.event.RemoveCharacter;
import com.jingyuyao.tactical.model.event.ClearMap;
import com.jingyuyao.tactical.model.event.NewMap;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharactersTest {

  @Mock
  private EventBus eventBus;
  @Mock
  private Set<Character> characterSet;
  @Mock
  private NewMap newMap;
  @Mock
  private ClearMap clearMap;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Iterator<Character> characterIterator;
  @Mock
  private RemoveCharacter removeCharacter;

  // Mocking list iteration when we are abusing functional programming is too hard
  private List<Player> players;
  private List<Enemy> enemies;
  private Characters characters;

  @Before
  public void setUp() {
    characters = new Characters(eventBus, characterSet);
    verify(eventBus).register(characters);
    players = Collections.singletonList(player);
    enemies = Collections.singletonList(enemy);
  }

  @Test
  public void initialize() {
    when(newMap.getPlayers()).thenReturn(players);
    when(newMap.getEnemies()).thenReturn(enemies);

    characters.initialize(newMap);

    verify(characterSet).add(player);
    verify(characterSet).add(enemy);
    verifyNoMoreInteractions(characterSet);
  }

  @Test
  public void dispose() {
    when(characterSet.iterator()).thenReturn(characterIterator);
    when(characterIterator.hasNext()).thenReturn(true, true, false);
    when(characterIterator.next()).thenReturn(player, enemy);

    characters.dispose(clearMap);

    verify(player).dispose();
    verify(enemy).dispose();
    verify(characterSet).clear();
  }

  @Test
  public void remove_character() {
    when(removeCharacter.getObject()).thenReturn(player);

    characters.removeCharacter(removeCharacter);

    verify(characterSet).remove(player);
  }

  @Test
  public void iterator() {
    when(characterSet.iterator()).thenReturn(characterIterator);

    assertThat(characters.iterator()).isSameAs(characterIterator);
  }
}