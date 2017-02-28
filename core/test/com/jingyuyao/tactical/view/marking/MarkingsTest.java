package com.jingyuyao.tactical.view.marking;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.jingyuyao.tactical.model.map.MapObject;
import com.jingyuyao.tactical.view.actor.WorldActor;
import com.jingyuyao.tactical.view.resource.MarkerSprites;
import com.jingyuyao.tactical.view.resource.SingleAnimation;
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
  private MarkerSprites markerSprites;
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
  private Sprite highlightSprite;
  @Mock
  private Sprite activatedSprite;
  @Mock
  private SingleAnimation singleAnimation;
  @Mock
  private TextureRegion textureRegion;

  private Multimap<WorldActor<?>, SingleAnimation> animationsMap;
  private Markings markings;

  @Before
  public void setUp() {
    animationsMap = HashMultimap.create();
    markings = new Markings(batch, world, markerSprites, animationsMap, markedActors);
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
  public void draw_nothing() {
    markings.draw();

    verify(batch).begin();
    verify(batch).end();
    verifyZeroInteractions(markerSprites);
  }

  @Test
  public void draw_highlight() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);
    when(markerSprites.getHighlight()).thenReturn(highlightSprite);

    markings.highlight(mapObject);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, highlightSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(highlightSprite).draw(batch);
    inOrder.verify(batch).end();
    verifyZeroInteractions(activatedSprite);
  }

  @Test
  public void draw_activated() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(activatedActor);
    when(markerSprites.getActivated()).thenReturn(activatedSprite);

    markings.activate(mapObject);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, activatedSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(activatedSprite).draw(batch);
    inOrder.verify(batch).end();
    verifyZeroInteractions(highlightSprite);
  }

  @Test
  public void draw_animations() {
    when(singleAnimation.getCurrentFrame()).thenReturn(textureRegion);
    animationsMap.put(worldActor, singleAnimation);

    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch);
    inOrder.verify(batch).begin();
    inOrder
        .verify(batch)
        .draw(
            textureRegion, worldActor.getX(), worldActor.getY(),
            worldActor.getWidth(), worldActor.getHeight());
    inOrder.verify(batch).end();
  }

  @Test
  public void draw_highlight_and_activate_and_animation() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);
    Mockito.<WorldActor<?>>when(world.get(mapObject2)).thenReturn(activatedActor);
    when(markerSprites.getHighlight()).thenReturn(highlightSprite);
    when(markerSprites.getActivated()).thenReturn(activatedSprite);
    when(singleAnimation.getCurrentFrame()).thenReturn(textureRegion);
    animationsMap.put(worldActor, singleAnimation);

    markings.highlight(mapObject);
    markings.activate(mapObject2);
    markings.draw();

    InOrder inOrder = Mockito.inOrder(batch, highlightSprite, activatedSprite);
    inOrder.verify(batch).begin();
    inOrder.verify(highlightSprite).draw(batch);
    inOrder.verify(activatedSprite).draw(batch);
    inOrder
        .verify(batch)
        .draw(
            textureRegion, worldActor.getX(), worldActor.getY(),
            worldActor.getWidth(), worldActor.getHeight());
    inOrder.verify(batch).end();
  }

  @Test
  public void mark() {
    Mockito.<WorldActor<?>>when(world.get(mapObject)).thenReturn(highlightActor);

    markings.mark(mapObject, highlightSprite);

    verify(highlightActor).addMarker(highlightSprite);
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