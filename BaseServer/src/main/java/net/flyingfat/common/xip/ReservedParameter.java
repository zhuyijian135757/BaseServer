package net.flyingfat.common.xip;

import net.flyingfat.common.serialization.bytebean.ByteBean;
import net.flyingfat.common.serialization.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class ReservedParameter
  implements ByteBean
{
  @ByteField(index=0, description="参数key")
  private String key;
  @ByteField(index=1, description="参数值")
  private String value;
  
  public String getKey()
  {
    return this.key;
  }
  
  public void setKey(String key)
  {
    this.key = key;
  }
  
  public String getValue()
  {
    return this.value;
  }
  
  public void setValue(String value)
  {
    this.value = value;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
