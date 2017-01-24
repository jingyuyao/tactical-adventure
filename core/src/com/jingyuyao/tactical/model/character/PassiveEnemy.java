package com.jingyuyao.tactical.model.character;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.common.Coordinate;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Movement;
import com.jingyuyao.tactical.model.map.MovementFactory;
import com.jingyuyao.tactical.model.map.Path;
import java.util.List;
import javax.inject.Inject;

class PassiveEnemy extends Enemy {

  private final MovementFactory movementFactory;

  @Inject
  PassiveEnemy(EventBus eventBus,
      @Assisted Coordinate coordinate,
      @Assisted Stats stats,
      @Assisted List<Item> items,
      MovementFactory movementFactory) {
    super(eventBus, coordinate, stats, items);
    this.movementFactory = movementFactory;
  }

  @Override
  public ListenableFuture<Void> retaliate() {
    Movement movement = movementFactory.create(this);
    Coordinate originalCoordinate = getCoordinate();

    for (Coordinate moveCoordinate : movement.getCoordinates()) {
      setCoordinate(moveCoordinate);
      for (final Weapon weapon : getWeapons()) {
        for (final Target target : weapon.createTargets(getCoordinate())) {
          ImmutableSet<Character> targetCharacters = target.getTargetCharacters();
          // Don't hit friendly characters?
          if (!Iterables.isEmpty(Iterables.filter(targetCharacters, Enemy.class))) {
            continue;
          }
          boolean hasPlayers = !Iterables.isEmpty(Iterables.filter(targetCharacters, Player.class));

          if (hasPlayers) {
            setCoordinate(originalCoordinate);
            Path path = movement.pathTo(moveCoordinate);

            return Futures.transformAsync(moveAlong(path), new AsyncFunction<Void, Void>() {
              @Override
              public ListenableFuture<Void> apply(Void input) {
                return weapon.attack(PassiveEnemy.this, target);
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
