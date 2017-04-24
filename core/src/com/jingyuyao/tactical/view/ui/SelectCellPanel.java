package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import com.kotcrab.vis.ui.widget.VisLabel;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
class SelectCellPanel extends DisplayPanel<Cell> {

  private static final String CHARACTER_FMT = "%s\nHP: %d\n\n";
  private static final String TERRAIN_FMT = "%s\nMove: %d\n%s";

  @Override
  Label createLabel(Cell cell) {
    Terrain terrain = cell.getTerrain();
    String terrainName = terrain.getName();
    int penalty = terrain.getMovementPenalty();
    String coordinateText = cell.getCoordinate().toString();

    String text = String.format(Locale.US, TERRAIN_FMT, terrainName, penalty, coordinateText);

    if (cell.hasCharacter()) {
      Character character = cell.getCharacter();
      text = String.format(Locale.US, CHARACTER_FMT, character.getName(), character.getHp()) + text;
    }

    VisLabel label = new VisLabel(text);
    label.setAlignment(Align.right);
    label.setFontScale(0.5f);
    return label;
  }
}
