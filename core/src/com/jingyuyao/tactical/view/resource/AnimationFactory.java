package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

interface AnimationFactory {

  LoopAnimation create(int fps, Array<? extends TextureRegion> regions);
}
