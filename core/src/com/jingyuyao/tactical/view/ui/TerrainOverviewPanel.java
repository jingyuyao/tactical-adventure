package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
class TerrainOverviewPanel extends TextPanel<Terrain> {

  private static final String FMT = "Type: %s\nMove: %d";

  TerrainOverviewPanel() {
    super(Align.right);
  }

  @Override
  Optional<String> getText(Terrain terrain) {
    return Optional.of(String.format(
        Locale.US, FMT, terrain.getName(), terrain.getMovementPenalty()));
  }
}
