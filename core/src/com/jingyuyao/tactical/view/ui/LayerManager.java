package com.jingyuyao.tactical.view.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.jingyuyao.tactical.view.ui.WorldUIModule.BackingLayerStack;
import com.jingyuyao.tactical.view.ui.WorldUIModule.UIStage;
import java.util.Deque;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class LayerManager {

  private final Stage stage;
  private final Deque<Actor> layerStack;

  @Inject
  LayerManager(@UIStage Stage stage, @BackingLayerStack Deque<Actor> layerStack) {
    this.stage = stage;
    this.layerStack = layerStack;
  }

  void init(Actor layer) {
    layerStack.clear();
    stage.clear();
    layerStack.push(layer);
    stage.addActor(layer);
  }

  void open(Actor layer) {
    layerStack.push(layer);
    stage.clear();
    stage.addActor(layer);
  }

  void close(Actor layer) {
    layerStack.remove(layer);
    stage.clear();
    stage.addActor(layerStack.peek());
  }
}
