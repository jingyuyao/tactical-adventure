package com.jingyuyao.tactical.menu;

import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

class MenuBundle {

  private static final KeyBundle BUNDLE = KeyBundle.i18n("menu/Menu");
  static final StringKey PLAY_BTN = BUNDLE.get("playBtn");
  static final StringKey RESET_BTN = BUNDLE.get("resetBtn");
  static final StringKey CLEAR_SAVE = BUNDLE.get("clearSave");
  static final StringKey LEVEL_INFO = BUNDLE.get("levelInfo");
  static final StringKey HAS_PROGRESS = BUNDLE.get("hasProgress");
  static final StringKey NO_PROGRESS = BUNDLE.get("noProgress");

}
