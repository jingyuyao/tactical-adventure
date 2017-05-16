package com.jingyuyao.tactical.tools;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerRunner {

  private static final String INPUT_DIR = "android/assets_raw/drawable/";
  private static final String OUTPUT_DIR = "android/assets/drawable/";
  private static final String FILE_NAME = "texture";

  public static void main(String[] args) {
    TexturePacker.process(INPUT_DIR, OUTPUT_DIR, FILE_NAME);
  }
}
