package com.jingyuyao.tactical.view.world;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.jingyuyao.tactical.model.ModelBusListener;
import com.jingyuyao.tactical.view.world.WorldViewModule.WorldEngine;
import com.jingyuyao.tactical.view.world.system.Systems;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@ModelBusListener
public class WorldView {

  private final Batch batch;
  private final PooledEngine engine;
  private final WorldCamera worldCamera;

  @Inject
  WorldView(
      Batch batch,
      @WorldEngine PooledEngine engine,
      WorldCamera worldCamera,
      Systems systems) {
    this.batch = batch;
    this.worldCamera = worldCamera;
    this.engine = engine;
    systems.addTo(engine);
  }

  public void update(float delta) {
    // tell openGL to use this viewport and calls camera.update()
    worldCamera.apply();
    // Render and update everything else
    batch.begin();
    batch.setProjectionMatrix(worldCamera.getProjectionMatrix());
    engine.update(delta);
    batch.end();
  }

  public void resize(int width, int height) {
    worldCamera.resize(width, height);
  }
}
