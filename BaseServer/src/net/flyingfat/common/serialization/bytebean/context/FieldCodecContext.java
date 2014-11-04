package net.flyingfat.common.serialization.bytebean.context;

import java.lang.reflect.Field;

import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public abstract interface FieldCodecContext
  extends FieldCodecProvider
{
  public abstract ByteFieldDesc getFieldDesc();
  
  public abstract Field getField();
  
  public abstract NumberCodec getNumberCodec();
  
  public abstract int getByteSize();
}
