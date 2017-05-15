package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.google.common.base.Optional;
import com.google.inject.Guice;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.Script;
import com.jingyuyao.tactical.model.script.ScriptActions;
import com.jingyuyao.tactical.model.state.Turn;
import com.jingyuyao.tactical.model.state.Turn.TurnStage;
import java.util.List;
import javax.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * All tests require working directory to be in android/assets
 */
@RunWith(MockitoJUnitRunner.class)
public class ScriptLoaderTest {

  private static final MessageBundle LEVEL_DIALOGUE = new MessageBundle("i18n/TestLevelDialogue");
  private static final MessageBundle DEATH_DIALOGUE = new MessageBundle("i18n/TestDeathDialogue");
  private static final MessageBundle NAME = new MessageBundle("i18n/TestCharacterName");

  @Mock
  private DataConfig dataConfig;

  @Inject
  private Files files;

  private ScriptLoader scriptLoader;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    scriptLoader = new ScriptLoader(dataConfig, files);
  }

  @Test
  public void load_level_dialogues() {
    when(dataConfig.getLevelDialogueBundle(2)).thenReturn(LEVEL_DIALOGUE);
    when(dataConfig.getDeathDialogueBundle()).thenReturn(DEATH_DIALOGUE);
    when(dataConfig.getCharacterNameBundle()).thenReturn(NAME);

    Script script = scriptLoader.load(2);

    Optional<ScriptActions> start1Script = script.turnScript(new Turn(1, TurnStage.START));
    assertThat(start1Script).isPresent();
    List<Dialogue> start1Dialogues = start1Script.get().getDialogues();
    assertThat(start1Dialogues).hasSize(2);
    assertThat(start1Dialogues.get(0).getName()).isEqualTo(NAME.get("first"));
    assertThat(start1Dialogues.get(0).getMessage())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-first-0"));
    assertThat(start1Dialogues.get(1).getName()).isEqualTo(NAME.get("second"));
    assertThat(start1Dialogues.get(1).getMessage())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-second-1"));

    Optional<ScriptActions> end1Script = script.turnScript(new Turn(1, TurnStage.END));
    assertThat(end1Script).isPresent();
    List<Dialogue> end1Dialogues = end1Script.get().getDialogues();
    assertThat(end1Dialogues).hasSize(2);
    assertThat(end1Dialogues.get(0).getName()).isEqualTo(NAME.get("third"));
    assertThat(end1Dialogues.get(0).getMessage()).isEqualTo(LEVEL_DIALOGUE.get("1-END-third-0"));
    assertThat(end1Dialogues.get(1).getName()).isEqualTo(NAME.get("fourth"));
    assertThat(end1Dialogues.get(1).getMessage()).isEqualTo(LEVEL_DIALOGUE.get("1-END-fourth-1"));

    Optional<ScriptActions> start2Script = script.turnScript(new Turn(2, TurnStage.START));
    assertThat(start2Script).isAbsent();

    Optional<ScriptActions> start3Script = script.turnScript(new Turn(3, TurnStage.START));
    assertThat(start3Script).isPresent();
    List<Dialogue> start3Dialogues = start3Script.get().getDialogues();
    assertThat(start3Dialogues).hasSize(1);
    assertThat(start3Dialogues.get(0).getName()).isEqualTo(NAME.get("fifth"));
    assertThat(start3Dialogues.get(0).getMessage())
        .isEqualTo(LEVEL_DIALOGUE.get("3-START-fifth-0"));

    assertThat(script.turnScript(new Turn(9001, TurnStage.START))).isAbsent();
  }

  @Test
  public void load_death_dialogues() {
    when(dataConfig.getLevelDialogueBundle(2)).thenReturn(LEVEL_DIALOGUE);
    when(dataConfig.getDeathDialogueBundle()).thenReturn(DEATH_DIALOGUE);
    when(dataConfig.getCharacterNameBundle()).thenReturn(NAME);

    Script script = scriptLoader.load(2);

    Optional<ScriptActions> dead = script.deathScript(NAME.get("dead"));
    assertThat(dead).isPresent();
    List<Dialogue> deadDialogues = dead.get().getDialogues();
    assertThat(deadDialogues).hasSize(1);
    assertThat(deadDialogues.get(0).getName()).isEqualTo(NAME.get("dead"));
    assertThat(deadDialogues.get(0).getMessage()).isEqualTo(DEATH_DIALOGUE.get("dead"));

    Optional<ScriptActions> dead2 = script.deathScript(NAME.get("dead2"));
    assertThat(dead2).isPresent();
    List<Dialogue> dead2Dialogues = dead2.get().getDialogues();
    assertThat(dead2Dialogues).hasSize(1);
    assertThat(dead2Dialogues.get(0).getName()).isEqualTo(NAME.get("dead2"));
    assertThat(dead2Dialogues.get(0).getMessage()).isEqualTo(DEATH_DIALOGUE.get("dead2"));

    assertThat(script.deathScript(NAME.get("never-gonna-give-you-up"))).isAbsent();
  }
}