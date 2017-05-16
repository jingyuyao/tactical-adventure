package com.jingyuyao.tactical.menu;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.MessageBundle;

class MenuBundle {

  private static final MessageBundle BUNDLE = new MessageBundle("i18n/menu/Menu");
  static final Message PLAY_BTN = BUNDLE.get("playBtn");
  static final Message RESET_BTN = BUNDLE.get("resetBtn");
  static final Message LEVEL_INFO = BUNDLE.get("levelInfo");
  static final Message HAS_PROGRESS = BUNDLE.get("hasProgress");
  static final Message NO_PROGRESS = BUNDLE.get("noProgress");

}
