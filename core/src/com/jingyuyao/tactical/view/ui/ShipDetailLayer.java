package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.jingyuyao.tactical.data.MessageLoader;
import com.jingyuyao.tactical.model.character.Ship;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ShipDetailLayer extends VisTable {

  private final LayerManager layerManager;
  private final ShipStatsPanel shipStatsPanel;
  private final ShipItemsPanel shipItemsPanel;
  private final MessageLoader messageLoader;

  @Inject
  ShipDetailLayer(
      LayerManager layerManager,
      ShipStatsPanel shipStatsPanel,
      ShipItemsPanel shipItemsPanel,
      MessageLoader messageLoader) {
    super(true);
    this.layerManager = layerManager;
    this.shipStatsPanel = shipStatsPanel;
    this.shipItemsPanel = shipItemsPanel;
    this.messageLoader = messageLoader;
    setFillParent(true);
    setBackground("window-bg");
    pad(20);

    VisTable scrollTable = new VisTable(true);
    scrollTable.defaults().top().left();
    scrollTable.add(shipStatsPanel);
    scrollTable.add(shipItemsPanel).expand().fill();

    VisScrollPane scrollPane = new VisScrollPane(scrollTable);
    scrollPane.setScrollingDisabled(true, false);
    add(scrollPane).grow().center();
    row();
    add(this.new CloseButton()).bottom().right();
  }

  void display(Ship ship) {
    shipStatsPanel.display(ship);
    shipItemsPanel.display(ship);
    layerManager.open(this);
  }

  private class CloseButton extends VisTextButton {

    private CloseButton() {
      super(messageLoader.get(UIBundle.CLOSE_BTN), new ChangeListener() {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
          layerManager.close(ShipDetailLayer.this);
        }
      });
      pad(20);
    }
  }
}
