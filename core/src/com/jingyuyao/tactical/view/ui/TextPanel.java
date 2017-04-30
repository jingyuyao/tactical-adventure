package com.jingyuyao.tactical.view.ui;

import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 * A refreshable button that can display an object as text.
 */
abstract class TextPanel<T> extends VisTextButton {

  private T object;

  TextPanel() {
    super(null);
    setVisible(false);
    pad(10);
  }

  void display(T object) {
    this.object = object;
    setText(createText(object));
    setVisible(true);
  }

  void refresh() {
    if (object != null) {
      display(object);
    }
  }

  void clearDisplay() {
    object = null;
    setVisible(false);
    setText(null);
  }

  T getObject() {
    return object;
  }

  abstract String createText(T object);
}
