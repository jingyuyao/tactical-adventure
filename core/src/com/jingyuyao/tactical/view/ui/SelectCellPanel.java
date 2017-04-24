package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SelectCellPanel extends Container<Label> {

  private static final String CHARACTER_FMT = "%s\nHP: %d\n\n";
  private static final String TERRAIN_FMT = "%s\nMove: %d\n%s";

  private final Skin skin;
  private Cell cell;

  @Inject
  SelectCellPanel(Skin skin) {
    this.skin = skin;
  }

  void display(Cell cell) {
    this.cell = cell;

    Terrain terrain = cell.getTerrain();
    String terrainName = terrain.getName();
    int penalty = terrain.getMovementPenalty();
    String coordinateText = cell.getCoordinate().toString();

    String text = String.format(Locale.US, TERRAIN_FMT, terrainName, penalty, coordinateText);

    if (cell.hasCharacter()) {
      Character character = cell.getCharacter();
      text = String.format(Locale.US, CHARACTER_FMT, character.getName(), character.getHp()) + text;
    }

    Label label = new Label(text, skin);
    label.setAlignment(Align.right);
    setActor(label);
  }

  void refresh() {
    if (cell != null) {
      display(cell);
    }
  }

  void reset() {
    cell = null;
    setActor(null);
  }
}
