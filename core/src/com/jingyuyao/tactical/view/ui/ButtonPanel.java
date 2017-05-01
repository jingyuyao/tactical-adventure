package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
    this.button = new VisTextButton(null, new ChangeListener() {
      @Override
      public void changed(ChangeEvent event, Actor actor) {
        if (object != null) {
          click(object);
        }
      }
    });
    this.button.getLabel().setAlignment(alignment);
    this.button.pad(15);
    setVisible(false);
  }

  void display(T object) {
    this.object = object;
    button.setText(createText(object));
    setActor(button);
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

  /**
   * Called when this panel is clicked.
   *
   * @param object never null
   */
  abstract void click(T object);
}
