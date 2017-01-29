package com.jingyuyao.tactical.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jingyuyao.tactical.TacticalAdventure;

public class DesktopLauncher {

  public static void main(String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 16 * 32 * 2;
    config.height = 9 * 32 * 2;
    config.backgroundFPS = -1; // don't render
    new LwjglApplication(new TacticalAdventure(), config);
  }
}
