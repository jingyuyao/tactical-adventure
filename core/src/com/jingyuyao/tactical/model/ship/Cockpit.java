package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.person.Pilot;
import java.util.ArrayList;
import java.util.List;

class Cockpit {

  private List<Pilot> pilots = new ArrayList<>();

  ImmutableList<Pilot> getPilots() {
    return ImmutableList.copyOf(pilots);
  }
}
