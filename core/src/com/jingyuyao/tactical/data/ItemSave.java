package com.jingyuyao.tactical.data;

import com.google.gson.JsonObject;

class ItemSave {

  /**
   * The canonical class name.
   */
  private String dataClassName;
  private JsonObject data;

  String getDataClassName() {
    return dataClassName;
  }

  JsonObject getData() {
    return data;
  }
}
