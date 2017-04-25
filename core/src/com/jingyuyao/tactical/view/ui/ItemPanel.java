package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.event.ExitState;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.model.state.Battling;
import com.jingyuyao.tactical.model.state.SelectingTarget;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.state.UsingConsumable;
import com.kotcrab.vis.ui.widget.VisLabel;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class ItemPanel extends DisplayPanel<Item> {

  private static final String ITEM_FMT = "%s\nUsage: %d\n%s";

  @Override
  Label createLabel(Item item) {
    String text =
        String.format(
            Locale.US, ITEM_FMT, item.getName(), item.getUsageLeft(), item.getDescription());
    VisLabel label = new VisLabel(text);
    label.setAlignment(Align.left);
    label.setFontScale(0.5f);

    return label;
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
    display(battling.getWeapon());
  }

  @Subscribe
  void exitState(ExitState exitState) {
    reset();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    reset();
  }
}
