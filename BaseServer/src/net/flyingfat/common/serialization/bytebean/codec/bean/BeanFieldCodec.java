package net.flyingfat.common.serialization.bytebean.codec.bean;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;

public abstract interface BeanFieldCodec
  extends ByteFieldCodec
{
  public abstract int getStaticByteSize(Class<?> paramClass);
  
  public abstract DecContextFactory getDecContextFactory();
  
  public abstract EncContextFactory getEncContextFactory();
}
