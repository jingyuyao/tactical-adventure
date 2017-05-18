package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.google.common.base.Optional;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ListMultimap;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.LevelTrigger;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ScriptLoaderTest {

  @Mock
  private DataConfig dataConfig;
  @Mock
  private DialogueLoader dialogueLoader;
  @Mock
  private LevelDataLoader levelDataLoader;
  @Mock
  private GameDataManager gameDataManager;
  @Mock
  private LevelScript levelScript;
  @Mock
  private GameScript gameScript;
  @Mock
  private Dialogue dialogue1;
  @Mock
  private Dialogue dialogue2;
  @Mock
  private Dialogue dialogue3;
  @Mock
  private Dialogue dialogue4;
  @Mock
  private Turn start1;
  @Mock
  private Turn end1;
  @Mock
  private Turn start3;

  private ScriptLoader scriptLoader;

  @Before
  public void setUp() {
    scriptLoader = new ScriptLoader(dataConfig, dialogueLoader, levelDataLoader, gameDataManager);
  }

  @Test
  public void load() {
    ResourceKeyBundle bundle = new ResourceKeyBundle("test/test");
    ResourceKey name1 = bundle.get("name1");
    ResourceKey name2 = bundle.get("name2");
    ResourceKey name3 = bundle.get("name3");
    ListMultimap<Turn, Dialogue> levelDialogues = ArrayListMultimap.create();
    levelDialogues.put(start1, dialogue1);
    levelDialogues.put(end1, dialogue2);
    ListMultimap<ResourceKey, Dialogue> deathDialogues = ArrayListMultimap.create();
    deathDialogues.put(name1, dialogue3);
    deathDialogues.put(name3, dialogue4);
    Map<Turn, LevelTrigger> levelTriggers =
        ImmutableMap.of(start1, LevelTrigger.WON, start3, LevelTrigger.LOST);
    List<String> keepAlive = ImmutableList.of("name1", "name2");

    when(levelDataLoader.loadScript(2)).thenReturn(levelScript);
    when(levelScript.getLevelTriggers()).thenReturn(levelTriggers);
    when(dialogueLoader.getLevelDialogues(2)).thenReturn(levelDialogues);
    when(dialogueLoader.getDeathDialogues()).thenReturn(deathDialogues);
    when(dataConfig.getPersonNameBundle()).thenReturn(bundle);
    when(gameDataManager.loadScript()).thenReturn(gameScript);
    when(gameScript.getKeepAlive()).thenReturn(keepAlive);

    Script script = scriptLoader.load(2);

    Optional<ScriptActions> start1Actions = script.turnScript(start1);
    assertThat(start1Actions).isPresent();
    assertThat(start1Actions.get().getDialogues()).containsExactly(dialogue1);
    assertThat(start1Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.WON);

    Optional<ScriptActions> end1Actions = script.turnScript(end1);
    assertThat(end1Actions).isPresent();
    assertThat(end1Actions.get().getDialogues()).containsExactly(dialogue2);
    assertThat(end1Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.NONE);

    Optional<ScriptActions> start3Actions = script.turnScript(start3);
    assertThat(start3Actions).isPresent();
    assertThat(start3Actions.get().getDialogues()).isEmpty();
    assertThat(start3Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.LOST);

    Optional<ScriptActions> name1Actions = script.deathScript(name1);
    assertThat(name1Actions).isPresent();
    assertThat(name1Actions.get().getDialogues()).containsExactly(dialogue3);
    assertThat(name1Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.LOST);

    Optional<ScriptActions> name2Actions = script.deathScript(name2);
    assertThat(name2Actions).isPresent();
    assertThat(name2Actions.get().getDialogues()).isEmpty();
    assertThat(name2Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.LOST);

    Optional<ScriptActions> name3Actions = script.deathScript(name3);
    assertThat(name3Actions).isPresent();
    assertThat(name3Actions.get().getDialogues()).containsExactly(dialogue4);
    assertThat(name3Actions.get().getLevelTrigger()).isSameAs(LevelTrigger.NONE);
  }
}