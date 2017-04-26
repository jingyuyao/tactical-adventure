package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.kotcrab.vis.ui.widget.VisLabel;

/**
 * A panel that can display an object as a {@link VisLabel}.
 */
abstract class TextPanel<T> extends Container<VisLabel> {

  private T object;

  void display(T object) {
    this.object = object;
    VisLabel label = new VisLabel(createText(object));
    label.setAlignment(labelAlign());
    label.setFontScale(0.5f);
    setActor(label);
  }

  void refresh() {
    if (object != null) {
      display(object);
    }
  }

  void reset() {
    object = null;
    setActor(null);
  }

  abstract String createText(T object);

  abstract int labelAlign();
}
