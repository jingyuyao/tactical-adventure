package com.jingyuyao.tactical.controller;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.Model;
import com.jingyuyao.tactical.model.world.World;
import com.jingyuyao.tactical.view.ui.UIModule.UIStage;
import com.jingyuyao.tactical.view.world2.WorldModule.WorldViewport;
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
  @UIStage
  private Stage uiStage;
  @Bind
  @Mock
  @WorldViewport
  private Viewport worldViewport;

  @Inject
  private WorldController worldController;

  @Test
  public void can_create_module() {
    Guice.createInjector(
        BoundFieldModule.of(this), new MockGameModule(), new ControllerModule())
        .injectMembers(this);
  }
}