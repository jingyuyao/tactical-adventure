package com.jingyuyao.tactical.view.marking;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
import com.jingyuyao.tactical.view.resource.WorldTexture;
import com.jingyuyao.tactical.view.world.World;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingsTest {

  @Mock
  private Batch batch;
  @Mock
  private World world;
  @Mock
  private Markers markers;
  @Mock
  private List<WorldActor<?>> markedActors;
  @Mock
  private MapObject mapObject;
  @Mock
  private MapObject mapObject2;
  @Mock
  private WorldActor<?> worldActor;
  @Mock
  private WorldActor<?> highlightActor;
  @Mock
  private WorldActor<?> activatedActor;
  @Mock
  private WorldTexture highlightTexture;
  @Mock
  private WorldTexture activatedTexture;
  @Mock
  private SingleAnimation singleAnimation;
  @Mock
  private WorldTexture animationFrame;

  private Multimap<WorldActor<?>, SingleAnimation> animationsMap;
  private Markings markings;

  @Before
  public void setUp() {
    animationsMap = HashMultimap.create();
    markings = new Markings(batch, world, markers, animationsMap, markedActors);
  }

  @Test
  public void add_single_animation() {
    ListenableFuture<Void> future = SettableFuture.create();
    when(singleAnimation.getFuture()).thenReturn(future);
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(worldActor);

    markings.addSingleAnimation(mapObject, singleAnimation);

    assertThat(animationsMap).containsEntry(worldActor, singleAnimation);
  }

  @Test
  public void add_single_animation_finished() {
    SettableFuture<Void> future = SettableFuture.create();
    when(singleAnimation.getFuture()).thenReturn(future);
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(worldActor);

    markings.addSingleAnimation(mapObject, singleAnimation);

    assertThat(animationsMap).containsEntry(worldActor, singleAnimation);

    future.set(null);

    assertThat(animationsMap).isEmpty();
  }

  @Test
  public void draw_highlight_and_activate_and_animation() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);
    Mockito.<WorldActor<?>>when(world.get(mapObject2)).thenReturn(activatedActor);
    when(markers.getHighlight()).thenReturn(highlightTexture);
    when(markers.getActivated()).thenReturn(activatedTexture);
    when(singleAnimation.getCurrentFrame()).thenReturn(animationFrame);
    animationsMap.put(worldActor, singleAnimation);

    markings.highlight(mapObject);
    markings.activate(mapObject2);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, animationFrame, highlightTexture, activatedTexture);
    inOrder.verify(batch).begin();
    inOrder.verify(highlightTexture).draw(batch, highlightActor);
    inOrder.verify(activatedTexture).draw(batch, activatedActor);
    inOrder.verify(animationFrame).draw(batch, worldActor);
    inOrder.verify(batch).end();
  }

  @Test
  public void mark() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);

    markings.mark(mapObject, highlightTexture);

    verify(highlightActor).addMarker(highlightTexture);
    verify(markedActors).add(highlightActor);
  }

  @Test
  public void clear_marked() {
    when(markedActors.iterator())
        .thenReturn(ImmutableList.of(highlightActor, activatedActor).iterator());

    markings.clearMarked();

    verify(highlightActor).clearMarkers();
    verify(activatedActor).clearMarkers();
    verify(markedActors).clear();
  }
}