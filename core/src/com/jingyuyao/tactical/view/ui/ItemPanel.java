package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.item.Item;
import com.kotcrab.vis.ui.widget.VisLabel;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
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
}
