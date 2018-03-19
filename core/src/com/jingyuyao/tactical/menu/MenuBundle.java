package com.jingyuyao.tactical.menu;

import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.StringKey;

class MenuBundle {

  private static final KeyBundle BUNDLE = KeyBundle.i18n("menu/Menu");
  static final StringKey PLAY_BTN = BUNDLE.get("playBtn");
  static final StringKey SHOW_INSTRUCTIONS_BTN = BUNDLE.get("showInstructionsBtn");
  static final StringKey RESET_LEVEL_BTN = BUNDLE.get("resetLevelBtn");
  static final StringKey CLEAR_SAVE_BTN = BUNDLE.get("clearSaveBtn");
  static final StringKey CONTINUE_BTN = BUNDLE.get("continueBtn");
  static final StringKey LEVEL_INFO = BUNDLE.get("levelInfo");
  static final StringKey HAS_PROGRESS = BUNDLE.get("hasProgress");
  static final StringKey NO_PROGRESS = BUNDLE.get("noProgress");
  static final StringKey LEVEL_WON = BUNDLE.get("levelWon");
  static final StringKey LEVEL_LOST = BUNDLE.get("levelLost");
  static final StringKey INSTRUCTIONS_TITLE = BUNDLE.get("instructionsTitle");
  static final StringKey INSTRUCTIONS = BUNDLE.get("instructions");

}
