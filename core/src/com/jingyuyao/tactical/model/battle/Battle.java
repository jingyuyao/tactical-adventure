package com.jingyuyao.tactical.model.battle;

import com.jingyuyao.tactical.model.ModelBus;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.Attack;
import com.jingyuyao.tactical.model.event.MyFuture;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.world.Cell;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
// TODO: we really don't need this class, merge this with battling state
public class Battle {

  private final ModelBus modelBus;

  @Inject
  Battle(ModelBus modelBus) {
    this.modelBus = modelBus;
  }

  public MyFuture begin(
      final Character attacker, final Weapon weapon, final Target target) {
    MyFuture future = new MyFuture();
    modelBus.post(new Attack(target, weapon, future));
    future.addCallback(new Runnable() {
      @Override
      public void run() {
        attacker.useWeapon(weapon);
        weapon.damages(target);
        for (Cell cell : target.getTargetCells()) {
          for (Character character : cell.character().asSet()) {
            character.useEquippedArmors();
            if (character.getHp() == 0) {
              cell.removeCharacter();
            }
          }
        }
      }
    });
    return future;
  }
}
