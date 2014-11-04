package net.flyingfat.common.lang;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapUtil
{
  private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);
  
  public static Map objectToMap(Object object)
  {
    if (object == null) {
      return new HashMap();
    }
    if ((object instanceof Map)) {
      return (Map)object;
    }
    Map map = new HashMap();
    



    map.put("value", object);
    
    Field[] fields = object.getClass().getDeclaredFields();
    for (Field field : fields)
    {
      String fieldName = field.getName();
      field.setAccessible(true);
      try
      {
        map.put(fieldName, field.get(object));
      }
      catch (Exception e)
      {
        logger.error("objectToMap error: ", e);
      }
    }
    return map;
  }
  
  public static void populate(Object bean, Map parameters)
  {
    for (Iterator i = parameters.entrySet().iterator(); i.hasNext();)
    {
      Map.Entry entry = (Map.Entry)i.next();
      Object key = entry.getKey();
      Object value = entry.getValue();
      if (key != null)
      {
        String strKey;
        if (!(key instanceof String)) {
          strKey = key.toString();
        } else {
          strKey = (String)key;
        }
        FieldUtil.setFieldValue(bean, strKey, value);
      }
    }
  }
}
