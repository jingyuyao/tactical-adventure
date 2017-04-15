package com.jingyuyao.tactical.view.world.component;

import com.badlogic.ashley.core.ComponentMapper;
import com.google.inject.Guice;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ComponentModuleTest {

  @Inject
  private ComponentMapper<CharacterComponent> characterComponentComponentMapper;
  @Inject
  private ComponentMapper<Frame> frameComponentMapper;
  @Inject
  private ComponentMapper<LoopAnimation> loopAnimationComponentMapper;
  @Inject
  private ComponentMapper<Moving> movingComponentMapper;
  @Inject
  private ComponentMapper<PlayerComponent> playerComponentComponentMapper;
  @Inject
  private ComponentMapper<Position> positionComponentMapper;
  @Inject
  private ComponentMapper<Remove> removeComponentMapper;
  @Inject
  private ComponentMapper<SingleAnimation> singleAnimationComponentMapper;

  @Test
  public void can_create_module() {
    Guice.createInjector(new ComponentModule()).injectMembers(this);
  }
}