package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.google.common.collect.ImmutableMap;
import com.jingyuyao.tactical.model.script.ActivateGroup;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.DeactivateGroup;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import java.io.Reader;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptLoaderTest {

  private static final String LEVEL_SCRIPT = "levels/1/script";

  @Mock
  private DataConfig dataConfig;
  @Mock
  private Files files;
  @Mock
  private InitLoader initLoader;
  @Mock
  private DialogueLoader dialogueLoader;
  @Mock
  private Condition condition1;
  @Mock
  private Condition condition2;
  @Mock
  private Condition condition3;
  @Mock
  private Condition condition4;
  @Mock
  private Condition condition5;
  @Mock
  private ActivateGroup activateGroup1;
  @Mock
  private DeactivateGroup deactivateGroup1;
  @Mock
  private Dialogue dialogue;
  @Mock
  private LevelScript levelScript;
  @Mock
  private FileHandle fileHandle;
  @Mock
  private Reader reader;

  private ScriptLoader scriptLoader;

  @Before
  public void setUp() {
    scriptLoader = new ScriptLoader(dataConfig, files, initLoader, dialogueLoader);
  }

  @Test
  public void load() {
    when(dataConfig.getLevelScriptFileName(2)).thenReturn(LEVEL_SCRIPT);
    when(files.internal(LEVEL_SCRIPT)).thenReturn(fileHandle);
    when(fileHandle.reader()).thenReturn(reader);
    when(initLoader.fromJson(reader, LevelScript.class)).thenReturn(levelScript);
    when(levelScript.getWinConditions()).thenReturn(Collections.singletonList(condition1));
    when(levelScript.getLoseConditions()).thenReturn(Collections.singletonList(condition2));
    when(levelScript.getGroupActivations())
        .thenReturn(ImmutableMap.of(condition4, activateGroup1));
    when(levelScript.getGroupDeactivations())
        .thenReturn(ImmutableMap.of(condition5, deactivateGroup1));
    when(dialogueLoader.getDialogues(2))
        .thenReturn(ImmutableMap.of(condition3, Collections.singletonList(dialogue)));

    Script script = scriptLoader.load(2);

    assertThat(script.getWinConditions()).containsExactly(condition1);
    assertThat(script.getLoseConditions()).containsExactly(condition2);
    assertThat(script.getGroupActivations()).containsExactly(condition4, activateGroup1);
    assertThat(script.getGroupDeactivations()).containsExactly(condition5, deactivateGroup1);
    assertThat(script.getDialogues()).containsKey(condition3);
    assertThat(script.getDialogues().get(condition3)).containsExactly(dialogue);
  }
}