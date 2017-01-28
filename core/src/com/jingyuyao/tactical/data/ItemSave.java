package com.jingyuyao.tactical.data;

import com.google.gson.JsonObject;

class ItemSave {

  /**
   * The canonical class name.
   */
  private String className;
  private JsonObject data;

  String getClassName() {
    return className;
  }

  JsonObject getData() {
    return data;
  }
}
