package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainOverviewPanel extends TextPanel<Terrain> {

  private final TextLoader textLoader;

  @Inject
  TerrainOverviewPanel(TextLoader textLoader) {
    super(Align.right);
    this.textLoader = textLoader;
  }

  @Override
  Optional<String> createText(Terrain terrain) {
    String name = textLoader.get(terrain.getName());
    int moveCost = terrain.getMovementPenalty();
    ResourceKey resourceKey = UIBundle.TERRAIN_OVERVIEW_PANEL.format(name, moveCost);
    return Optional.of(textLoader.get(resourceKey));
  }
}
