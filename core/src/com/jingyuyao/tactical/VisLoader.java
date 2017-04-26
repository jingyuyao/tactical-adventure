package com.jingyuyao.tactical;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Preconditions;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;

class VisLoader {

  /**
   * Loads the {@link VisUI} skin. Can be called multiple times. Skin will only be loaded once.
   */
  static void load() {
    Preconditions.checkNotNull(Gdx.files);

    if (!VisUI.isLoaded()) {
      VisUI.load(SkinScale.X2);
    }
  }
}
