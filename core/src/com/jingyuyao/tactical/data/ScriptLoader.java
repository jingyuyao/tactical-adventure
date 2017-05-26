package com.jingyuyao.tactical.data;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.DeactivateGroup;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class ScriptLoader {

  private final DataConfig dataConfig;
  private final Files files;
  private final JsonLoader jsonLoader;
  private final DialogueLoader dialogueLoader;

  @Inject
  ScriptLoader(
      DataConfig dataConfig,
      Files files,
      JsonLoader jsonLoader,
      DialogueLoader dialogueLoader) {
    this.dataConfig = dataConfig;
    this.files = files;
    this.jsonLoader = jsonLoader;
    this.dialogueLoader = dialogueLoader;
  }

  Script load(int level) {
    FileHandle fileHandle = files.internal(dataConfig.getLevelScriptFileName(level));
    LevelScript levelScript = jsonLoader.deserialize(fileHandle.reader(), LevelScript.class);
    List<Condition> winConditions = levelScript.getWinConditions();
    List<Condition> loseConditions = levelScript.getLoseConditions();
    Map<Condition, List<Dialogue>> dialogues = dialogueLoader.getDialogues(level);
    Map<Condition, ActivateGroup> groupActivations = levelScript.getGroupActivations();
    Map<Condition, DeactivateGroup> groupDeactivations = levelScript.getGroupDeactivations();

    return new Script(
        winConditions, loseConditions, dialogues, groupActivations, groupDeactivations);
  }
}
