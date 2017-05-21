package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Died;
import com.jingyuyao.tactical.model.script.OnTurn;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

  ListMultimap<Condition, Dialogue> getDialogues(int level) {
    ListMultimap<Condition, Dialogue> dialogues = ArrayListMultimap.create();
    ListMultimap<Turn, Dialogue> turnDialogues = getTurnDialogues(level);
    for (Turn turn : turnDialogues.keySet()) {
      dialogues.putAll(new OnTurn(turn), turnDialogues.get(turn));
    }
    ListMultimap<String, Dialogue> deathDialogues = getDeathDialogues();
    for (String nameKey : deathDialogues.keySet()) {
      dialogues.putAll(new Died(nameKey), deathDialogues.get(nameKey));
    }
    return dialogues;
  }

  private ListMultimap<Turn, Dialogue> getTurnDialogues(int level) {
    ListMultimap<Turn, Dialogue> dialogueMap = ArrayListMultimap.create();
    ResourceKeyBundle bundle = dataConfig.getLevelDialogueBundle(level);
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

  private ListMultimap<String, Dialogue> getDeathDialogues() {
    ListMultimap<String, Dialogue> dialogueMap = ArrayListMultimap.create();
    ResourceKeyBundle bundle = dataConfig.getDeathDialogueBundle();
    Optional<Properties> dialogueProperties = getProperties(bundle);
    if (dialogueProperties.isPresent()) {
      for (String nameKey : dialogueProperties.get().stringPropertyNames()) {
        // supports only one death dialogue per person
        dialogueMap.put(nameKey, create(nameKey, bundle.get(nameKey)));
      }
    } else {
      throw new RuntimeException("death dialogue properties does not exists");
    }
    return dialogueMap;
  }

  /**
   * Return the default properties file for a given {@link ResourceKeyBundle}
   */
  private Optional<Properties> getProperties(ResourceKeyBundle bundle) {
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

  private Dialogue create(String nameKey, ResourceKey resourceKey) {
    return new Dialogue(getName(nameKey), resourceKey);
  }

  private ResourceKey getName(String nameKey) {
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
