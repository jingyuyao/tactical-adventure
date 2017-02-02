package com.jingyuyao.tactical.model.state;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Consumable;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.item.Target;
import com.jingyuyao.tactical.model.item.Weapon;

class ItemActions extends AbstractPlayerState {

  ItemActions(MapState mapState, StateFactory stateFactory, Player player) {
    super(mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    FluentIterable<Item> items = getPlayer().fluentItems();
    ImmutableList.Builder<Action> builder = ImmutableList.builder();
    for (Weapon weapon : items.filter(Weapon.class)) {
      builder.add(this.new SelectWeapon(weapon));
    }
    for (Consumable consumable : items.filter(Consumable.class)) {
      builder.add(this.new UseConsumable(consumable));
    }
    builder.add(this.new Finish());
    builder.add(this.new Back());
    return builder.build();
  }

  class SelectWeapon implements Action {

    private final Weapon weapon;

    SelectWeapon(Weapon weapon) {
      this.weapon = weapon;
    }

    @Override
    public String getText() {
      return weapon.toString();
    }

    @Override
    public void run() {
      getPlayer().quickAccess(weapon);
      ImmutableList<Target> targets = weapon.createTargets(getPlayer().getCoordinate());
      goTo(getStateFactory().createSelectingTarget(getPlayer(), weapon, targets));
    }
  }

  class UseConsumable implements Action {

    private final Consumable consumable;

    UseConsumable(Consumable consumable) {
      this.consumable = consumable;
    }

    @Override
    public String getText() {
      return consumable.toString();
    }

    @Override
    public void run() {
      getPlayer().quickAccess(consumable);
      consumable.apply(getPlayer());
      getPlayer().useItem(consumable);
      finish();
    }
  }
}
