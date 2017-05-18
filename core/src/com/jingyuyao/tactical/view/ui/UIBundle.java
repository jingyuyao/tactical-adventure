package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;

class UIBundle {

  private static final ResourceKeyBundle BUNDLE = new ResourceKeyBundle("i18n/view/ui/UI");
  static final ResourceKey CLOSE_BTN = BUNDLE.get("closeBtn");
  static final ResourceKey NEXT_BTN = BUNDLE.get("nextBtn");
  static final ResourceKey PERSON_NAME_HEADER = BUNDLE.get("personNameHeader");
  static final ResourceKey PERSON_ROLE_HEADER = BUNDLE.get("personRoleHeader");
  static final ResourceKey ITEM_NAME_HEADER = BUNDLE.get("itemNameHeader");
  static final ResourceKey ITEM_DURABILITY_HEADER = BUNDLE.get("itemDurabilityHeader");
  static final ResourceKey ITEM_DESCRIPTION_HEADER = BUNDLE.get("itemDescriptionHeader");
  static final ResourceKey ITEM_ACTION_HEADER = BUNDLE.get("itemActionHeader");
  static final ResourceKey EQUIP_BTN = BUNDLE.get("equipBtn");
  static final ResourceKey UNEQUIP_BTN = BUNDLE.get("unequipBtn");
  static final ResourceKey OVERVIEW_PANEL = BUNDLE.get("shipOverviewPanel");
  static final ResourceKey SHIP_NAME_HEADER = BUNDLE.get("shipNameHeader");
  static final ResourceKey SHIP_HP_HEADER = BUNDLE.get("shipHPHeader");
  static final ResourceKey SHIP_MOVE_HEADER = BUNDLE.get("shipMoveHeader");
  static final ResourceKey ITEM_PANEL = BUNDLE.get("itemPanel");
  static final ResourceKey TARGET_PANEL_HEADER = BUNDLE.get("targetPanelHeader");
  static final ResourceKey TARGET_PANEL_ITEM = BUNDLE.get("targetPanelItem");
  static final ResourceKey TERRAIN_OVERVIEW_PANEL = BUNDLE.get("terrainOverviewPanel");

}
