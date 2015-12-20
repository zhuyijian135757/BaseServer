package net.flyingfat.common.serialization.protocol.meta;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DefaultMsgCode2TypeMetainfo
  implements MsgCode2TypeMetainfo
{
  private Map<Integer, Class<?>> codes = new HashMap();
  
  public Class<?> find(int value)
  {
    return (Class)this.codes.get(Integer.valueOf(value));
  }
  
  public void add(int tag, Class<?> type)
  {
    this.codes.put(Integer.valueOf(tag), type);
  }
  
  public Map<Integer, String> getAllMetainfo()
  {
    Map<Integer, String> ret = new HashMap();
    for (Map.Entry<Integer, Class<?>> entry : this.codes.entrySet()) {
      ret.put(entry.getKey(), ((Class)entry.getValue()).toString());
    }
    return ret;
  }
}
