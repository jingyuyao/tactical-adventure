package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.google.common.collect.ListMultimap;
import com.google.inject.Guice;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import com.jingyuyao.tactical.model.script.Dialogue;
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

  private static final ResourceKeyBundle LEVEL_DIALOGUE = new ResourceKeyBundle(
      "i18n/TestLevelDialogue");
  private static final ResourceKeyBundle DEATH_DIALOGUE = new ResourceKeyBundle(
      "i18n/TestDeathDialogue");
  private static final ResourceKeyBundle NAME = new ResourceKeyBundle("i18n/TestShipName");

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
  public void get_level_dialogues() {
    ListMultimap<Turn, Dialogue> levelDialogues = dialogueLoader.getLevelDialogues(2);

    List<Dialogue> start1Dialogues = levelDialogues.get(new Turn(1, TurnStage.START));
    assertThat(start1Dialogues).hasSize(2);
    assertThat(start1Dialogues.get(0).getName()).isEqualTo(NAME.get("first"));
    assertThat(start1Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-first-0"));
    assertThat(start1Dialogues.get(1).getName()).isEqualTo(NAME.get("second"));
    assertThat(start1Dialogues.get(1).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-START-second-1"));

    List<Dialogue> end1Dialogues = levelDialogues.get(new Turn(1, TurnStage.END));
    assertThat(end1Dialogues).hasSize(2);
    assertThat(end1Dialogues.get(0).getName()).isEqualTo(NAME.get("third"));
    assertThat(end1Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-END-third-0"));
    assertThat(end1Dialogues.get(1).getName()).isEqualTo(NAME.get("fourth"));
    assertThat(end1Dialogues.get(1).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("1-END-fourth-1"));

    assertThat(levelDialogues.get(new Turn(2, TurnStage.START))).isEmpty();

    List<Dialogue> start3Dialogues = levelDialogues.get(new Turn(3, TurnStage.START));
    assertThat(start3Dialogues).hasSize(1);
    assertThat(start3Dialogues.get(0).getName()).isEqualTo(NAME.get("fifth"));
    assertThat(start3Dialogues.get(0).getText())
        .isEqualTo(LEVEL_DIALOGUE.get("3-START-fifth-0"));

    assertThat(levelDialogues.get(new Turn(9001, TurnStage.START))).isEmpty();
  }

  @Test
  public void get_death_dialogues() {
    ListMultimap<ResourceKey, Dialogue> deathDialogues = dialogueLoader.getDeathDialogues();

    List<Dialogue> deadDialogues = deathDialogues.get(NAME.get("dead"));
    assertThat(deadDialogues).hasSize(1);
    assertThat(deadDialogues.get(0).getName()).isEqualTo(NAME.get("dead"));
    assertThat(deadDialogues.get(0).getText()).isEqualTo(DEATH_DIALOGUE.get("dead"));

    List<Dialogue> dead2Dialogues = deathDialogues.get(NAME.get("dead2"));
    assertThat(dead2Dialogues).hasSize(1);
    assertThat(dead2Dialogues.get(0).getName()).isEqualTo(NAME.get("dead2"));
    assertThat(dead2Dialogues.get(0).getText()).isEqualTo(DEATH_DIALOGUE.get("dead2"));

    assertThat(deathDialogues.get(NAME.get("never-gonna-give-you-up"))).isEmpty();
  }
}