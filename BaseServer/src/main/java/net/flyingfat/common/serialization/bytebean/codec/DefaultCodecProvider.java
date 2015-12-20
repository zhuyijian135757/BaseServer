package net.flyingfat.common.serialization.bytebean.codec;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultCodecProvider
  implements FieldCodecProvider
{
  private final Map<Class<?>, ByteFieldCodec> class2Codec = new HashMap();
  private final Map<FieldCodecCategory, ByteFieldCodec> category2Codec = new HashMap();
  
  public void setCodecs(Collection<ByteFieldCodec> codecs)
  {
    for (ByteFieldCodec codec : codecs) {
      addCodec(codec);
    }
  }
  
  public DefaultCodecProvider addCodec(ByteFieldCodec codec)
  {
    Class<?>[] classes = codec.getFieldType();
    if (null != classes) {
      for (Class<?> clazz : classes) {
        this.class2Codec.put(clazz, codec);
      }
    } else if (null != codec.getCategory()) {
      this.category2Codec.put(codec.getCategory(), codec);
    }
    return this;
  }
  
  public ByteFieldCodec getCodecOf(FieldCodecCategory type)
  {
    return (ByteFieldCodec)this.category2Codec.get(type);
  }
  
  public ByteFieldCodec getCodecOf(Class<?> clazz)
  {
    return (ByteFieldCodec)this.class2Codec.get(clazz);
  }
}
