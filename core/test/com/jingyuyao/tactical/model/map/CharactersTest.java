package com.jingyuyao.tactical.model.map;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.times;
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
import java.util.LinkedHashSet;
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
  private Player player;
  @Mock
  private Enemy enemy;
  @Captor
  private ArgumentCaptor<Object> argumentCaptor;

  private Set<Character> characterSet;
  private Characters characters;

  @Before
  public void setUp() {
    characterSet = new LinkedHashSet<>(); // preserves insertion order for testing
    characters = new Characters(eventBus, characterSet);
  }

  @Test
  public void add_player() {
    characters.add(player);

    assertThat(characterSet).containsExactly(player);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, AddPlayer.class);
  }

  @Test
  public void add_enemy() {
    characters.add(enemy);

    assertThat(characterSet).containsExactly(enemy);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, enemy, AddEnemy.class);
  }

  @Test
  public void remove_dead() {
    when(player.getHp()).thenReturn(0);
    when(enemy.getHp()).thenReturn(100);
    characterSet.add(player);
    characterSet.add(enemy);

    characters.removeDead();

    assertThat(characterSet).containsExactly(enemy);
    verify(eventBus).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, RemoveObject.class);
  }

  @Test
  public void reset() {
    characterSet.add(player);
    characterSet.add(enemy);

    characters.reset();

    verify(eventBus, times(2)).post(argumentCaptor.capture());
    TestHelpers.verifyObjectEvent(argumentCaptor, 0, player, RemoveObject.class);
    TestHelpers.verifyObjectEvent(argumentCaptor, 1, enemy, RemoveObject.class);
  }
}