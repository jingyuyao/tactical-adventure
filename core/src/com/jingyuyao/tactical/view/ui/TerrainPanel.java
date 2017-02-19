package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class TerrainPanel extends VerticalGroup {

  private final Skin skin;

  @Inject
  TerrainPanel(Skin skin) {
    this.skin = skin;
    columnRight();
  }

  void display(Terrain terrain) {
    clear();
    addActor(new Label(terrain.getClass().getSimpleName(), skin));
    addActor(
        new Label(String.format(Locale.US, "Penalty: %d", terrain.getMovementPenalty()), skin));
  }
}
