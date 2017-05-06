package com.jingyuyao.tactical.view.ui;

import static com.jingyuyao.tactical.view.ui.GameUIModule.BUNDLE;

import com.badlogic.gdx.utils.Align;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.i18n.Message;
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
    Message message = BUNDLE
        .get("terrainOverviewPanel", terrain.getName(), terrain.getMovementPenalty());
    return Optional.of(messageLoader.get(message));
  }
}
