package com.jingyuyao.tactical;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import javax.inject.Singleton;

public class MockGameModule extends AbstractModule {

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  AssetManager provideAssetManager() {
    AtlasRegion atlasRegion = mock(AtlasRegion.class);
    when(atlasRegion.getRegionHeight()).thenReturn(10);
    when(atlasRegion.getRegionWidth()).thenReturn(10);

    TextureAtlas textureAtlas = mock(TextureAtlas.class);
    when(textureAtlas.findRegion(anyString())).thenReturn(atlasRegion);

    AssetManager assetManager = mock(AssetManager.class);
    when(assetManager.get(anyString(), eq(TextureAtlas.class))).thenReturn(textureAtlas);
    return assetManager;
  }

  @Provides
  @Singleton
  Skin provideSkin() {
    return mock(Skin.class);
  }

  @Provides
  @Singleton
  Batch provideBatch() {
    return mock(Batch.class);
  }
}
