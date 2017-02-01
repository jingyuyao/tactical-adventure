package com.jingyuyao.tactical.model.character;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.eventbus.EventBus;
import com.google.common.util.concurrent.AsyncFunction;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.jingyuyao.tactical.model.character.CharacterModule.CharacterEventBus;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import com.jingyuyao.tactical.model.map.Characters;
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
      Movements movements,
      Characters characters) {
    super(markers, eventBus, characters);
    this.movements = movements;
  }

  PassiveEnemy(
      Coordinate coordinate, Multiset<Marker> markers, Movements movements,
      Characters characters, EventBus eventBus, String name, int maxHp, int hp, int moveDistance,
      List<Item> items) {
    super(coordinate, markers, characters, eventBus, name, maxHp, hp, moveDistance, items);
    this.movements = movements;
  }

  @Override
  public ListenableFuture<Void> retaliate() {
    Movement movement = movements.distanceFrom(this);
    Coordinate originalCoordinate = getCoordinate();

    for (Coordinate moveCoordinate : movement.getCoordinates()) {
      setCoordinate(moveCoordinate);
      for (final Weapon weapon : getWeapons()) {
        for (final Target target : weapon.createTargets(getCoordinate())) {
          Iterable<Character> targetCharacters = target.getTargetCharacters();
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
