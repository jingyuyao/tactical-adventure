package com.jingyuyao.tactical.view.marking;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.google.inject.Guice;
import com.google.inject.testing.fieldbinder.Bind;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import com.jingyuyao.tactical.view.resource.Animations;
import com.jingyuyao.tactical.view.resource.Markers;
import com.jingyuyao.tactical.view.world.World;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MarkingModuleTest {

  @Bind
  @Mock
  private World world;
  @Bind
  @Mock
  private Batch batch;
  @Bind
  @Mock
  private Animations animations;
  @Bind
  @Mock
  private Markers markers;

  @Inject
  private Markings markings;
  @Inject
  private MarkingsSubscriber markingsSubscriber;

  @Test
  public void can_create_module() {
    Guice.createInjector(BoundFieldModule.of(this), new MarkingModule()).injectMembers(this);
  }
}