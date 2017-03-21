package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.marking.MarkingModule.InProgressAnimationsMap;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkedActors;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.World;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markings {

  private final Batch batch;
  private final World world;
  private final Markers markers;
  private final Multimap<WorldActor<?>, SingleAnimation> animationsMap;
  private final List<WorldActor<?>> markedActors;
  private WorldActor highlightedActor;
  private WorldActor activatedActor;

  @Inject
  Markings(
      Batch batch,
      World world,
      Markers markers,
      @InProgressAnimationsMap Multimap<WorldActor<?>, SingleAnimation> animationsMap,
      @MarkedActors List<WorldActor<?>> markedActors) {
    this.batch = batch;
    this.world = world;
    this.markers = markers;
    this.animationsMap = animationsMap;
    this.markedActors = markedActors;
  }

  public void draw() {
    batch.begin();
    markers.getHighlight().draw(batch, highlightedActor);
    markers.getActivated().draw(batch, activatedActor);
    for (Entry<WorldActor<?>, SingleAnimation> entry : animationsMap.entries()) {
      WorldTexture worldTexture = entry.getValue().getCurrentFrame();
      worldTexture.draw(batch, entry.getKey());
    }
    batch.end();
  }

  void highlight(MapObject object) {
    highlightedActor = world.get(object);
  }

  void activate(MapObject object) {
    activatedActor = world.get(object);
  }

  void addSingleAnimation(MapObject object, final SingleAnimation singleAnimation) {
    final WorldActor<?> actor = world.get(object);
    animationsMap.put(actor, singleAnimation);
    Futures.addCallback(singleAnimation.getFuture(), new FutureCallback<Void>() {
      @Override
      public void onSuccess(Void result) {
        animationsMap.remove(actor, singleAnimation);
      }

      @Override
      public void onFailure(Throwable t) {

      }
    });
  }

  void mark(MapObject object, WorldTexture worldTexture) {
    WorldActor actor = world.get(object);
    actor.addMarker(worldTexture);
    markedActors.add(actor);
  }

  void clearMarked() {
    for (WorldActor actor : markedActors) {
      actor.clearMarkers();
    }
    markedActors.clear();
  }
}
