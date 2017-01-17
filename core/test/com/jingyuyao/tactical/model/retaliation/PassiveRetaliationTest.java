package com.jingyuyao.tactical.model.retaliation;

import com.jingyuyao.tactical.model.map.MovementFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PassiveRetaliationTest {

  @Mock
  private MovementFactory movementFactory;

  private PassiveRetaliation passiveRetaliation;

  @Before
  public void setUp() {
    passiveRetaliation = new PassiveRetaliation(movementFactory);
  }

  @Test
  public void run() {
    // TODO: stub
  }
}