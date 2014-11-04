package net.flyingfat.common.xip;

import java.util.ArrayList;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

@SignalCode(messageCode=207001)
public class CheckThirdSessionResp
  extends AbstractXipResponse
{
  @ByteField(index=3)
  private String thirdLoginId;
  @ByteField(index=4)
  private ArrayList<ReservedParameter> reservedParameter = new ArrayList();
  
  public String getThirdLoginId()
  {
    return this.thirdLoginId;
  }
  
  public void setThirdLoginId(String thirdLoginId)
  {
    this.thirdLoginId = thirdLoginId;
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
