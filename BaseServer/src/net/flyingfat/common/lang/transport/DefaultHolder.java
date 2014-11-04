package net.flyingfat.common.lang.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.flyingfat.common.lang.Holder;

public class DefaultHolder
  implements Holder
{
  private Map<Object, Object> map = new ConcurrentHashMap();
  
  public void put(Object key, Object value)
  {
    this.map.put(key, value);
  }
  
  public Object get(Object key)
  {
    return this.map.get(key);
  }
  
  public Object getAndRemove(Object key)
  {
    Object ret = this.map.get(key);
    this.map.remove(key);
    return ret;
  }
  
  public void remove(Object key)
  {
    this.map.remove(key);
  }
}
