package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.google.common.collect.ListMultimap;
import com.google.inject.Guice;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.resource.KeyBundle;
import com.jingyuyao.tactical.model.script.Condition;
import com.jingyuyao.tactical.model.script.Dialogue;
import com.jingyuyao.tactical.model.script.OnDeath;
import com.jingyuyao.tactical.model.script.OnTurn;
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
public class DialogueLoaderTest {

  private static final KeyBundle LEVEL_DIALOGUE = new KeyBundle(
      "i18n/TestLevelDialogue");
  private static final KeyBundle DEATH_DIALOGUE = new KeyBundle(
      "i18n/TestDeathDialogue");
  private static final KeyBundle NAME = new KeyBundle("i18n/TestShipName");

  @Mock
  private DataConfig dataConfig;

  @Inject
  private Files files;

  private DialogueLoader dialogueLoader;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    dialogueLoader = new DialogueLoader(dataConfig, files);

    when(dataConfig.getLevelDialogueBundle(2)).thenReturn(LEVEL_DIALOGUE);
    when(dataConfig.getDeathDialogueBundle()).thenReturn(DEATH_DIALOGUE);
    when(dataConfig.getPersonNameBundle()).thenReturn(NAME);
  }

  @Test
  public void get_dialogues() {
    ListMultimap<Condition, Dialogue> dialogues = dialogueLoader.getDialogues(2);

    List<Dialogue> start1Dialogues = dialogues.get(new OnTurn(new Turn(1, TurnStage.START)));
    assertThat(start1Dialogues).hasSize(2);
    assertThat(start1Dialogues.get(0).getName()).isEqualTo(NAME.get("first"));
    assertThat(start1Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-first-0"));
    assertThat(start1Dialogues.get(1).getName()).isEqualTo(NAME.get("second"));
    assertThat(start1Dialogues.get(1).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-second-1"));

    List<Dialogue> end1Dialogues = dialogues.get(new OnTurn(new Turn(1, TurnStage.END)));
    assertThat(end1Dialogues).hasSize(2);
    assertThat(end1Dialogues.get(0).getName()).isEqualTo(NAME.get("third"));
    assertThat(end1Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-END-third-0"));
    assertThat(end1Dialogues.get(1).getName()).isEqualTo(NAME.get("fourth"));
    assertThat(end1Dialogues.get(1).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-END-fourth-1"));

    assertThat(dialogues.get(new OnTurn(new Turn(2, TurnStage.START)))).isEmpty();

    List<Dialogue> start3Dialogues = dialogues.get(new OnTurn(new Turn(3, TurnStage.START)));
    assertThat(start3Dialogues).hasSize(1);
    assertThat(start3Dialogues.get(0).getName()).isEqualTo(NAME.get("fifth"));
    assertThat(start3Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("3-START-fifth-0"));

    assertThat(dialogues.get(new OnTurn(new Turn(9001, TurnStage.START)))).isEmpty();

    List<Dialogue> deadDialogues = dialogues.get(new OnDeath("dead"));
    assertThat(deadDialogues).hasSize(1);
    assertThat(deadDialogues.get(0).getName()).isEqualTo(NAME.get("dead"));
    assertThat(deadDialogues.get(0).getText()).isEqualTo(DEATH_DIALOGUE.get("dead"));

    List<Dialogue> dead2Dialogues = dialogues.get(new OnDeath("dead2"));
    assertThat(dead2Dialogues).hasSize(1);
    assertThat(dead2Dialogues.get(0).getName()).isEqualTo(NAME.get("dead2"));
    assertThat(dead2Dialogues.get(0).getText()).isEqualTo(DEATH_DIALOGUE.get("dead2"));

    assertThat(dialogues.get(new OnDeath("never-gonna-give-you-up"))).isEmpty();
  }
}