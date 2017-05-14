package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.script.TurnScript;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.inject.Inject;
import javax.inject.Singleton;

// TODO: test me
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
    Map<Integer, TurnScript> turnScriptMap = new HashMap<>();
    for (Properties dialogues : getDialogueProperties(level).asSet()) {
      List<DialogueKey> keys = new ArrayList<>(dialogues.size());
      for (String rawKey : dialogues.stringPropertyNames()) {
        keys.add(new DialogueKey(rawKey));
      }
      Collections.sort(keys);
      Multimap<Integer, Dialogue> turnStartDialogues = ArrayListMultimap.create();
      Multimap<Integer, Dialogue> turnEndDialogues = ArrayListMultimap.create();
      MessageBundle bundle = new MessageBundle(dataConfig.getLevelDialogueBundle(level));
      for (DialogueKey key : keys) {
        Dialogue dialogue = key.toDialogue(bundle);
        if (key.isStart) {
          turnStartDialogues.put(key.turn, dialogue);
        } else {
          turnEndDialogues.put(key.turn, dialogue);
        }
      }
      for (Integer turn : Sets.union(turnStartDialogues.keySet(), turnEndDialogues.keySet())) {
        List<Dialogue> startDialogues = new ArrayList<>();
        List<Dialogue> endDialogues = new ArrayList<>();
        if (turnStartDialogues.containsKey(turn)) {
          startDialogues.addAll(turnStartDialogues.get(turn));
        }
        if (turnEndDialogues.containsKey(turn)) {
          endDialogues.addAll(turnEndDialogues.get(turn));
        }
        ScriptActions startActions = new ScriptActions(startDialogues);
        ScriptActions endActions = new ScriptActions(endDialogues);
        turnScriptMap.put(turn, new TurnScript(startActions, endActions));
      }
    }
    return new Script(turnScriptMap);
  }

  private Optional<Properties> getDialogueProperties(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getDefaultLevelDialogueFileName(level));
    if (!fileHandle.exists()) {
      return Optional.absent();
    }
    Properties properties = new Properties();
    try {
      properties.load(fileHandle.read());
    } catch (IOException e) {
      throw new IllegalArgumentException("error while reading dialogue property file.");
    }
    return Optional.of(properties);
  }

  private static class DialogueKey implements Comparable<DialogueKey> {

    private final int turn;
    private final boolean isStart;
    private final String characterNameKey;
    private final int index;
    private final String rawKey;

    private DialogueKey(String rawKey) {
      List<String> split = Splitter.on("-").splitToList(rawKey);
      Preconditions.checkArgument(split.size() == 4);
      this.rawKey = rawKey;
      this.turn = Integer.valueOf(split.get(0));
      this.isStart = split.get(1).equals("S");
      this.characterNameKey = split.get(2);
      this.index = Integer.valueOf(split.get(3));
    }

    private Dialogue toDialogue(MessageBundle bundle) {
      return new Dialogue(characterNameKey, bundle.get(rawKey));
    }

    @Override
    public int compareTo(DialogueKey other) {
      return index - other.index;
    }
  }
}
