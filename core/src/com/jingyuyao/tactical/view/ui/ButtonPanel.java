package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.google.common.base.Optional;
import com.kotcrab.vis.ui.widget.VisTextButton;

/**
 * A clickable panel with a dark background that can display an object in text format. This panel
 * can be manually refreshed to update its text (based on the previous object). This panel can also
 * be cleared so it does not display anything and does not take up any layout space.
 */
abstract class ButtonPanel<T> extends Container<VisTextButton> {

  private final VisTextButton button;
  private T object;

  ButtonPanel(int alignment) {
    this.button = new VisTextButton(null, "blue", new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        if (object != null) {
          click(object);
        }
      }
    });
    this.button.pad(15);
    this.button.getLabel().setAlignment(alignment);
    setVisible(false);
  }

  void display(T object) {
    Optional<String> textOptional = getText(object);
    if (textOptional.isPresent()) {
      this.object = object;
      button.setText(textOptional.get());
      setActor(button);
      setVisible(true);
    } else {
      clearDisplay();
    }
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
   * Called to create the text for an object. If the return value is not present, it will not be
   * displayed and the object will not be stored for future refresh.
   */
  abstract Optional<String> getText(T object);

  /**
   * Called when this panel is clicked.
   *
   * @param object never null
   */
  abstract void click(T object);
}
