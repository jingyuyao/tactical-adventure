package com.jingyuyao.tactical.model.character;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Coordinate;
import com.jingyuyao.tactical.model.map.MapModule.InitialMarkers;
import com.jingyuyao.tactical.model.map.Marker;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.Movements;
import com.jingyuyao.tactical.model.map.Path;
import java.util.List;
import javax.inject.Inject;

public class PassiveEnemy extends AbstractEnemy {

  private transient final Movements movements;

  @Inject
  PassiveEnemy(
      @InitialMarkers Multiset<Marker> markers,
      @CharacterEventBus EventBus eventBus,
      Movements movements) {
    super(markers, eventBus);
    this.movements = movements;
  }

  PassiveEnemy(
      Coordinate coordinate, Multiset<Marker> markers, Movements movements,
      EventBus eventBus, String name, int maxHp, int hp, int moveDistance,
      List<Item> items) {
    super(coordinate, markers, eventBus, name, maxHp, hp, moveDistance, items);
    this.movements = movements;
  }

  @Override
  public ListenableFuture<Void> retaliate() {
    Movement movement = movements.distanceFrom(this);
    Coordinate originalCoordinate = getCoordinate();

    for (Coordinate moveCoordinate : movement.getCoordinates()) {
      setCoordinate(moveCoordinate);
      for (final Weapon weapon : fluentItems().filter(Weapon.class)) {
        for (final Target target : weapon.createTargets(getCoordinate())) {
          FluentIterable<Character> targetCharacters = target.getTargetCharacters();
          // Don't hit friendly characters?
          if (!targetCharacters.filter(Enemy.class).isEmpty()) {
            continue;
          }
          if (!targetCharacters.filter(Player.class).isEmpty()) {
            setCoordinate(originalCoordinate);
            Path path = movement.pathTo(moveCoordinate);

            return Futures.transformAsync(moveAlong(path), new AsyncFunction<Void, Void>() {
              @Override
              public ListenableFuture<Void> apply(Void input) {
                return attacks(weapon, target);
              }
            });
          }
        }
      }
    }
    setCoordinate(originalCoordinate);
    return Futures.immediateFuture(null);
  }
}
