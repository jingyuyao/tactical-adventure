package com.jingyuyao.tactical.model;

import com.google.inject.AbstractModule;
import com.google.inject.Stage;
import com.jingyuyao.tactical.model.character.CharacterModule;
import com.jingyuyao.tactical.model.common.CommonModule;
import com.jingyuyao.tactical.model.item.ItemModule;
import com.jingyuyao.tactical.model.map.MapModule;
import com.jingyuyao.tactical.model.retaliation.RetaliationModule;
import com.jingyuyao.tactical.model.state.StateModule;

public class ModelModule extends AbstractModule {

  @Override
  protected void configure() {
    if (!currentStage().equals(Stage.PRODUCTION)) {
      addError("ModelModule requires singleton pre-loading.");
    }
    install(new CommonModule());
    install(new MapModule());
    install(new CharacterModule());
    install(new ItemModule());
    install(new StateModule());
    install(new RetaliationModule());
  }
}
