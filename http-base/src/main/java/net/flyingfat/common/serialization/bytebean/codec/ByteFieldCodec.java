package net.flyingfat.common.serialization.bytebean.codec;

import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

public abstract interface ByteFieldCodec
{
  public abstract FieldCodecCategory getCategory();
  
  public abstract Class<?>[] getFieldType();
  
  public abstract DecResult decode(DecContext paramDecContext);
  
  public abstract byte[] encode(EncContext paramEncContext);
}
