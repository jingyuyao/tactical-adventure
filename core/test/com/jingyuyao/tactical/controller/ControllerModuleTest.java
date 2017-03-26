package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.Cell;
import com.jingyuyao.tactical.model.world.World;
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
  private Model model;
  @Bind
  @Mock
  private World world;
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
  private Cell cell;

  @Inject
  private WorldController worldController;
  @Inject
  private ControllerFactory controllerFactory;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new ControllerModule()).injectMembers(this);
    controllerFactory.create(cell);
  }
}