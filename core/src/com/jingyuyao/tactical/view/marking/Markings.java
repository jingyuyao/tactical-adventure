package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.marking.MarkingModule.InProgressAnimationsMap;
import com.jingyuyao.tactical.view.marking.MarkingModule.MarkedActors;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import java.util.List;
import java.util.Map.Entry;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Markings {

  private final Batch batch;
  private final Markers markers;
  private final Multimap<WorldActor, SingleAnimation> animationsMap;
  private final List<WorldActor> markedActors;
  private Actor highlightedActor;
  private Actor activatedActor;

  @Inject
  Markings(
      Batch batch,
      Markers markers,
      @InProgressAnimationsMap Multimap<WorldActor, SingleAnimation> animationsMap,
      @MarkedActors List<WorldActor> markedActors) {
    this.batch = batch;
    this.markers = markers;
    this.animationsMap = animationsMap;
    this.markedActors = markedActors;
  }

  public void draw() {
    batch.begin();
    markers.getHighlight().draw(batch, highlightedActor);
    markers.getActivated().draw(batch, activatedActor);
    for (Entry<WorldActor, SingleAnimation> entry : animationsMap.entries()) {
      WorldTexture worldTexture = entry.getValue().getCurrentFrame();
      worldTexture.draw(batch, entry.getKey());
    }
    batch.end();
  }

  void highlight(Actor actor) {
    highlightedActor = actor;
  }

  void activate(Actor actor) {
    activatedActor = actor;
  }

  void addSingleAnimation(final WorldActor actor, final SingleAnimation singleAnimation) {
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

  void mark(WorldActor actor, WorldTexture worldTexture) {
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
