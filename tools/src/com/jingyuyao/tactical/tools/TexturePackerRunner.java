package com.jingyuyao.tactical.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerRunner {

  private static final String INPUT_DIR = "android/assets/unpacked/";
  private static final String OUTPUT_DIR = "android/assets/packed/";
  private static final String FILE_NAME = "textures";

  public static void main(String[] args) {
    TexturePacker.process(INPUT_DIR, OUTPUT_DIR, FILE_NAME);
  }
}
