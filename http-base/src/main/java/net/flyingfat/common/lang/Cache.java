package net.flyingfat.common.lang;

public abstract interface Cache<K, V>
{
  public abstract V get(K paramK);
  
  public abstract boolean put(K paramK, V paramV);
  
  public abstract boolean put(K paramK, V paramV, int paramInt);
  
  public abstract boolean update(K paramK, V paramV);
  
  public abstract boolean remove(K paramK);
  
  public abstract boolean clear();
  
  public abstract void destroy();
  
  public abstract boolean containsKey(K paramK);
  
  public abstract boolean flushAll();
  
  public abstract boolean isConnected();
}
