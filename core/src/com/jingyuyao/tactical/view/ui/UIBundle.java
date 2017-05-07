package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;

class UIBundle {

  private static final MessageBundle BUNDLE = new MessageBundle("i18n/view/ui/UI");
  static final Message CLOSE_BTN = BUNDLE.get("closeBtn");
  static final Message ITEM_NAME_HEADER = BUNDLE.get("itemNameHeader");
  static final Message ITEM_DURABILITY_HEADER = BUNDLE.get("itemDurabilityHeader");
  static final Message ITEM_DESCRIPTION_HEADER = BUNDLE.get("itemDescriptionHeader");
  static final Message ITEM_ACTION_HEADER = BUNDLE.get("itemActionHeader");
  static final Message EQUIP_BTN = BUNDLE.get("equipBtn");
  static final Message UNEQUIP_BTN = BUNDLE.get("unequipBtn");
  static final Message OVERVIEW_PANEL = BUNDLE.get("characterOverviewPanel");
  static final Message CHARACTER_NAME_HEADER = BUNDLE.get("characterNameHeader");
  static final Message CHARACTER_HP_HEADER = BUNDLE.get("characterHPHeader");
  static final Message CHARACTER_MOVE_HEADER = BUNDLE.get("characterMoveHeader");
  static final Message ITEM_PANEL = BUNDLE.get("itemPanel");
  static final Message TARGET_PANEL_HEADER = BUNDLE.get("targetPanelHeader");
  static final Message TARGET_PANEL_ITEM = BUNDLE.get("targetPanelItem");
  static final Message TERRAIN_OVERVIEW_PANEL = BUNDLE.get("terrainOverviewPanel");

}
