package com.jingyuyao.tactical.view.ui;

import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Locale;

class TerrainOverviewPanel extends TextPanel<Terrain> {

  private static final String FMT = "%s\nMove: %d";

  @Override
  String createText(Terrain terrain) {
    return String.format(Locale.US, FMT, terrain.getName(), terrain.getMovementPenalty());
  }
}
