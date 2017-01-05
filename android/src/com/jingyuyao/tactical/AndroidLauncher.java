package com.jingyuyao.tactical;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jingyuyao.tactical.TacticalAdventure;

public class AndroidLauncher extends AndroidApplication {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
    // Saves battery
    config.useAccelerometer = false;
    config.useCompass = false;
    initialize(new TacticalAdventure(), config);
  }
}
