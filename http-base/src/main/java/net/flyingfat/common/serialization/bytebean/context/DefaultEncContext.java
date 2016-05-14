package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public class DefaultEncContext
  extends AbstractCodecContext
  implements EncContext
{
  private Object encObject;
  private EncContextFactory encContextFactory;
  
  public DefaultEncContext setCodecProvider(FieldCodecProvider codecProvider)
  {
    this.codecProvider = codecProvider;
    return this;
  }
  
  public DefaultEncContext setEncClass(Class<?> encClass)
  {
    this.targetType = encClass;
    return this;
  }
  
  public DefaultEncContext setFieldDesc(ByteFieldDesc desc)
  {
    this.fieldDesc = desc;
    return this;
  }
  
  public DefaultEncContext setNumberCodec(NumberCodec numberCodec)
  {
    this.numberCodec = numberCodec;
    return this;
  }
  
  public DefaultEncContext setEncObject(Object encObject)
  {
    this.encObject = encObject;
    return this;
  }
  
  public DefaultEncContext setEncContextFactory(EncContextFactory encContextFactory)
  {
    this.encContextFactory = encContextFactory;
    return this;
  }
  
  public Object getEncObject()
  {
    return this.encObject;
  }
  
  public Class<?> getEncClass()
  {
    return this.targetType;
  }
  
  public EncContextFactory getEncContextFactory()
  {
    return this.encContextFactory;
  }
}
