package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.jingyuyao.tactical.model.terrain.Terrain;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TerrainInfo extends VerticalGroup {

  private final Skin skin;

  @Inject
  TerrainInfo(Skin skin) {
    this.skin = skin;
    columnRight();
  }

  public void display(Terrain terrain) {
    clear();
    addActor(new Label(terrain.getClass().getSimpleName(), skin));
    addActor(
        new Label(String.format(Locale.US, "Penalty: %d", terrain.getMovementPenalty()), skin));
  }
}
