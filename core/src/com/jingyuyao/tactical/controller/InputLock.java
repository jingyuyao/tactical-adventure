package com.jingyuyao.tactical.controller;

import javax.inject.Singleton;

/**
 * A non-blocking semaphore.
 */
@Singleton
public class InputLock {

  private int locks = 0;

  public void lock() {
    locks++;
  }

  public void unlock() {
    locks--;
  }

  public boolean isLocked() {
    return locks > 0;
  }
}
