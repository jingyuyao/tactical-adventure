package com.jingyuyao.tactical.data;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jingyuyao.tactical.model.item.ItemData;

class ItemSave extends BaseSave {

  private JsonObject data;

  <T extends ItemData> T getData(Gson gson, Class<T> type) {
    return gson.fromJson(data, type);
  }
}
