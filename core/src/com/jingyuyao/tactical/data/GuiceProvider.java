package com.jingyuyao.tactical.data;

import com.google.gson.InstanceCreator;
import java.lang.reflect.Type;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * An {@link InstanceCreator} that gets the instance from Guice. This allows the created object
 * to receive injections before the data is loaded by {@link com.google.gson.Gson}.
 */
class GuiceProvider<T> implements InstanceCreator<T> {

  private final Provider<T> provider;

  @Inject
  GuiceProvider(Provider<T> provider) {
    this.provider = provider;
  }

  @Override
  public T createInstance(Type type) {
    return provider.get();
  }
}
