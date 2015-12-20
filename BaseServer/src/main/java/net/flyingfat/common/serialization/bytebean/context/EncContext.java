package net.flyingfat.common.serialization.bytebean.context;

public abstract interface EncContext
  extends FieldCodecContext
{
  public abstract Object getEncObject();
  
  public abstract Class<?> getEncClass();
  
  public abstract EncContextFactory getEncContextFactory();
}
