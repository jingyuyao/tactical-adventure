package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.item.Item;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ItemInfo extends VerticalGroup {

  private final Skin skin;
  private Item current;

  @Inject
  ItemInfo(Skin skin) {
    this.skin = skin;
    columnLeft();
  }

  @Override
  public void clear() {
    super.clear();
    current = null;
  }

  void display(Item item) {
    clear();
    Label label = new Label(getLabelString(item), skin);
    label.setAlignment(Align.left);
    addActor(label);
    current = item;
  }

  void refresh() {
    if (current != null) {
      display(current);
    }
  }

  private String getLabelString(Item item) {
    return String.format(
        Locale.US,
        "%s\n" +
            "Usage: %d\n" +
            "%s",
        item.getName(),
        item.getUsageLeft(),
        item.getDescription()
    );
  }
}
