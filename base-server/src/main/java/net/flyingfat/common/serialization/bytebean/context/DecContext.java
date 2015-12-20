package net.flyingfat.common.serialization.bytebean.context;

public abstract interface DecContext
  extends FieldCodecContext
{
  public abstract Object getDecOwner();
  
  public abstract byte[] getDecBytes();
  
  public abstract Class<?> getDecClass();
  
  public abstract DecContextFactory getDecContextFactory();
}
