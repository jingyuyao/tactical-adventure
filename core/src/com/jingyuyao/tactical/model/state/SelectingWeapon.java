package com.jingyuyao.tactical.model.state;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.jingyuyao.tactical.model.character.Player;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.Locale;
import javax.inject.Inject;

class SelectingWeapon extends AbstractPlayerState {

  @Inject
  SelectingWeapon(
      EventBus eventBus,
      MapState mapState,
      StateFactory stateFactory,
      @Assisted Player player) {
    super(eventBus, mapState, stateFactory, player);
  }

  @Override
  public ImmutableList<Action> getActions() {
    ImmutableList.Builder<Action> builder = new ImmutableList.Builder<Action>();
    for (Weapon weapon : getPlayer().getWeapons()) {
      builder.add(this.new SelectWeapon(weapon));
    }
    builder.add(this.new Back());
    return builder.build();
  }

  class SelectWeapon implements Action {

    private final Weapon weapon;

    SelectWeapon(Weapon weapon) {
      this.weapon = weapon;
    }

    @Override
    public String getName() {
      return String.format(Locale.US, "%s (%d)", weapon.getName(), weapon.getUsageLeft());
    }

    @Override
    public void run() {
      getPlayer().equipWeapon(weapon);
      goTo(getStateFactory()
          .createSelectingTarget(weapon, weapon.getTargets(getPlayer().getCoordinate())));
    }
  }
}
