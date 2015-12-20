package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public class DefaultEncContextFactory
  implements EncContextFactory
{
  private FieldCodecProvider codecProvider;
  private NumberCodec numberCodec;
  
  public EncContext createEncContext(Object encObject, Class<?> type, ByteFieldDesc desc)
  {
    return new DefaultEncContext().setCodecProvider(this.codecProvider).setEncClass(type).setEncObject(encObject).setNumberCodec(this.numberCodec).setFieldDesc(desc).setEncContextFactory(this);
  }
  
  public FieldCodecProvider getCodecProvider()
  {
    return this.codecProvider;
  }
  
  public void setCodecProvider(FieldCodecProvider codecProvider)
  {
    this.codecProvider = codecProvider;
  }
  
  public NumberCodec getNumberCodec()
  {
    return this.numberCodec;
  }
  
  public void setNumberCodec(NumberCodec numberCodec)
  {
    this.numberCodec = numberCodec;
  }
}
