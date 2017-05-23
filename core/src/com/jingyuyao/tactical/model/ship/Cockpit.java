package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.person.Pilot;
import java.util.List;

class Cockpit {

  private final List<Pilot> pilots;

  Cockpit(List<Pilot> pilots) {
    this.pilots = pilots;
  }

  ImmutableList<Pilot> getPilots() {
    return ImmutableList.copyOf(pilots);
  }
}
