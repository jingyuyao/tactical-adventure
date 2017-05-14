package com.jingyuyao.tactical.model.state;

import static com.google.common.truth.Truth.assertThat;

import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TurnTest {

  @Test
  public void initial_turn() {
    Turn turn = new Turn();

    assertThat(turn.getNumber()).isEqualTo(1);
    assertThat(turn.getStage()).isSameAs(TurnStage.START);
  }

  @Test
  public void advance_start() {
    Turn turn = new Turn(2, TurnStage.START);

    turn.advance();

    assertThat(turn.getNumber()).isEqualTo(2);
    assertThat(turn.getStage()).isSameAs(TurnStage.PLAYER);
  }

  @Test
  public void advance_player() {
    Turn turn = new Turn(2, TurnStage.PLAYER);

    turn.advance();

    assertThat(turn.getNumber()).isEqualTo(2);
    assertThat(turn.getStage()).isSameAs(TurnStage.END);
  }

  @Test
  public void advance_end() {
    Turn turn = new Turn(2, TurnStage.END);

    turn.advance();

    assertThat(turn.getNumber()).isEqualTo(2);
    assertThat(turn.getStage()).isSameAs(TurnStage.ENEMY);
  }

  @Test
  public void advance_enemy() {
    Turn turn = new Turn(2, TurnStage.ENEMY);

    turn.advance();

    assertThat(turn.getNumber()).isEqualTo(3);
    assertThat(turn.getStage()).isSameAs(TurnStage.START);
  }

  @Test
  public void equals_and_hash() {
    Turn turn1 = new Turn(3, TurnStage.PLAYER);
    Turn turn2 = new Turn(3, TurnStage.PLAYER);
    Turn turn3 = new Turn(4, TurnStage.START);

    assertThat(turn1).isEqualTo(turn2);
    assertThat(turn1.hashCode()).isEqualTo(turn2.hashCode());
    assertThat(turn1).isNotEqualTo(turn3);
    assertThat(turn1.hashCode()).isNotEqualTo(turn3.hashCode());
  }
}