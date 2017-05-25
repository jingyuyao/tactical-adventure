package com.jingyuyao.tactical.model.ship;

import com.jingyuyao.tactical.model.person.Pilot;
import java.io.Serializable;
import java.util.List;

class Cockpit implements Serializable {

  private List<Pilot> pilots;

  Cockpit() {
  }

  List<Pilot> getPilots() {
    return pilots;
  }
}
