package com.jingyuyao.tactical.view.world.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.base.Optional;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.component.Frame;
import com.jingyuyao.tactical.view.world.component.Position;
import com.jingyuyao.tactical.view.world.resource.WorldTexture;
import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class RenderSystem extends SortedIteratingSystem {

  private final Batch batch;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Frame> frameMapper;

  @Inject
  RenderSystem(
      Batch batch,
      ComponentMapper<Position> positionMapper,
      ComponentMapper<Frame> frameMapper) {
    super(
        Family.all(Position.class, Frame.class).get(),
        new ZComparator(positionMapper),
        SystemPriority.RENDER);
    this.batch = batch;
    this.positionMapper = positionMapper;
    this.frameMapper = frameMapper;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    Position position = positionMapper.get(entity);
    Frame frame = frameMapper.get(entity);
    Optional<Direction> direction = frame.direction();
    float x = position.getX();
    float y = position.getY();
    Optional<WorldTexture> textureOptional = frame.texture();
    if (textureOptional.isPresent()) {
      batch.setColor(frame.getColor());
      if (direction.isPresent()) {
        textureOptional.get().draw(batch, x, y, direction.get());
      } else {
        textureOptional.get().draw(batch, x, y);
      }
      batch.setColor(Color.WHITE);
    }
    for (WorldTexture texture : frame.getOverlays()) {
      if (direction.isPresent()) {
        texture.draw(batch, x, y, direction.get());
      } else {
        texture.draw(batch, x, y);
      }
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
