package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class TargetPanel extends TextPanel<Battling> {

  private static final String FMT = "%s\nHP: %d\nDmg: %d\n\n";

  TargetPanel() {
    getLabel().setAlignment(Align.left);
  }

  @Override
  String createText(Battling battling) {
    StringBuilder builder = new StringBuilder("Targets:\n");
    int damage = battling.getWeapon().getAttackPower();
    for (Cell cell : battling.getTarget().getTargetCells()) {
      if (cell.hasCharacter()) {
        Character character = cell.getCharacter();
        builder.append(
            String.format(Locale.US, FMT, character.getName(), character.getHp(), damage));
      }
    }
    return builder.toString();
  }

  @Subscribe
  void battling(Battling battling) {
    display(battling);
  }

  @Subscribe
  void exitState(ExitState exitState) {
    clearDisplay();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    clearDisplay();
  }
}