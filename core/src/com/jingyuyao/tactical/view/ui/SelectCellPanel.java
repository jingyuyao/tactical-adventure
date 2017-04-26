package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.utils.Align;
import com.google.common.eventbus.Subscribe;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.event.SelectCell;
import com.jingyuyao.tactical.model.event.WorldReset;
import com.jingyuyao.tactical.model.state.State;
import com.jingyuyao.tactical.model.terrain.Terrain;
import com.jingyuyao.tactical.model.world.Cell;
import java.util.Locale;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
class SelectCellPanel extends TextPanel<Cell> {

  private static final String CHARACTER_FMT = "%s\nHP: %d\n\n";
  private static final String TERRAIN_FMT = "%s\nMove: %d\n%s";

  SelectCellPanel() {
    setAlignment(Align.right);
  }

  @Override
  String createText(Cell cell) {
    Terrain terrain = cell.getTerrain();
    String terrainName = terrain.getName();
    int penalty = terrain.getMovementPenalty();
    String coordinateText = cell.getCoordinate().toString();

    String text = String.format(Locale.US, TERRAIN_FMT, terrainName, penalty, coordinateText);

    if (cell.hasCharacter()) {
      Character character = cell.getCharacter();
      text = String.format(Locale.US, CHARACTER_FMT, character.getName(), character.getHp()) + text;
    }

    return text;
  }

  @Subscribe
  void selectCell(SelectCell selectCell) {
    display(selectCell.getObject());
  }

  @Subscribe
  void state(State state) {
    refresh();
  }

  @Subscribe
  void worldReset(WorldReset worldReset) {
    reset();
  }
}
