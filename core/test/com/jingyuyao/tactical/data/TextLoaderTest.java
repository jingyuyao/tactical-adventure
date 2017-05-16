package com.jingyuyao.tactical.data;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.I18NBundle;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.jingyuyao.tactical.MockGameModule;
import com.jingyuyao.tactical.model.resource.ResourceKey;
import com.jingyuyao.tactical.model.resource.ResourceKeyBundle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * All tests require working directory to be in android/assets
 */
@RunWith(MockitoJUnitRunner.class)
public class TextLoaderTest {

  private static final String TEST_PATH = "i18n/Test";
  private static final ResourceKeyBundle TEST_BUNDLE = new ResourceKeyBundle(TEST_PATH);

  @Mock
  private AssetManager assetManager;

  @Inject
  private Files files;

  private TextLoader textLoader;

  @Before
  public void setUp() {
    Guice.createInjector(new MockGameModule()).injectMembers(this);
    textLoader = new TextLoader(assetManager);
    // Again, why the fuck is this a static property...
    assertThat(I18NBundle.getExceptionOnMissingKey()).isFalse();
  }

  @Test
  public void get_key_no_bundle_no_args() {
    ResourceKey resourceKey = TEST_BUNDLE.get("test1");
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(TEST_PATH));
    when(assetManager.get(TEST_PATH, I18NBundle.class))
        .thenThrow(new GdxRuntimeException("not loaded")).thenReturn(bundle);

    assertThat(textLoader.get(resourceKey)).isEqualTo("Test message");

    verify(assetManager).load(TEST_PATH, I18NBundle.class);
    verify(assetManager).finishLoadingAsset(TEST_PATH);
  }

  @Test
  public void get_key_no_bundle_with_args() {
    ResourceKey resourceKey = TEST_BUNDLE.get("test2", 246);
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(TEST_PATH));
    when(assetManager.get(TEST_PATH, I18NBundle.class))
        .thenThrow(new GdxRuntimeException("not loaded")).thenReturn(bundle);

    assertThat(textLoader.get(resourceKey)).isEqualTo("Test 246");

    verify(assetManager).load(TEST_PATH, I18NBundle.class);
    verify(assetManager).finishLoadingAsset(TEST_PATH);
  }

  @Test
  public void get_key_has_bundle() {
    ResourceKey resourceKey = TEST_BUNDLE.get("test2", 246);
    // can't mock this since I18NBundle#get() is final...
    I18NBundle bundle = I18NBundle.createBundle(files.internal(TEST_PATH));
    when(assetManager.get(TEST_PATH, I18NBundle.class)).thenReturn(bundle);

    assertThat(textLoader.get(resourceKey)).isEqualTo("Test 246");

    verify(assetManager, never()).load(TEST_PATH, I18NBundle.class);
    verify(assetManager, never()).finishLoadingAsset(TEST_PATH);
  }
}