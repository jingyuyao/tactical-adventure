package com.jingyuyao.tactical.menu;

import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;

class MenuBundle {

  private static final ResourceKeyBundle BUNDLE = new ResourceKeyBundle("i18n/menu/Menu");
  static final ResourceKey PLAY_BTN = BUNDLE.get("playBtn");
  static final ResourceKey RESET_BTN = BUNDLE.get("resetBtn");
  static final ResourceKey LEVEL_INFO = BUNDLE.get("levelInfo");
  static final ResourceKey HAS_PROGRESS = BUNDLE.get("hasProgress");
  static final ResourceKey NO_PROGRESS = BUNDLE.get("noProgress");

}
