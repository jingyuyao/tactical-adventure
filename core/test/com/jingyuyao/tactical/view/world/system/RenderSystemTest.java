package com.jingyuyao.tactical.view.world.system;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.Colors;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RenderSystemTest {

  @Mock
  private Batch batch;
  @Mock
  private Viewport viewport;
  @Mock
  private Camera camera;
  @Mock
  private WorldTexture worldTexture;
  @Mock
  private WorldTexture worldTexture2;
  @Mock
  private WorldTexture overlay1;
  @Mock
  private WorldTexture overlay2;

  private PooledEngine engine;

  @Before
  public void setUp() {
    engine = new PooledEngine();
    RenderSystem renderSystem =
        new RenderSystem(
            batch,
            viewport,
            ComponentMapper.getFor(Position.class),
            ComponentMapper.getFor(Frame.class));
    assertThat(renderSystem.priority).isEqualTo(SystemPriority.RENDER);
    engine.addSystem(renderSystem);
  }

  @Test
  public void update() {
    when(viewport.getCamera()).thenReturn(camera);

    Entity entity1 = engine.createEntity();
    Frame frame1 = engine.createComponent(Frame.class);
    Position position1 = engine.createComponent(Position.class);
    frame1.setTexture(worldTexture);
    frame1.addOverlay(overlay1);
    frame1.addOverlay(overlay2);
    frame1.setColor(Colors.RED_500);
    position1.setX(10f);
    position1.setY(11f);
    position1.setZ(1);
    entity1.add(frame1).add(position1);

    Entity entity2 = engine.createEntity();
    Frame frame2 = engine.createComponent(Frame.class);
    Position position2 = engine.createComponent(Position.class);
    frame2.setTexture(worldTexture2);
    position2.setX(20f);
    position2.setY(21f);
    position2.setZ(0);
    entity2.add(frame2).add(position2);

    engine.addEntity(entity1);
    engine.addEntity(entity2);

    engine.update(5f);

    InOrder inOrder =
        Mockito.inOrder(batch, camera, worldTexture, worldTexture2, overlay1, overlay2);
    inOrder.verify(camera).update();
    inOrder.verify(batch).begin();
    inOrder.verify(batch).setProjectionMatrix(camera.combined);
    // draw entity 2
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(worldTexture2).draw(batch, 20f, 21f);
    inOrder.verify(batch).setColor(Color.WHITE);
    // draw entity 1
    inOrder.verify(batch).setColor(Colors.RED_500);
    inOrder.verify(worldTexture).draw(batch, 10f, 11f);
    inOrder.verify(batch).setColor(Color.WHITE);
    inOrder.verify(overlay1).draw(batch, 10f, 11f);
    inOrder.verify(overlay2).draw(batch, 10f, 11f);
    inOrder.verify(batch).end();
  }
}