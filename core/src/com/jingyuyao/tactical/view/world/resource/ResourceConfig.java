package com.jingyuyao.tactical.view.world.resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResourceConfig {

  @Inject
  ResourceConfig() {

  }

  int getCharacterIdleFPS() {
    return 5;
  }

  String getCharacterAssetPrefix() {
    return "character/";
  }

  int getWeaponFPS() {
    return 10;
  }

  String getWeaponAssetPrefix() {
    return "weapon/";
  }
}
