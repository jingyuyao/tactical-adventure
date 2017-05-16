package com.jingyuyao.tactical.view.world.resource;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ResourceConfig {

  @Inject
  ResourceConfig() {

  }

  int getShipIdleFPS() {
    return 5;
  }

  String getShipAssetPrefix() {
    return "ship/";
  }

  int getWeaponFPS() {
    return 10;
  }

  String getWeaponAssetPrefix() {
    return "weapon/";
  }
}
