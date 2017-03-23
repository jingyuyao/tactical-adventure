package com.jingyuyao.tactical.model;

import com.google.inject.Guice;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ModelModuleTest {

  @Inject
  private World world;

  @Test
  public void can_create_module() {
    Guice.createInjector(new ModelModule()).injectMembers(this);
  }
}
