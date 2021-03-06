package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.data.TextLoader;
import com.jingyuyao.tactical.model.ship.Ship;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipDetailLayer extends VisTable {

  private final LayerManager layerManager;
  private final ShipStatsPanel shipStatsPanel;
  private final ShipPersonPanel shipPersonPanel;
  private final ShipItemsPanel shipItemsPanel;
  private final TextLoader textLoader;

  @Inject
  ShipDetailLayer(
      LayerManager layerManager,
      ShipStatsPanel shipStatsPanel,
      ShipPersonPanel shipPersonPanel,
      ShipItemsPanel shipItemsPanel,
      TextLoader textLoader) {
    super(true);
    this.layerManager = layerManager;
    this.shipStatsPanel = shipStatsPanel;
    this.shipPersonPanel = shipPersonPanel;
    this.shipItemsPanel = shipItemsPanel;
    this.textLoader = textLoader;
    setFillParent(true);
    setBackground("window-bg");
    pad(20);

    VisTable scrollTable = new VisTable(true);
    scrollTable.defaults().top().left();
    scrollTable.add(shipStatsPanel);
    scrollTable.add(shipPersonPanel);
    scrollTable.row();
    scrollTable.add();
    scrollTable.add(shipItemsPanel).expand().fill();

    VisScrollPane scrollPane = new VisScrollPane(scrollTable);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).grow().center();
    row();
    add(this.new CloseButton()).bottom().right();
  }

  void display(Ship ship) {
    shipStatsPanel.display(ship);
    shipPersonPanel.display(ship);
    shipItemsPanel.display(ship);
    layerManager.open(this);
  }

  private class CloseButton extends VisTextButton {

    private CloseButton() {
      super(textLoader.get(UIBundle.CLOSE_BTN), "blue", new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          layerManager.close(ShipDetailLayer.this);
        }
      });
      pad(20);
    }
  }
}
