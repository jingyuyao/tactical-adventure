package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.item.Consumable;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ConsumableInfo extends VerticalGroup {

  private final Skin skin;

  @Inject
  ConsumableInfo(Skin skin) {
    this.skin = skin;
  }

  public void display(Consumable consumable) {
    Label label = new Label(getLabelString(consumable), skin);
    label.setAlignment(Align.left);
    addActor(label);
  }

  private String getLabelString(Consumable consumable) {
    return String.format(
        Locale.US,
        "%s\n" +
            "Effect: %s\n" +
            "Usage: %d",
        consumable.getName(),
        consumable.getEffects(),
        consumable.getUsageLeft()
    );
  }
}
