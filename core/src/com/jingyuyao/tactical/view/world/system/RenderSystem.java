package com.jingyuyao.tactical.view.world.system;

import static com.jingyuyao.tactical.view.world.WorldModule.WorldViewport;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderSystem extends SortedIteratingSystem {

  private final Batch batch;
  private final Viewport viewport;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Frame> frameMapper;

  @Inject
  RenderSystem(
      Batch batch,
      @WorldViewport Viewport viewport,
      ComponentMapper<Position> positionMapper,
      ComponentMapper<Frame> frameMapper) {
    super(Family.all(Position.class, Frame.class).get(), new ZComparator(positionMapper));
    this.batch = batch;
    this.viewport = viewport;
    this.positionMapper = positionMapper;
    this.frameMapper = frameMapper;
    this.priority = SystemPriority.RENDER;
  }

  @Override
  public void update(float deltaTime) {
    Camera camera = viewport.getCamera();
    camera.update();
    batch.begin();
    batch.setProjectionMatrix(camera.combined);
    super.update(deltaTime);
    batch.end();
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionMapper.get(entity);
    Frame frame = frameMapper.get(entity);
    Optional<WorldTexture> textureOptional = frame.getTexture();
    if (textureOptional.isPresent()) {
      batch.setColor(frame.getColor());
      textureOptional.get().draw(batch, position.getX(), position.getY());
      batch.setColor(Color.WHITE);
    }
    for (WorldTexture texture : frame.getOverlays()) {
      texture.draw(batch, position.getX(), position.getY());
    }
  }

  private static class ZComparator implements Comparator<Entity> {

    private final ComponentMapper<Position> positionMapper;

    private ZComparator(ComponentMapper<Position> positionMapper) {
      this.positionMapper = positionMapper;
    }

    @Override
    public int compare(Entity e1, Entity e2) {
      return (int) Math.signum(positionMapper.get(e1).getZ() - positionMapper.get(e2).getZ());
    }
  }
}
