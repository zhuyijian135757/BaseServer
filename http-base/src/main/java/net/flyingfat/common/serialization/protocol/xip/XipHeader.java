package net.flyingfat.common.serialization.protocol.xip;

import java.util.UUID;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class XipHeader
{
  public static final int HEADER_LENGTH = 28;
  public static final int XIP_REQUEST = 1;
  public static final int XIP_RESPONSE = 2;
  public static final int XIP_NOTIFY = 3;
  public static final int CONTENT_DES = 1;
  @ByteField(index=0, bytes=1)
  private int basicVer = 1;
  @ByteField(index=1)
  private int length = 0;
  @ByteField(index=2, bytes=1)
  private int type = 1;
  @ByteField(index=3, bytes=2)
  private int reserved = 0; //区分加解密:0需要加解密 1不需要加解密
  @ByteField(index=4)
  private long firstTransaction;
  @ByteField(index=5)
  private long secondTransaction;
  @ByteField(index=6)
  private int messageCode;
  
  public int getBasicVer()
  {
    return this.basicVer;
  }
  
  public void setBasicVer(int basicVer)
  {
    this.basicVer = basicVer;
  }
  
  public int getLength()
  {
    return this.length;
  }
  
  public void setLength(int length)
  {
    this.length = length;
  }
  
  public int getType()
  {
    return this.type;
  }
  
  public void setType(int type)
  {
    this.type = type;
  }
  
  public int getReserved()
  {
    return this.reserved;
  }
  
  public void setReserved(int reserved)
  {
    this.reserved = reserved;
  }
  
  public long getFirstTransaction()
  {
    return this.firstTransaction;
  }
  
  public void setFirstTransaction(long firstTransaction)
  {
    this.firstTransaction = firstTransaction;
  }
  
  public long getSecondTransaction()
  {
    return this.secondTransaction;
  }
  
  public void setSecondTransaction(long secondTransaction)
  {
    this.secondTransaction = secondTransaction;
  }
  
  public int getMessageCode()
  {
    return this.messageCode;
  }
  
  public void setMessageCode(int messageCode)
  {
    if (messageCode <= 0) {
      throw new RuntimeException("invalid message code.");
    }
    this.messageCode = messageCode;
  }
  
  public void setTransaction(UUID uuid)
  {
    this.firstTransaction = uuid.getMostSignificantBits();
    this.secondTransaction = uuid.getLeastSignificantBits();
  }
  
  /**
   * 客户端由httpclient实现，服务端统一分配标识号,返回 UUID.randomUUID()
   * 客户端由httpConnector实现，客户端各自指定标识号，返回new UUID(this.firstTransaction, this.secondTransaction); 
   * @return
   */
  public UUID getTransactionAsUUID()
  {
      return new UUID(this.firstTransaction, this.secondTransaction); 
  }
  
  public void setTypeForClass(Class<?> cls)
  {
    if (XipRequest.class.isAssignableFrom(cls)) {
      this.type = 1;
    } else if (XipResponse.class.isAssignableFrom(cls)) {
      this.type = 2;
    } else if (XipNotify.class.isAssignableFrom(cls)) {
      this.type = 3;
    }
  }
  
  public String toString()
  {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
