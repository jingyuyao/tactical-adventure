package com.jingyuyao.tactical.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class KryoSerializer {

  private final Kryo kryo;

  @Inject
  KryoSerializer(Kryo kryo) {
    this.kryo = kryo;
  }

  /**
   * Deserialize an object from a {@link InputStream} then closes it. See {@link
   * Kryo#readObject(Input, Class)}.
   */
  <T> T deserialize(InputStream inputStream, Class<T> clazz) {
    Input input = new Input(inputStream);
    T result = kryo.readObject(input, clazz);
    input.close();
    return result;
  }

  /**
   * Serialize an object to a {@link OutputStream} then closes it. See {@link
   * Kryo#writeObject(Output, Object)}.
   */
  void serialize(Object object, OutputStream outputStream) {
    Output output = new Output(outputStream);
    kryo.writeObject(output, object);
    output.close();
  }
}
