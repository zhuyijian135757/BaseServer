package net.flyingfat.common.xip;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@SignalCode(messageCode=121609)
public class GenerateNickNameReq
  extends AbstractXipRequest
{
  @ByteField(index=1, bytes=4)
  private int zoneId;
  @ByteField(index=2, bytes=1, description="1:男， 2：女")
  private int gender;
  
  public int getZoneId()
  {
    return this.zoneId;
  }
  
  public void setZoneId(int zoneId)
  {
    this.zoneId = zoneId;
  }
  
  public int getGender()
  {
    return this.gender;
  }
  
  public void setGender(int gender)
  {
    this.gender = gender;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
