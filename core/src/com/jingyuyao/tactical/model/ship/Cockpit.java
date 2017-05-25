package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.person.Pilot;
import java.io.Serializable;
import java.util.List;

class Cockpit implements Serializable {

  private List<Pilot> pilots;

  Cockpit() {
  }

  ImmutableList<Pilot> getPilots() {
    return ImmutableList.copyOf(pilots);
  }
}
