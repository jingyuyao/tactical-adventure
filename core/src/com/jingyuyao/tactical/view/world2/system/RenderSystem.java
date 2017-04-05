package com.jingyuyao.tactical.view.world2.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world2.WorldModule.WorldViewport;
import com.jingyuyao.tactical.view.world2.component.Position;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class RenderSystem extends EntitySystem {

  private final Batch batch;
  private final Viewport viewport;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<WorldTexture> worldTextureMapper;
  private ImmutableArray<Entity> entities;

  @Inject
  RenderSystem(
      Batch batch,
      @WorldViewport Viewport viewport,
      ComponentMapper<Position> positionMapper,
      ComponentMapper<WorldTexture> worldTextureMapper) {
    this.batch = batch;
    this.viewport = viewport;
    this.positionMapper = positionMapper;
    this.worldTextureMapper = worldTextureMapper;
    this.priority = 999; // render goes last
  }

  @Override
  public void addedToEngine(Engine engine) {
    entities = engine.getEntitiesFor(Family.all(Position.class, WorldTexture.class).get());
  }

  @Override
  public void update(float deltaTime) {
    Camera camera = viewport.getCamera();
    camera.update();

    batch.begin();
    batch.setProjectionMatrix(camera.combined);

    for (Entity entity : entities) {
      Position position = positionMapper.get(entity);
      WorldTexture worldTexture = worldTextureMapper.get(entity);
      // TODO: add area and use it in draw
      worldTexture.draw(batch, position.getX(), position.getY());
    }
    batch.end();
  }
}
