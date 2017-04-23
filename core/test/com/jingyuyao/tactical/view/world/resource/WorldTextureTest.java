package com.jingyuyao.tactical.view.world.resource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.jingyuyao.tactical.model.world.Direction;
import com.jingyuyao.tactical.view.world.WorldConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldTextureTest {

  private static final int TEXTURE_WIDTH = 10;
  private static final int TEXTURE_HEIGHT = 15;
  private static final int TILE_SIZE = 5;

  @Mock
  private TextureRegion textureRegion;
  @Mock
  private WorldConfig worldConfig;
  @Mock
  private Batch batch;

  private WorldTexture worldTexture;

  @Test
  public void draw() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_WIDTH);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_HEIGHT);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f);

    verify(batch).draw(textureRegion, 1.5f, 2f, 2f, 3f);
  }

  @Test
  public void draw_same_size() {
    when(textureRegion.getRegionWidth()).thenReturn(TILE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TILE_SIZE);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f);

    verify(batch).draw(textureRegion, 2f, 3f, 1f, 1f);
  }

  @Test
  public void draw_right() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_WIDTH);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_HEIGHT);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f, Direction.RIGHT);

    verify(batch).draw(textureRegion, 2f, 2f, 0.5f, 1.5f, 2f, 3f, 1f, 1f, 0f);
  }

  @Test
  public void draw_left() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_WIDTH);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_HEIGHT);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f, Direction.LEFT);

    verify(batch).draw(textureRegion, 2f, 2f, 0.5f, 1.5f, 2f, 3f, -1f, 1f, 0f);
  }

  @Test
  public void draw_up() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_WIDTH);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_HEIGHT);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f, Direction.UP);

    verify(batch).draw(textureRegion, 2f, 2f, 0.5f, 1.5f, 2f, 3f, 1f, 1f, 90f);
  }

  @Test
  public void draw_down() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_WIDTH);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_HEIGHT);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig);
    worldTexture.draw(batch, 2f, 3f, Direction.DOWN);

    verify(batch).draw(textureRegion, 2f, 2f, 0.5f, 1.5f, 2f, 3f, 1f, 1f, -90f);
  }
}