package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.TestHelpers;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.event.AddEnemy;
import com.jingyuyao.tactical.model.event.AddPlayer;
import com.jingyuyao.tactical.model.event.RemoveObject;
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
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, AddPlayer.class);
  }

  @Test
  public void add_enemy() {
    characters.add(enemy);

    verify(characterSet).add(enemy);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, enemy, AddEnemy.class);
  }

  @Test
  public void remove_character() {
    characters.remove(player);

    verify(characterSet).remove(player);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, RemoveObject.class);
  }

  @Test
  public void fluent() {
    when(characterSet.iterator()).thenReturn(characterIterator);

    assertThat(characters.fluent().iterator()).isSameAs(characterIterator);
  }
}