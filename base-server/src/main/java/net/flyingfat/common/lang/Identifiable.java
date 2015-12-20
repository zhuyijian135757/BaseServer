package net.flyingfat.common.lang;

import java.util.UUID;

public abstract interface Identifiable
{
  public abstract void setIdentification(UUID paramUUID);
  
  public abstract UUID getIdentification();
  
  public abstract void setReserved(int paramInt);
  
  public abstract int getReserved();
}
