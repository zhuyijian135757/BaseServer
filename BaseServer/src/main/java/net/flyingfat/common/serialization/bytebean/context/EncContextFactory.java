package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public abstract interface EncContextFactory
{
  public abstract EncContext createEncContext(Object paramObject, Class<?> paramClass, ByteFieldDesc paramByteFieldDesc);
}
