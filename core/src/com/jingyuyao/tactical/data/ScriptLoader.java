package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.TurnScript;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DataConfig dataConfig;
  private final Files files;

  @Inject
  ScriptLoader(DataConfig dataConfig, Files files) {
    this.dataConfig = dataConfig;
    this.files = files;
  }

  Script load(int level) {
    Map<Turn, TurnScript> turnScriptMap = new HashMap<>();
    for (Entry<Turn, Collection<Dialogue>> entry : getDialogues(level).asMap().entrySet()) {
      turnScriptMap.put(entry.getKey(), new TurnScript((List<Dialogue>) entry.getValue()));
    }
    return new Script(turnScriptMap);
  }

  private ArrayListMultimap<Turn, Dialogue> getDialogues(int level) {
    ArrayListMultimap<Turn, Dialogue> dialogueMap = ArrayListMultimap.create();
    for (Properties dialoguesProperties : getDialogueProperties(level).asSet()) {
      List<DialogueKey> dialogueKeys = new ArrayList<>(dialoguesProperties.size());
      for (String rawKey : dialoguesProperties.stringPropertyNames()) {
        dialogueKeys.add(new DialogueKey(rawKey));
      }

      Collections.sort(dialogueKeys);

      final MessageBundle bundle = new MessageBundle(dataConfig.getLevelDialogueBundle(level));
      for (DialogueKey key : dialogueKeys) {
        dialogueMap.put(key.turn, key.toDialogue(bundle));
      }
    }
    return dialogueMap;
  }

  private Optional<Properties> getDialogueProperties(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getDefaultLevelDialogueFileName(level));
    if (fileHandle.exists()) {
      Properties properties = new Properties();
      try {
        InputStream stream = fileHandle.read();
        properties.load(stream);
        stream.close();
      } catch (IOException e) {
        throw new RuntimeException("error while reading dialogue property file.");
      }
      return Optional.of(properties);
    }
    return Optional.absent();
  }

  private static class DialogueKey implements Comparable<DialogueKey> {

    private final String rawKey;
    private final Turn turn;
    private final String characterNameKey;
    private final int index;

    private DialogueKey(String rawKey) {
      List<String> split = Splitter.on("-").splitToList(rawKey);
      Preconditions.checkArgument(split.size() == 4);
      this.rawKey = rawKey;
      this.turn = new Turn(Integer.valueOf(split.get(0)), TurnStage.valueOf(split.get(1)));
      this.characterNameKey = split.get(2);
      this.index = Integer.valueOf(split.get(3));
    }

    private Dialogue toDialogue(MessageBundle bundle) {
      return new Dialogue(characterNameKey, bundle.get(rawKey));
    }

    @Override
    public int compareTo(DialogueKey other) {
      int turnDiff = turn.compareTo(other.turn);
      return turnDiff == 0 ? index - other.index : turnDiff;
    }
  }
}
