package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.resource.StringKey;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.OnDeath;
import com.jingyuyao.tactical.model.script.OnTurn;
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
class DialogueLoader {

  private final DataConfig dataConfig;
  private final Files files;

  @Inject
  DialogueLoader(DataConfig dataConfig, Files files) {
    this.dataConfig = dataConfig;
    this.files = files;
  }

  Map<Condition, List<Dialogue>> getDialogues(int level) {
    Map<Condition, List<Dialogue>> dialogues = new HashMap<>();
    Map<Turn, List<Dialogue>> turnDialogues = getTurnDialogues(level);
    for (Turn turn : turnDialogues.keySet()) {
      dialogues.put(new OnTurn(turn), turnDialogues.get(turn));
    }
    Map<String, List<Dialogue>> deathDialogues = getDeathDialogues();
    for (String nameKey : deathDialogues.keySet()) {
      dialogues.put(new OnDeath(nameKey), deathDialogues.get(nameKey));
    }
    return dialogues;
  }

  private Map<Turn, List<Dialogue>> getTurnDialogues(int level) {
    Map<Turn, List<Dialogue>> dialogueMap = new HashMap<>();
    KeyBundle bundle = dataConfig.getLevelDialogueBundle(level);
    for (Properties dialoguesProperties : getProperties(bundle).asSet()) {
      List<DialogueKey> dialogueKeys = new ArrayList<>(dialoguesProperties.size());
      for (String rawKey : dialoguesProperties.stringPropertyNames()) {
        dialogueKeys.add(new DialogueKey(rawKey));
      }

      Collections.sort(dialogueKeys);

      for (DialogueKey key : dialogueKeys) {
        Dialogue dialogue = create(key.nameKey, bundle.get(key.rawKey));
        if (dialogueMap.containsKey(key.turn)) {
          dialogueMap.get(key.turn).add(dialogue);
        } else {
          dialogueMap.put(key.turn, new ArrayList<>(Collections.singletonList(dialogue)));
        }
      }
    }
    return dialogueMap;
  }

  private Map<String, List<Dialogue>> getDeathDialogues() {
    Map<String, List<Dialogue>> dialogueMap = new HashMap<>();
    KeyBundle bundle = dataConfig.getDeathDialogueBundle();
    Optional<Properties> dialogueProperties = getProperties(bundle);
    if (dialogueProperties.isPresent()) {
      for (String nameKey : dialogueProperties.get().stringPropertyNames()) {
        // supports only one death dialogue per person
        Dialogue dialogue = create(nameKey, bundle.get(nameKey));
        dialogueMap.put(nameKey, Collections.singletonList(dialogue));
      }
    } else {
      throw new RuntimeException("death dialogue properties does not exists");
    }
    return dialogueMap;
  }

  /**
   * Return the default properties file for a given {@link KeyBundle}
   */
  private Optional<Properties> getProperties(KeyBundle bundle) {
    FileHandle fileHandle = files.internal(bundle.getPathWithExtension());
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

  private Dialogue create(String nameKey, StringKey stringKey) {
    return new Dialogue(getName(nameKey), stringKey);
  }

  private StringKey getName(String nameKey) {
    return dataConfig.getPersonNameBundle().get(nameKey);
  }

  private static class DialogueKey implements Comparable<DialogueKey> {

    private static final Splitter SPLITTER = Splitter.on('-').trimResults().omitEmptyStrings();

    private final String rawKey;
    private final Turn turn;
    private final String nameKey;
    private final int index;

    private DialogueKey(String rawKey) {
      List<String> split = SPLITTER.splitToList(rawKey);
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
