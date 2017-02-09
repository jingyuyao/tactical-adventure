package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.item.Weapon;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class WeaponInfo extends VerticalGroup {

  private final Skin skin;

  @Inject
  WeaponInfo(Skin skin) {
    this.skin = skin;
    columnLeft();
  }

  public void display(Weapon weapon) {
    Label label = new Label(getLabelString(weapon), skin);
    label.setAlignment(Align.left);
    addActor(label);
  }

  private String getLabelString(Weapon weapon) {
    return String.format(
        Locale.US,
        "%s\n" +
            "Damage: %d\n" +
            "Usage: %d",
        weapon.getName(),
        weapon.getAttackPower(),
        weapon.getUsageLeft()
    );
  }
}
