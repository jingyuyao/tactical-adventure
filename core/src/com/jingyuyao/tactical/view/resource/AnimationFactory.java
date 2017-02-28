package com.jingyuyao.tactical.view.resource;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

interface AnimationFactory {

  LoopAnimation createLoop(int fps, Array<? extends TextureRegion> regions);
}
