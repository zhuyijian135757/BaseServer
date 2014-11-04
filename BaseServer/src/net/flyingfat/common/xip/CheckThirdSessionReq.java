package net.flyingfat.common.xip;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;

@SignalCode(messageCode=107001)
public class CheckThirdSessionReq
  extends AbstractXipRequest
{
  @ByteField(index=0)
  private String channelId;
  @ByteField(index=1)
  private String thirdUserId;
  @ByteField(index=2)
  private String sessionId;
  @ByteField(index=3)
  private String securityKey;
  @ByteField(index=4, bytes=1, description="登录来源1：真趣，2：腾讯, 3:91, 4:新浪, 5：UC, 6:索乐 7：索乐华为  8:OPPO 9:斯凯 10:梦宝谷 11.乐逗")
  private int loginSource;
  @ByteField(index=5, description="游戏app信息")
  private String appId;
  @ByteField(index=6, bytes=2, description="游戏gameid")
  private int gameId;
  
  public String getChannelId()
  {
    return this.channelId;
  }
  
  public void setChannelId(String channelId)
  {
    this.channelId = channelId;
  }
  
  public String getSessionId()
  {
    return this.sessionId;
  }
  
  public void setSessionId(String sessionId)
  {
    this.sessionId = sessionId;
  }
  
  public String getSecurityKey()
  {
    return this.securityKey;
  }
  
  public void setSecurityKey(String securityKey)
  {
    this.securityKey = securityKey;
  }
  
  public String getThirdUserId()
  {
    return this.thirdUserId;
  }
  
  public void setThirdUserId(String thirdUserId)
  {
    this.thirdUserId = thirdUserId;
  }
  
  public int getLoginSource()
  {
    return this.loginSource;
  }
  
  public void setLoginSource(int loginSource)
  {
    this.loginSource = loginSource;
  }
  
  public String getAppId()
  {
    return this.appId;
  }
  
  public void setAppId(String appId)
  {
    this.appId = appId;
  }
  
  public int getGameId()
  {
    return this.gameId;
  }
  
  public void setGameId(int gameId)
  {
    this.gameId = gameId;
  }
}
