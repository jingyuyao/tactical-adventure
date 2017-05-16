package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.resource.Message;
import com.jingyuyao.tactical.model.terrain.Terrain;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainOverviewPanel extends TextPanel<Terrain> {

  private final MessageLoader messageLoader;

  @Inject
  TerrainOverviewPanel(MessageLoader messageLoader) {
    super(Align.right);
    this.messageLoader = messageLoader;
  }

  @Override
  Optional<String> createText(Terrain terrain) {
    String name = messageLoader.get(terrain.getName());
    int moveCost = terrain.getMovementPenalty();
    Message message = UIBundle.TERRAIN_OVERVIEW_PANEL.format(name, moveCost);
    return Optional.of(messageLoader.get(message));
  }
}
