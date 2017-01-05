package com.jingyuyao.tactical.model.mark;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jingyuyao.tactical.model.character.Character;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Singleton;

public class MarkModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MarkingFactory.class);
    bind(Markings.class);
  }

  @Provides
  @Singleton
  @com.jingyuyao.tactical.model.mark.Markings.InitialDangerAreas
  Map<Character, Marking> provideInitialDangerAreas() {
    return new HashMap<Character, Marking>();
  }
}
