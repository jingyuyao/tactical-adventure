package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class ItemPanel extends TextPanel<Item> {

  private static final String ITEM_FMT = "%s\nUsage: %d\n%s";

  private final MessageLoader messageLoader;

  @Inject
  ItemPanel(MessageLoader messageLoader) {
    super(Align.left);
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Item item) {
    return Optional.of(String.format(
        Locale.US, ITEM_FMT,
        messageLoader.get(item.getName()),
        item.getUsageLeft(),
        messageLoader.get(item.getDescription())));
  }

  @Subscribe
  void state(State state) {
    refresh();
  }

  @Subscribe
  void usingConsumable(UsingConsumable usingConsumable) {
    display(usingConsumable.getConsumable());
  }

  @Subscribe
  void selectingTarget(SelectingTarget selectingTarget) {
    display(selectingTarget.getWeapon());
  }

  @Subscribe
  void battling(Battling battling) {
    display(battling.getBattle().getWeapon());
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
