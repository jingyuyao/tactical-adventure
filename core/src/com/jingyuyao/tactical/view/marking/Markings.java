package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkedActors;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkingsActor;
import com.jingyuyao.tactical.view.util.ViewUtil;
import com.jingyuyao.tactical.view.world.World;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markings {

  private final Batch batch;
  private final World world;
  private final MarkerSprites markerSprites;
  private final Actor actionActor;
  private final List<WorldActor<?>> markedActors;
  private WorldActor highlightedActor;
  private WorldActor activatedActor;

  @Inject
  Markings(
      Batch batch,
      World world,
      MarkerSprites markerSprites,
      @MarkingsActor Actor actionActor,
      @MarkedActors List<WorldActor<?>> markedActors) {
    this.batch = batch;
    this.world = world;
    this.markerSprites = markerSprites;
    this.actionActor = actionActor;
    this.markedActors = markedActors;
  }

  public void act(float delta) {
    actionActor.act(delta);
  }

  public void draw() {
    batch.begin();
    if (highlightedActor != null) {
      ViewUtil.draw(batch, markerSprites.getHighlight(), highlightedActor);
    }
    if (activatedActor != null) {
      ViewUtil.draw(batch, markerSprites.getActivated(), activatedActor);
    }
    batch.end();
  }

  void highlight(MapObject object) {
    highlightedActor = world.get(object);
  }

  void activate(MapObject object) {
    activatedActor = world.get(object);
  }

  void addAction(Action action) {
    actionActor.addAction(action);
  }

  void mark(MapObject object, Sprite sprite) {
    WorldActor actor = world.get(object);
    actor.addMarker(sprite);
    markedActors.add(actor);
  }

  void clearMarked() {
    for (WorldActor actor : markedActors) {
      actor.clearMarkers();
    }
    markedActors.clear();
  }
}
