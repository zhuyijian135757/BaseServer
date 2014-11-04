package net.flyingfat.common.xip;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@SignalCode(messageCode=221609)
public class GenerateNickNameResp
  extends AbstractXipResponse
{
  @ByteField(index=2)
  private String nickName;
  
  public String getNickName()
  {
    return this.nickName;
  }
  
  public void setNickName(String nickName)
  {
    this.nickName = nickName;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
