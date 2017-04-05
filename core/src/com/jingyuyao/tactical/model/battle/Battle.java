package com.jingyuyao.tactical.model.battle;

import com.google.common.eventbus.EventBus;
import com.jingyuyao.tactical.model.ModelModule.ModelEventBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Battle {

  private final EventBus eventBus;

  @Inject
  Battle(@ModelEventBus EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public MyFuture begin(
      final Character attacker, final Weapon weapon, final Target target) {
    MyFuture future = new MyFuture();
    eventBus.post(new Attack(target, weapon, future));
    future.addCallback(new Runnable() {
      @Override
      public void run() {
        attacker.useItem(weapon);
        weapon.damages(target);
        for (Cell cell : target.getTargetCells()) {
          if (cell.hasCharacter() && cell.getCharacter().getHp() == 0) {
            cell.removeCharacter();
          }
        }
      }
    });
    return future;
  }
}
