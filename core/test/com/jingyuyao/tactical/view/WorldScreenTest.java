package com.jingyuyao.tactical.view;

import static org.mockito.Mockito.verify;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jingyuyao.tactical.controller.WorldController;
import com.jingyuyao.tactical.view.marking.Markings;
import com.jingyuyao.tactical.view.resource.AnimationTime;
import com.jingyuyao.tactical.view.ui.UI;
import com.jingyuyao.tactical.view.world.World;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldScreenTest {

  @Mock
  private Batch batch;
  @Mock
  private World world;
  @Mock
  private Markings markings;
  @Mock
  private UI ui;
  @Mock
  private AnimationTime animationTime;
  @Mock
  private WorldController worldController;
  @Mock
  private GL20 gl20;

  private WorldScreen worldScreen;

  @Before
  public void setUp() {
    worldScreen = new WorldScreen(batch, world, markings, ui, animationTime, worldController);
    Gdx.gl = gl20;
  }

  @Test
  public void show() {
    worldScreen.show();

    verify(worldController).receiveInput();
  }

  @Test
  public void hide() {
    worldScreen.hide();

    verify(worldController).stopReceivingInput();
  }

  @Test
  public void render() {
    worldScreen.render(10f);

    InOrder inOrder = Mockito.inOrder(world, markings, ui, animationTime, gl20);
    inOrder.verify(ui).act(10f);
    inOrder.verify(markings).act(10f);
    inOrder.verify(world).act(10f);
    inOrder.verify(animationTime).advanceStateTime(10f);
    inOrder.verify(gl20).glClear(GL20.GL_COLOR_BUFFER_BIT);
    inOrder.verify(world).draw();
    inOrder.verify(markings).draw();
    inOrder.verify(ui).draw();
  }

  @Test
  public void resize() {
    worldScreen.resize(20, 30);

    verify(world).resize(20, 30);
    verify(ui).resize(20, 30);
  }

  @Test
  public void dispose() {
    worldScreen.dispose();

    verify(world).dispose();
    verify(ui).dispose();
    verify(batch).dispose();
  }
}