package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    // looks a bit verbose right now because we eventually want to add more than just dialogues
    // to the scripts
    Map<Turn, ScriptActions> turnScripts = new HashMap<>();
    ListMultimap<Turn, Dialogue> levelDialogues = getLevelDialogues(level);
    for (Turn turn : levelDialogues.keySet()) {
      turnScripts.put(turn, new ScriptActions(levelDialogues.get(turn)));
    }
    Map<Message, ScriptActions> deathScripts = new HashMap<>();
    ListMultimap<Message, Dialogue> deathDialogues = getDeathDialogues();
    for (Message name : deathDialogues.keySet()) {
      deathScripts.put(name, new ScriptActions(deathDialogues.get(name)));
    }
    return new Script(turnScripts, deathScripts);
  }

  private ListMultimap<Message, Dialogue> getDeathDialogues() {
    ListMultimap<Message, Dialogue> dialogueMap = ArrayListMultimap.create();
    MessageBundle bundle = dataConfig.getDeathDialogueBundle();
    Optional<Properties> dialogueProperties = getProperties(bundle);
    if (dialogueProperties.isPresent()) {
      for (String nameKey : dialogueProperties.get().stringPropertyNames()) {
        // supports only one death dialogue per person
        dialogueMap.put(getName(nameKey), create(nameKey, bundle.get(nameKey)));
      }
    } else {
      throw new RuntimeException("death dialogue properties does not exists");
    }
    return dialogueMap;
  }

  private ListMultimap<Turn, Dialogue> getLevelDialogues(int level) {
    ListMultimap<Turn, Dialogue> dialogueMap = ArrayListMultimap.create();
    MessageBundle bundle = dataConfig.getLevelDialogueBundle(level);
    for (Properties dialoguesProperties : getProperties(bundle).asSet()) {
      List<DialogueKey> dialogueKeys = new ArrayList<>(dialoguesProperties.size());
      for (String rawKey : dialoguesProperties.stringPropertyNames()) {
        dialogueKeys.add(new DialogueKey(rawKey));
      }

      Collections.sort(dialogueKeys);

      for (DialogueKey key : dialogueKeys) {
        dialogueMap.put(key.turn, create(key.nameKey, bundle.get(key.rawKey)));
      }
    }
    return dialogueMap;
  }

  /**
   * Return the default properties file for a given {@link MessageBundle}
   */
  private Optional<Properties> getProperties(MessageBundle bundle) {
    FileHandle fileHandle = files.internal(bundle.getPathWithExtensions());
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

  private Dialogue create(String nameKey, Message message) {
    return new Dialogue(getName(nameKey), message);
  }

  private Message getName(String nameKey) {
    return dataConfig.getPersonNameBundle().get(nameKey);
  }

  private static class DialogueKey implements Comparable<DialogueKey> {

    private final String rawKey;
    private final Turn turn;
    private final String nameKey;
    private final int index;

    private DialogueKey(String rawKey) {
      List<String> split = Splitter.on("-").splitToList(rawKey);
      Preconditions.checkArgument(split.size() == 4);
      this.rawKey = rawKey;
      this.turn = new Turn(Integer.valueOf(split.get(0)), TurnStage.valueOf(split.get(1)));
      this.nameKey = split.get(2);
      this.index = Integer.valueOf(split.get(3));
    }

    @Override
    public int compareTo(DialogueKey other) {
      int turnDiff = turn.compareTo(other.turn);
      return turnDiff == 0 ? index - other.index : turnDiff;
    }
  }
}
