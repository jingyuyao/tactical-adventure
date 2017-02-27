package com.jingyuyao.tactical.view.resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResourceConfig {

  @Inject
  ResourceConfig() {

  }

  float getFrameDuration() {
    return 0.2f;
  }

  String getCharacterAssetPrefix() {
    return "character/";
  }
}
