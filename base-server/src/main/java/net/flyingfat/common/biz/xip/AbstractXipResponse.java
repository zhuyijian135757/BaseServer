package net.flyingfat.common.biz.xip;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.xip.AbstractXipSignal;
import net.flyingfat.common.serialization.protocol.xip.XipResponse;

public class AbstractXipResponse
  extends AbstractXipSignal
  implements XipResponse
{
  @ByteField(index=0, description="错误代码")
  private int errorCode;
  @ByteField(index=1, description="提示消息内容")
  private String errorMessage;
  
  public static <T extends AbstractXipResponse> T createRespForError(Class<T> clazz, int errorCode, String errorMessage)
  {
    T resp;
    try
    {
      resp = (T)clazz.newInstance();
    }
    catch (InstantiationException e)
    {
      e.printStackTrace();
      return null;
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
      return null;
    }
    resp.setErrorCode(errorCode);
    resp.setErrorMessage(errorMessage);
    
    return resp;
  }
  
  public int getErrorCode()
  {
    return this.errorCode;
  }
  
  public void setErrorCode(int errorCode)
  {
    this.errorCode = errorCode;
  }
  
  public String getErrorMessage()
  {
    return this.errorMessage;
  }
  
  public void setErrorMessage(String errorMessage)
  {
    this.errorMessage = errorMessage;
  }
}
