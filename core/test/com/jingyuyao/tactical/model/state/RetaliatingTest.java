package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.character.Enemy;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.map.Characters;
import com.jingyuyao.tactical.model.map.Terrain;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RetaliatingTest {

  @Mock
  private MapState mapState;
  @Mock
  private StateFactory stateFactory;
  @Mock
  private Characters characters;
  @Mock
  private Player player;
  @Mock
  private Enemy enemy;
  @Mock
  private Enemy enemy2;
  @Mock
  private Terrain terrain;
  @Mock
  private Waiting waiting;

  private ListenableFuture<Void> retaliation;
  private ListenableFuture<Void> retaliation2;
  private ImmutableList<Character> characterList;
  private Retaliating retaliating;

  @Before
  public void setUp() {
    retaliation = Futures.immediateFuture(null);
    retaliation2 = Futures.immediateFuture(null);
    characterList = ImmutableList.of(player, enemy, enemy2);
    retaliating = new Retaliating(mapState, stateFactory, characters);
  }

  @Test
  public void selects() {
    retaliating.select(player);
    retaliating.select(enemy);
    retaliating.select(terrain);

    verifyZeroInteractions(mapState);
  }

  @Test
  public void actions() {
    assertThat(retaliating.getActions()).isEmpty();
  }

  @Test
  public void enter() {
    when(characters.iterator()).thenReturn(characterList.iterator());
    when(enemy.retaliate()).thenReturn(retaliation);
    when(enemy2.retaliate()).thenReturn(retaliation2);
    when(stateFactory.createWaiting()).thenReturn(waiting);

    retaliating.enter();

    InOrder inOrder = Mockito.inOrder(enemy, enemy2, mapState);
    inOrder.verify(enemy).retaliate();
    inOrder.verify(enemy2).retaliate();
    inOrder.verify(mapState).newStack(waiting);
  }
}