package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public class DefaultDecContext
  extends AbstractCodecContext
  implements DecContext
{
  private byte[] decBytes;
  private Object decOwner;
  private DecContextFactory decContextFactory;
  
  public DefaultDecContext setCodecProvider(FieldCodecProvider codecProvider)
  {
    this.codecProvider = codecProvider;
    return this;
  }
  
  public DefaultDecContext setDecClass(Class<?> decClass)
  {
    this.targetType = decClass;
    return this;
  }
  
  public DefaultDecContext setDecBytes(byte[] decBytes)
  {
    this.decBytes = decBytes;
    return this;
  }
  
  public DefaultDecContext setFieldDesc(ByteFieldDesc desc)
  {
    this.fieldDesc = desc;
    return this;
  }
  
  public DefaultDecContext setDecOwner(Object decOwner)
  {
    this.decOwner = decOwner;
    return this;
  }
  
  public DefaultDecContext setNumberCodec(NumberCodec numberCodec)
  {
    this.numberCodec = numberCodec;
    return this;
  }
  
  public DefaultDecContext setDecContextFactory(DecContextFactory decContextFactory)
  {
    this.decContextFactory = decContextFactory;
    return this;
  }
  
  public Object getDecOwner()
  {
    return this.decOwner;
  }
  
  public byte[] getDecBytes()
  {
    return this.decBytes;
  }
  
  public Class<?> getDecClass()
  {
    return this.targetType;
  }
  
  public DecContextFactory getDecContextFactory()
  {
    return this.decContextFactory;
  }
}
