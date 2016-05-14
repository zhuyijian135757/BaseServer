package net.flyingfat.common.lang;

import java.util.Map;

public abstract interface Propertyable
{
  public abstract Object getProperty(String paramString);
  
  public abstract Map<String, Object> getProperties();
  
  public abstract void setProperty(String paramString, Object paramObject);
  
  public abstract void setProperties(Map<String, Object> paramMap);
}
