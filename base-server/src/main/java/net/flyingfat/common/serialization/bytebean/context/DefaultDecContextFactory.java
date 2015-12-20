package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public class DefaultDecContextFactory
  implements DecContextFactory
{
  private FieldCodecProvider codecProvider;
  private NumberCodec numberCodec;
  
  public DecContext createDecContext(byte[] decBytes, Class<?> targetType, Object parent, ByteFieldDesc desc)
  {
    return new DefaultDecContext().setCodecProvider(this.codecProvider).setDecBytes(decBytes).setDecClass(targetType).setDecOwner(parent).setNumberCodec(this.numberCodec).setFieldDesc(desc).setDecContextFactory(this);
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
