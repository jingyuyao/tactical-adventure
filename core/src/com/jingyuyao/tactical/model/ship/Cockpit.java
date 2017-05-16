package com.jingyuyao.tactical.model.ship;

import com.google.common.collect.ImmutableList;
import com.jingyuyao.tactical.model.person.Person;
import java.util.ArrayList;
import java.util.List;

class Cockpit {

  private List<Person> pilots = new ArrayList<>();

  ImmutableList<Person> getPilots() {
    return ImmutableList.copyOf(pilots);
  }
}
