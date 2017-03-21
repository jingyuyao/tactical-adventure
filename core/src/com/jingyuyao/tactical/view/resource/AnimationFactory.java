package com.jingyuyao.tactical.view.resource;

interface AnimationFactory {

  LoopAnimation createLoop(int fps, WorldTexture[] worldTextures);

  SingleAnimation createSingle(int fps, WorldTexture[] worldTextures);
}
