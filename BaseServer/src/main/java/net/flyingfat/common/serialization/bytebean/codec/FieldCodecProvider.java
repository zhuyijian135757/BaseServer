package net.flyingfat.common.serialization.bytebean.codec;

public abstract interface FieldCodecProvider
{
  public abstract ByteFieldCodec getCodecOf(FieldCodecCategory paramFieldCodecCategory);
  
  public abstract ByteFieldCodec getCodecOf(Class<?> paramClass);
}
