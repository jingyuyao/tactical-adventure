package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;
import javax.inject.Singleton;

@Singleton
class ItemActionsFactory {

  ImmutableList<Action> create(AbstractPlayerState playerState) {
    FluentIterable<Item> items = playerState.getPlayer().fluentItems();
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(new SelectWeapon(playerState, weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(new UseConsumable(playerState, consumable));
    }
    return builder.build();
  }

  static class SelectWeapon implements Action {

    private final AbstractPlayerState playerState;
    private final Weapon weapon;

    SelectWeapon(AbstractPlayerState playerState, Weapon weapon) {
      this.playerState = playerState;
      this.weapon = weapon;
    }

    @Override
    public String getText() {
      return weapon.toString();
    }

    @Override
    public void run() {
      Player player = playerState.getPlayer();
      player.quickAccess(weapon);
      ImmutableList<Target> targets = weapon.createTargets(player.getCoordinate());
      playerState
          .goTo(playerState.getStateFactory().createSelectingTarget(player, weapon, targets));
    }
  }

  static class UseConsumable implements Action {

    private final AbstractPlayerState playerState;
    private final Consumable consumable;

    UseConsumable(AbstractPlayerState playerState, Consumable consumable) {
      this.playerState = playerState;
      this.consumable = consumable;
    }

    @Override
    public String getText() {
      return consumable.toString();
    }

    @Override
    public void run() {
      Player player = playerState.getPlayer();
      player.quickAccess(consumable);
      consumable.apply(player);
      player.useItem(consumable);
      playerState.finish();
    }
  }
}
