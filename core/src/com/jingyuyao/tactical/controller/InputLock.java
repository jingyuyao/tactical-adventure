package com.jingyuyao.tactical.controller;

import javax.inject.Singleton;

@Singleton
public class InputLock {

  private boolean locked = false;

  public void lock() {
    locked = true;
  }

  public void unlock() {
    locked = false;
  }

  public boolean isLocked() {
    return locked;
  }
}
