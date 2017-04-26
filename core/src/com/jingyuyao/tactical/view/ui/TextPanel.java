package com.jingyuyao.tactical.view.ui;

import com.kotcrab.vis.ui.widget.VisLabel;

/**
 * A panel that can display an object as a {@link VisLabel}.
 */
abstract class TextPanel<T> extends VisLabel {

  private T object;

  TextPanel() {
    setFontScale(0.5f);
  }

  void display(T object) {
    this.object = object;
    setText(createText(object));
  }

  void refresh() {
    if (object != null) {
      display(object);
    }
  }

  void reset() {
    object = null;
    setText(null);
  }

  abstract String createText(T object);
}
