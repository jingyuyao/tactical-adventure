package com.jingyuyao.tactical.view.marking;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.view.world.World;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingModuleTest {

  @Bind
  @Mock
  private AssetManager assetManager;
  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private Batch batch;
  @Mock
  private Texture texture;

  @Inject
  private Markings markings;
  @Inject
  private MarkingsSubscriber markingsSubscriber;

  @Before
  public void setUp() {
    when(assetManager.get(anyString(), eq(Texture.class))).thenReturn(texture);
  }

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new MarkingModule()).injectMembers(this);
  }
}