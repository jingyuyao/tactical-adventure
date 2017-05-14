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
import com.jingyuyao.tactical.model.script.TurnScript;
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

  private static final String PROPERTY_BUNDLE = "i18n/TestDialogue";
  private static final String PROPERTY_FILE = "i18n/TestDialogue.properties";

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
  public void load() {
    when(dataConfig.getLevelDialogueBundle(2)).thenReturn(PROPERTY_BUNDLE);
    when(dataConfig.getDefaultLevelDialogueFileName(2)).thenReturn(PROPERTY_FILE);

    Script script = scriptLoader.load(2);

    MessageBundle bundle = new MessageBundle(PROPERTY_BUNDLE);
    Optional<TurnScript> start1Script = script.turnScript(new Turn(1, TurnStage.START));
    assertThat(start1Script).isPresent();
    List<Dialogue> start1Dialogues = start1Script.get().getDialogues();
    assertThat(start1Dialogues).hasSize(2);
    assertThat(start1Dialogues.get(0).getCharacterNameKey()).isEqualTo("first");
    assertThat(start1Dialogues.get(0).getMessage()).isEqualTo(bundle.get("1-START-first-0"));
    assertThat(start1Dialogues.get(1).getCharacterNameKey()).isEqualTo("second");
    assertThat(start1Dialogues.get(1).getMessage()).isEqualTo(bundle.get("1-START-second-1"));
    Optional<TurnScript> end1Script = script.turnScript(new Turn(1, TurnStage.END));
    assertThat(end1Script).isPresent();
    List<Dialogue> end1Dialogues = end1Script.get().getDialogues();
    assertThat(end1Dialogues).hasSize(2);
    assertThat(end1Dialogues.get(0).getCharacterNameKey()).isEqualTo("third");
    assertThat(end1Dialogues.get(0).getMessage()).isEqualTo(bundle.get("1-END-third-0"));
    assertThat(end1Dialogues.get(1).getCharacterNameKey()).isEqualTo("fourth");
    assertThat(end1Dialogues.get(1).getMessage()).isEqualTo(bundle.get("1-END-fourth-1"));
    Optional<TurnScript> start2Script = script.turnScript(new Turn(2, TurnStage.START));
    assertThat(start2Script).isAbsent();
    Optional<TurnScript> start3Script = script.turnScript(new Turn(3, TurnStage.START));
    assertThat(start3Script).isPresent();
    List<Dialogue> start3Dialogues = start3Script.get().getDialogues();
    assertThat(start3Dialogues).hasSize(1);
    assertThat(start3Dialogues.get(0).getCharacterNameKey()).isEqualTo("fifth");
    assertThat(start3Dialogues.get(0).getMessage()).isEqualTo(bundle.get("3-START-fifth-0"));
  }
}