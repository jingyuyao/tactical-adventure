package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.I18NBundle;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.i18n.Message;
import com.jingyuyao.tactical.model.i18n.MessageBundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * All tests require working directory to be in android/assets
 */
@RunWith(MockitoJUnitRunner.class)
public class MessageLoaderTest {

  @Mock
  private AssetManager assetManager;

  @Inject
  private Files files;

  private MessageLoader messageLoader;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    messageLoader = new MessageLoader(assetManager);
    // Again, why the fuck is this a static property...
    assertThat(I18NBundle.getExceptionOnMissingKey()).isFalse();
  }

  @Test
  public void all_bundle_exist() {
    for (MessageBundle messageBundle : MessageBundle.values()) {
      I18NBundle.createBundle(files.internal(messageBundle.getPath()));
    }
  }

  @Test
  public void get_message_no_bundle_no_args() {
    String itemBundlePath = MessageBundle.ITEM_NAME.getPath();
    Message message = MessageBundle.ITEM_NAME.get("testItem");
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(itemBundlePath));
    when(assetManager.isLoaded(itemBundlePath, I18NBundle.class)).thenReturn(false);
    when(assetManager.get(itemBundlePath, I18NBundle.class)).thenReturn(bundle);

    assertThat(messageLoader.get(message)).isEqualTo("Test Item");

    verify(assetManager).load(itemBundlePath, I18NBundle.class);
    verify(assetManager).finishLoadingAsset(itemBundlePath);
  }

  @Test
  public void get_message_no_bundle_with_args() {
    String itemBundlePath = MessageBundle.ITEM_NAME.getPath();
    Message message = MessageBundle.ITEM_NAME.get("testItem2", 246);
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(itemBundlePath));
    when(assetManager.isLoaded(itemBundlePath, I18NBundle.class)).thenReturn(false);
    when(assetManager.get(itemBundlePath, I18NBundle.class)).thenReturn(bundle);

    assertThat(messageLoader.get(message)).isEqualTo("Test 246");

    verify(assetManager).load(itemBundlePath, I18NBundle.class);
    verify(assetManager).finishLoadingAsset(itemBundlePath);
  }

  @Test
  public void get_message_has_bundle() {
    String itemBundlePath = MessageBundle.ITEM_NAME.getPath();
    Message message = MessageBundle.ITEM_NAME.get("testItem2", 246);
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(itemBundlePath));
    when(assetManager.isLoaded(itemBundlePath, I18NBundle.class)).thenReturn(true);
    when(assetManager.get(itemBundlePath, I18NBundle.class)).thenReturn(bundle);

    assertThat(messageLoader.get(message)).isEqualTo("Test 246");

    verify(assetManager, never()).load(itemBundlePath, I18NBundle.class);
    verify(assetManager, never()).finishLoadingAsset(itemBundlePath);
  }
}