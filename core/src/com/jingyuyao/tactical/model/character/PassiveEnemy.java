package com.jingyuyao.tactical.model.character;

import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.battle.Battle;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Terrains;
import java.util.List;
import javax.inject.Inject;

public class PassiveEnemy extends AbstractEnemy {

  private final transient Movements movements;
  private final transient Battle battle;

  @Inject
  PassiveEnemy(
      @CharacterEventBus EventBus eventBus, Terrains terrains, Movements movements, Battle battle) {
    super(eventBus, terrains);
    this.movements = movements;
    this.battle = battle;
  }

  PassiveEnemy(
      Coordinate coordinate, Terrains terrains, Movements movements, EventBus eventBus,
      Battle battle, String name, int maxHp, int hp, int moveDistance, List<Item> items) {
    super(coordinate, eventBus, terrains, name, maxHp, hp, moveDistance, items);
    this.movements = movements;
    this.battle = battle;
  }

  @Override
  public ListenableFuture<Void> retaliate() {
    // TODO: fix me!
//    Movement movement = movements.distanceFrom(this);
//    Coordinate originalCoordinate = getCoordinate();
//
//    for (Coordinate moveCoordinate : movement.getCoordinates()) {
//      setCoordinate(moveCoordinate);
//      for (final Weapon weapon : fluentItems().filter(Weapon.class)) {
//        for (final Target target : weapon.createTargets(getCoordinate())) {
//          FluentIterable<Character> targetCharacters = target.getTargetCharacters();
//          // Don't hit friendly characters?
//          if (!targetCharacters.filter(Enemy.class).isEmpty()) {
//            continue;
//          }
//          if (!targetCharacters.filter(Player.class).isEmpty()) {
//            setCoordinate(originalCoordinate);
//            Path path = movement.pathTo(moveCoordinate);
//
//            return Futures.transformAsync(moveAlong(path), new AsyncFunction<Void, Void>() {
//              @Override
//              public ListenableFuture<Void> apply(Void input) {
//                return battle.begin(PassiveEnemy.this, weapon, target);
//              }
//            });
//          }
//        }
//      }
//    }
//    setCoordinate(originalCoordinate);
//    return Futures.immediateFuture(null);
    return Futures.immediateFuture(null);
  }
}
