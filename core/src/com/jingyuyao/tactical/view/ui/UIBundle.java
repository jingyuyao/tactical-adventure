package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.resource.MessageBundle;

class UIBundle {

  private static final MessageBundle BUNDLE = new MessageBundle("i18n/view/ui/UI");
  static final Message CLOSE_BTN = BUNDLE.get("closeBtn");
  static final Message NEXT_BTN = BUNDLE.get("nextBtn");
  static final Message PILOT_NAME_HEADER = BUNDLE.get("pilotNameHeader");
  static final Message ITEM_NAME_HEADER = BUNDLE.get("itemNameHeader");
  static final Message ITEM_DURABILITY_HEADER = BUNDLE.get("itemDurabilityHeader");
  static final Message ITEM_DESCRIPTION_HEADER = BUNDLE.get("itemDescriptionHeader");
  static final Message ITEM_ACTION_HEADER = BUNDLE.get("itemActionHeader");
  static final Message EQUIP_BTN = BUNDLE.get("equipBtn");
  static final Message UNEQUIP_BTN = BUNDLE.get("unequipBtn");
  static final Message OVERVIEW_PANEL = BUNDLE.get("shipOverviewPanel");
  static final Message SHIP_NAME_HEADER = BUNDLE.get("shipNameHeader");
  static final Message SHIP_HP_HEADER = BUNDLE.get("shipHPHeader");
  static final Message SHIP_MOVE_HEADER = BUNDLE.get("shipMoveHeader");
  static final Message ITEM_PANEL = BUNDLE.get("itemPanel");
  static final Message TARGET_PANEL_HEADER = BUNDLE.get("targetPanelHeader");
  static final Message TARGET_PANEL_ITEM = BUNDLE.get("targetPanelItem");
  static final Message TERRAIN_OVERVIEW_PANEL = BUNDLE.get("terrainOverviewPanel");

}
