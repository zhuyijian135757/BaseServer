package net.flyingfat.common.biz.xip;

import net.flyingfat.common.serialization.bytebean.ByteBean;
import net.flyingfat.common.serialization.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TerminalInfo
  implements ByteBean
{
  @ByteField(index=0, description="厂商名称")
  private String hsman;
  @ByteField(index=1, description="机型类型")
  private String hstype;
  @ByteField(index=2, description="操作系统版本")
  private String osVer;
  @ByteField(index=3, bytes=2, description="屏幕宽")
  private int screenWidth;
  @ByteField(index=4, bytes=2, description="屏幕高")
  private int screenHeight;
  @ByteField(index=5, bytes=2, description="RAM大小")
  private int ramSize;
  @ByteField(index=6, description="国际移动用户识别码")
  private String imsi;
  @ByteField(index=7, description="国际移动设备身份码")
  private String imei;
  @ByteField(index=8, description="短消息中心号码")
  private String smsCenter;
  @ByteField(index=9, bytes=2, description="LAC位置区号码")
  private int lac;
  @ByteField(index=10)
  private String ip;
  @ByteField(index=11, bytes=1, description="1:2G, 2:3G, 3:wifi")
  private int networkType;
  @ByteField(index=12, description="通道标识符")
  private String channelId;
  @ByteField(index=13, description="应用程序id")
  private String appId;
  @ByteField(index=14, description="apkVersion")
  private String appVersion;
  @ByteField(index=15, description="0:其他， 1：移动， 2：联通， 3：电信")
  private String provider;
  
  public String getHsman()
  {
    return this.hsman;
  }
  
  public void setHsman(String hsman)
  {
    this.hsman = hsman;
  }
  
  public String getHstype()
  {
    return this.hstype;
  }
  
  public void setHstype(String hstype)
  {
    this.hstype = hstype;
  }
  
  public String getOsVer()
  {
    return this.osVer;
  }
  
  public void setOsVer(String osVer)
  {
    this.osVer = osVer;
  }
  
  public int getScreenWidth()
  {
    return this.screenWidth;
  }
  
  public void setScreenWidth(int screenWidth)
  {
    this.screenWidth = screenWidth;
  }
  
  public int getScreenHeight()
  {
    return this.screenHeight;
  }
  
  public void setScreenHeight(int screenHeight)
  {
    this.screenHeight = screenHeight;
  }
  
  public int getRamSize()
  {
    return this.ramSize;
  }
  
  public void setRamSize(int ramSize)
  {
    this.ramSize = ramSize;
  }
  
  public String getImsi()
  {
    if (this.imsi == null) {
      return "";
    }
    return this.imsi;
  }
  
  public void setImsi(String imsi)
  {
    this.imsi = imsi;
  }
  
  public String getImei()
  {
    if (this.imei == null) {
      return "";
    }
    return this.imei;
  }
  
  public void setImei(String imei)
  {
    this.imei = imei;
  }
  
  public String getSmsCenter()
  {
    return this.smsCenter;
  }
  
  public void setSmsCenter(String smsCenter)
  {
    this.smsCenter = smsCenter;
  }
  
  public int getLac()
  {
    return this.lac;
  }
  
  public void setLac(int lac)
  {
    this.lac = lac;
  }
  
  public String getIp()
  {
    return this.ip;
  }
  
  public void setIp(String ip)
  {
    this.ip = ip;
  }
  
  public int getNetworkType()
  {
    return this.networkType;
  }
  
  public void setNetworkType(int networkType)
  {
    this.networkType = networkType;
  }
  
  public String getChannelId()
  {
    if (this.channelId == null) {
      return "";
    }
    return this.channelId;
  }
  
  public void setChannelId(String channelId)
  {
    this.channelId = channelId;
  }
  
  public String getAppId()
  {
    if (this.appId == null) {
      return "";
    }
    return this.appId;
  }
  
  public void setAppId(String appId)
  {
    this.appId = appId;
  }
  
  public String getAppVersion()
  {
    return this.appVersion;
  }
  
  public void setAppVersion(String appVersion)
  {
    this.appVersion = appVersion;
  }
  
  public String getProvider()
  {
    return this.provider;
  }
  
  public void setProvider(String provider)
  {
    this.provider = provider;
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
