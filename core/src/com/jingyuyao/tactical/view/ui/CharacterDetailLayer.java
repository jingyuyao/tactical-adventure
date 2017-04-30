package com.jingyuyao.tactical.view.ui;

import com.google.common.collect.FluentIterable;
import com.jingyuyao.tactical.model.character.Character;
import com.jingyuyao.tactical.model.item.Item;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.kotcrab.vis.ui.building.OneColumnTableBuilder;
import com.kotcrab.vis.ui.building.TableBuilder;
import com.kotcrab.vis.ui.building.utilities.Padding;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;
import java.util.Locale;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class CharacterDetailLayer extends VisWindow {

  private final LayerManager layerManager;
  private final VisScrollPane content;

  @Inject
  CharacterDetailLayer(LayerManager layerManager) {
    super("Character Detail", false);
    this.layerManager = layerManager;
    this.content = new VisScrollPane(null);
    setFillParent(true);
    setMovable(false);
    addCloseButton();

    add(content).expand().fill().center();
  }

  void display(Character character) {
    TableBuilder builder = new OneColumnTableBuilder(new Padding(20));
    builder.append(new VisLabel(String.format("Name: %s", character.getName()), Colors.BLUE_300));
    builder.append(new VisLabel(String.format(Locale.US, "HP: %d", character.getHp())));
    builder.append(new VisLabel(String.format(Locale.US, "Move: %d", character.getMoveDistance())));
    builder.append(new VisLabel("Items:"));
    FluentIterable<Item> items = character.fluentItems();
    if (items.isEmpty()) {
      builder.append(new VisLabel("None"));
    } else {
      for (Item item : character.fluentItems()) {
        builder.append(new VisLabel(String.format(
            Locale.US, "%s (%d): %s",
            item.getName(), item.getUsageLeft(), item.getDescription())));
      }
    }
    content.setWidget(builder.build());
  }

  @Override
  protected void close() {
    layerManager.close(CharacterDetailLayer.this);
  }
}
