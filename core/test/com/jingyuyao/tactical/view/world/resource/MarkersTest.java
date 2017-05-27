package com.jingyuyao.tactical.view.world.resource;

import static com.google.common.truth.Truth.assertThat;
import static com.jingyuyao.tactical.view.world.resource.Markers.MARKER_BUNDLE;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.jingyuyao.tactical.model.resource.StringKey;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkersTest {

  @Mock
  private TextureAtlas textureAtlas;
  @Mock
  private TextureFactory textureFactory;
  @Mock
  private AtlasRegion atlasRegion;
  @Mock
  private WorldTexture worldTexture;

  private Map<StringKey, WorldTexture> textureCache;
  private Markers markers;

  @Before
  public void setUp() {
    textureCache = new HashMap<>();
    markers = new Markers(textureAtlas, textureFactory, textureCache);
  }

  @Test
  public void get_highlight() {
    when(textureAtlas.findRegion("texture/ui/marking/highlight")).thenReturn(atlasRegion);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    WorldTexture texture = markers.getHighlight();

    assertThat(texture).isSameAs(worldTexture);
    assertThat(textureCache).containsExactly(MARKER_BUNDLE.get("highlight"), worldTexture);
  }

  @Test
  public void get_activate() {
    when(textureAtlas.findRegion("texture/ui/marking/activated")).thenReturn(atlasRegion);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    WorldTexture texture = markers.getActivated();

    assertThat(texture).isSameAs(worldTexture);
    assertThat(textureCache).containsExactly(MARKER_BUNDLE.get("activated"), worldTexture);
  }

  @Test
  public void get_move() {
    when(textureAtlas.findRegion("texture/ui/marking/move")).thenReturn(atlasRegion);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    WorldTexture texture = markers.getMove();

    assertThat(texture).isSameAs(worldTexture);
    assertThat(textureCache).containsExactly(MARKER_BUNDLE.get("move"), worldTexture);
  }

  @Test
  public void get_target_select() {
    when(textureAtlas.findRegion("texture/ui/marking/target_select")).thenReturn(atlasRegion);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    WorldTexture texture = markers.getTargetSelect();

    assertThat(texture).isSameAs(worldTexture);
    assertThat(textureCache).containsExactly(MARKER_BUNDLE.get("target_select"), worldTexture);
  }

  @Test
  public void get_attack() {
    when(textureAtlas.findRegion("texture/ui/marking/attack")).thenReturn(atlasRegion);
    when(textureFactory.create(atlasRegion)).thenReturn(worldTexture);

    WorldTexture texture = markers.getAttack();

    assertThat(texture).isSameAs(worldTexture);
    assertThat(textureCache).containsExactly(MARKER_BUNDLE.get("attack"), worldTexture);
  }
}