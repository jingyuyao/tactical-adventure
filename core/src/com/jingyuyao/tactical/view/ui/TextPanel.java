package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;

/**
 * A panel with a dark background that can display an object in text format. This panel can be
 * manually refreshed to update its text (based on the previous object). This panel can also be
 * cleared so it does not display anything and does not take up any layout space.
 */
abstract class TextPanel<T> extends Container<VisLabel> {

  private final VisLabel label;
  private T object;

  TextPanel(int alignment) {
    this.label = new VisLabel(null, alignment);
    setBackground(VisUI.getSkin().getDrawable("window-bg"));
    setVisible(false);
    pad(15);
  }

  void display(T object) {
    this.object = object;
    label.setText(createText(object));
    setActor(label);
    setVisible(true);
  }

  void refresh() {
    if (object != null) {
      display(object);
    }
  }

  void clearDisplay() {
    object = null;
    setVisible(false);  // hides background
    setActor(null);  // stop panel from taking up space
  }

  /**
   * Called to create the text for an object.
   */
  abstract String createText(T object);
}
