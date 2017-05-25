package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class ItemPanel extends TextPanel<Item> {

  private final TextLoader textLoader;

  @Inject
  ItemPanel(TextLoader textLoader) {
    super(Align.left);
    this.textLoader = textLoader;
  }

  @Override
  Optional<String> createText(Item item) {
    String name = textLoader.get(item.getName());
    int usage = item.getUsageLeft();
    String description = textLoader.get(item.getDescription());
    StringKey stringKey = UIBundle.ITEM_PANEL.format(name, usage, description);
    return Optional.of(textLoader.get(stringKey));
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
