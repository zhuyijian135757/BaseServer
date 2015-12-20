package net.flyingfat.common.lang.holder;

public abstract interface Holder
{
  public abstract void put(Object paramObject1, Object paramObject2);
  
  public abstract Object get(Object paramObject);
  
  public abstract Object getAndRemove(Object paramObject);
  
  public abstract void remove(Object paramObject);
  
  public abstract Integer size();
}
