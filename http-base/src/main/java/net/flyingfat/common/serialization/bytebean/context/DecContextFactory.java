package net.flyingfat.common.serialization.bytebean.context;

import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public abstract interface DecContextFactory
{
  public abstract DecContext createDecContext(byte[] paramArrayOfByte, Class<?> paramClass, Object paramObject, ByteFieldDesc paramByteFieldDesc);
}
