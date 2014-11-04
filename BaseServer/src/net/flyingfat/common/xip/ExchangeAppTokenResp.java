package net.flyingfat.common.xip;

import java.util.ArrayList;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

@SignalCode(messageCode=200099)
public class ExchangeAppTokenResp
  extends AbstractXipResponse
{
  @ByteField(index=2, bytes=4, description="第三方与真趣的映射id")
  private long joyId;
  @ByteField(index=3, description="appToken")
  private String appToken;
  @ByteField(index=4, description="参数信息")
  private ArrayList<ReservedParameter> reservedParameter = new ArrayList();
  
  public long getJoyId()
  {
    return this.joyId;
  }
  
  public void setJoyId(long joyId)
  {
    this.joyId = joyId;
  }
  
  public String getAppToken()
  {
    return this.appToken;
  }
  
  public void setAppToken(String appToken)
  {
    this.appToken = appToken;
  }
  
  public ArrayList<ReservedParameter> getReservedParameter()
  {
    return this.reservedParameter;
  }
  
  public void setReservedParameter(ArrayList<ReservedParameter> reservedParameter)
  {
    this.reservedParameter = reservedParameter;
  }
}
