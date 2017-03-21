package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.model.map.Terrains;
import com.jingyuyao.tactical.model.state.SelectionHandler;
import com.jingyuyao.tactical.view.ui.UIModule.UIStage;
import com.jingyuyao.tactical.view.world.WorldModule.WorldStage;
import com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ControllerModuleTest {

  @Bind
  @Mock
  private SelectionHandler selectionHandler;
  @Bind
  @Mock
  private Terrains terrains;
  @Bind
  @Mock
  @WorldStage
  private Stage worldStage;
  @Bind
  @Mock
  @UIStage
  private Stage uiStage;
  @Bind
  @Mock
  @WorldViewport
  private Viewport worldViewport;
  @Mock
  private MapObject mapObject;

  @Inject
  private WorldController worldController;
  @Inject
  private ControllerFactory controllerFactory;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new ControllerModule()).injectMembers(this);
    controllerFactory.create(mapObject);
  }
}