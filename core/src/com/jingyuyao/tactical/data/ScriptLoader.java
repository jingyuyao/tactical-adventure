package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.TurnScript;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
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
    Map<Turn, TurnScript> turnScriptMap = new HashMap<>();
    for (Properties dialoguesProperties : getDialogueProperties(level).asSet()) {
      Multimap<Turn, DialogueKey> dialogueKeys = ArrayListMultimap.create();
      for (String rawKey : dialoguesProperties.stringPropertyNames()) {
        DialogueKey key = new DialogueKey(rawKey);
        dialogueKeys.put(key.turn, key);
      }
      final MessageBundle bundle = new MessageBundle(dataConfig.getLevelDialogueBundle(level));
      for (Entry<Turn, Collection<DialogueKey>> entry : dialogueKeys.asMap().entrySet()) {
        List<DialogueKey> keys = (List<DialogueKey>) entry.getValue();
        // sort the keys based on the dialogue index
        Collections.sort(keys);
        List<Dialogue> dialogues = new ArrayList<>(keys.size());
        for (DialogueKey key : keys) {
          dialogues.add(key.toDialogue(bundle));
        }
        turnScriptMap.put(entry.getKey(), new TurnScript(dialogues));
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
      return index - other.index;
    }
  }
}
