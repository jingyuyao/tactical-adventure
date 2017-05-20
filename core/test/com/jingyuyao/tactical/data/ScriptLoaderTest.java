package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.state.Turn;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptLoaderTest {

  @Mock
  private DialogueLoader dialogueLoader;
  @Mock
  private LevelDataLoader levelDataLoader;
  @Mock
  private LevelScript levelScript;
  @Mock
  private Condition condition1;
  @Mock
  private Condition condition2;
  @Mock
  private Condition condition3;

  private ScriptLoader scriptLoader;

  @Before
  public void setUp() {
    scriptLoader = new ScriptLoader(dialogueLoader, levelDataLoader);
  }

  @Test
  public void load() {
    ListMultimap<Turn, Dialogue> levelDialogues = ArrayListMultimap.create();
    ListMultimap<ResourceKey, Dialogue> deathDialogues = ArrayListMultimap.create();

    when(levelDataLoader.loadScript(2)).thenReturn(levelScript);
    when(levelScript.getLoseConditions()).thenReturn(ImmutableList.of(condition1, condition2));
    when(levelScript.getWinConditions()).thenReturn(ImmutableList.of(condition3));
    when(dialogueLoader.getLevelDialogues(2)).thenReturn(levelDialogues);
    when(dialogueLoader.getDeathDialogues()).thenReturn(deathDialogues);

    Script script = scriptLoader.load(2);

    assertThat(script.getWinConditions()).containsExactly(condition3);
    assertThat(script.getLoseConditions()).containsExactly(condition1, condition2);
    assertThat(script.getTurnDialogues()).isEqualTo(levelDialogues);
    assertThat(script.getDeathDialogues()).isEqualTo(deathDialogues);
  }
}