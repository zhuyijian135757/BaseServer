package net.flyingfat.common.lang.transport;

public abstract interface Sender
{
  public abstract void send(Object paramObject);
  
  public abstract void send(Object paramObject, Receiver paramReceiver);
}
