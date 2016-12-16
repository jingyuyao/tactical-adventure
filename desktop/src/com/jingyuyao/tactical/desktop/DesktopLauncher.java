package com.jingyuyao.tactical.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.jingyuyao.tactical.TacticalAdventure;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 25 * 32;
		config.height = 15 * 32;
		new LwjglApplication(new TacticalAdventure(), config);
	}
}
