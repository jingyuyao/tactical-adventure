package com.jingyuyao.tactical.view.resource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.jingyuyao.tactical.view.actor.ActorConfig;
import com.jingyuyao.tactical.view.world.WorldConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WorldTextureTest {

  private static int TEXTURE_SIZE = 10;
  private static int TILE_SIZE = 5;
  private static float ACTOR_SIZE = 1f;

  @Mock
  private TextureRegion textureRegion;
  @Mock
  private WorldConfig worldConfig;
  @Mock
  private ActorConfig actorConfig;
  @Mock
  private Batch batch;
  @Mock
  private Actor actor;

  private WorldTexture worldTexture;

  @Test
  public void draw() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_SIZE);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig, actorConfig);
    worldTexture.draw(batch, 2f, 3f);

    verify(batch).draw(textureRegion, 1.5f, 2.5f, 2 * ACTOR_SIZE, 2 * ACTOR_SIZE);
  }

  @Test
  public void draw_actor() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_SIZE);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);
    when(actor.getX()).thenReturn(2f);
    when(actor.getY()).thenReturn(3f);

    worldTexture = new WorldTexture(textureRegion, worldConfig, actorConfig);
    worldTexture.draw(batch, actor);

    verify(batch).draw(textureRegion, 1.5f, 2.5f, 2 * ACTOR_SIZE, 2 * ACTOR_SIZE);
  }

  @Test
  public void draw_actor_null() {
    when(textureRegion.getRegionWidth()).thenReturn(TEXTURE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_SIZE);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig, actorConfig);
    worldTexture.draw(batch, null);

    verifyZeroInteractions(batch);
  }

  @Test
  public void draw_same_size() {
    when(textureRegion.getRegionWidth()).thenReturn(TILE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TILE_SIZE);
    when(worldConfig.getTileSize()).thenReturn(TILE_SIZE);
    when(actorConfig.getActorWorldSize()).thenReturn(ACTOR_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig, actorConfig);
    worldTexture.draw(batch, 2f, 3f);

    verify(batch).draw(textureRegion, 2f, 3f, ACTOR_SIZE, ACTOR_SIZE);
  }

  @Test(expected = IllegalArgumentException.class)
  public void irregular_texture() {
    when(textureRegion.getRegionWidth()).thenReturn(TILE_SIZE);
    when(textureRegion.getRegionHeight()).thenReturn(TEXTURE_SIZE);

    worldTexture = new WorldTexture(textureRegion, worldConfig, actorConfig);
  }
}