package com.jingyuyao.tactical.view.world2.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.SortedIteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world2.WorldModule.WorldViewport;
import com.jingyuyao.tactical.view.world2.component.Position;
import java.util.Comparator;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderSystem extends SortedIteratingSystem {

  private final Batch batch;
  private final Viewport viewport;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<WorldTexture> worldTextureMapper;

  @Inject
  RenderSystem(
      Batch batch,
      @WorldViewport Viewport viewport,
      ComponentMapper<Position> positionMapper,
      ComponentMapper<WorldTexture> worldTextureMapper) {
    super(Family.all(Position.class, WorldTexture.class).get(), new ZComparator(positionMapper));
    this.batch = batch;
    this.viewport = viewport;
    this.positionMapper = positionMapper;
    this.worldTextureMapper = worldTextureMapper;
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
    WorldTexture worldTexture = worldTextureMapper.get(entity);
    // TODO: add area and use it in draw
    worldTexture.draw(batch, position.getX(), position.getY());
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
