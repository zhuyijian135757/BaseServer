package net.flyingfat.common.xip;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@SignalCode(messageCode=100099)
public class ExchangeAppTokenReq
  extends AbstractXipRequest
{
  @ByteField(index=0, description="第三方平台帐号id")
  private String thirdUserId;
  @ByteField(index=1, description="第三方平台token信息")
  private String thirdSessionId;
  @ByteField(index=2, bytes=1, description="登录来源1：真趣，2：腾讯, 3:91, 4:新浪, 5：UC, 6:索乐7：索乐华为 8:OPPO 9:斯凯 10:梦宝谷 11.乐逗")
  private int loginSource;
  @ByteField(index=3, description="第三方安全key")
  private String thirdSecrityKey;
  @ByteField(index=4, bytes=2)
  private int gameId;
  @ByteField(index=5, description="终端信息")
  private TerminalInfo terminalInfo;
  
  public String getThirdUserId()
  {
    return this.thirdUserId;
  }
  
  public void setThirdUserId(String thirdUserId)
  {
    this.thirdUserId = thirdUserId;
  }
  
  public String getThirdSessionId()
  {
    return this.thirdSessionId;
  }
  
  public void setThirdSessionId(String thirdSessionId)
  {
    this.thirdSessionId = thirdSessionId;
  }
  
  public int getLoginSource()
  {
    return this.loginSource;
  }
  
  public void setLoginSource(int loginSource)
  {
    this.loginSource = loginSource;
  }
  
  public String getThirdSecrityKey()
  {
    return this.thirdSecrityKey;
  }
  
  public void setThirdSecrityKey(String thirdSecrityKey)
  {
    this.thirdSecrityKey = thirdSecrityKey;
  }
  
  public int getGameId()
  {
    return this.gameId;
  }
  
  public void setGameId(int gameId)
  {
    this.gameId = gameId;
  }
  
  public TerminalInfo getTerminalInfo()
  {
    return this.terminalInfo;
  }
  
  public void setTerminalInfo(TerminalInfo terminalInfo)
  {
    this.terminalInfo = terminalInfo;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
  }
}
