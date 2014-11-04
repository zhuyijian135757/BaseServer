package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;

public abstract interface Field2Desc
{
  public abstract ByteFieldDesc genDesc(Field paramField);
}
