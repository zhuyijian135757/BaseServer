package net.flyingfat.common.serialization.bytebean.codec.primitive;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecCategory;

public abstract class AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  public FieldCodecCategory getCategory()
  {
    return null;
  }
}
