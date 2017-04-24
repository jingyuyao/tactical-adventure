package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * A panel that can display an object as a label.
 */
abstract class DisplayPanel<T> extends Container<Label> {

  private T object;

  void display(T object) {
    this.object = object;
    setActor(createLabel(object));
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

  abstract Label createLabel(T object);
}
