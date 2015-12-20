package net.flyingfat.common.http.codec;

import java.util.UUID;

import net.flyingfat.common.lang.ByteUtil;
import net.flyingfat.common.lang.DESUtil;
import net.flyingfat.common.serialization.bytebean.codec.AnyCodec;
import net.flyingfat.common.serialization.bytebean.codec.DefaultCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.DefaultNumberCodecs;
import net.flyingfat.common.serialization.bytebean.codec.array.LenArrayCodec;
import net.flyingfat.common.serialization.bytebean.codec.array.LenListCodec;
import net.flyingfat.common.serialization.bytebean.codec.bean.BeanFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.bean.EarlyStopBeanCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.ByteCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.CStyleStringCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.FloatCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.IntCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.LenByteArrayCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.LongCodec;
import net.flyingfat.common.serialization.bytebean.codec.primitive.ShortCodec;
import net.flyingfat.common.serialization.bytebean.context.DefaultDecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DefaultEncContextFactory;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;
import net.flyingfat.common.serialization.bytebean.field.DefaultField2Desc;
import net.flyingfat.common.serialization.protocol.annotation.SignalCode;
import net.flyingfat.common.serialization.protocol.xip.XipHeader;
import net.flyingfat.common.serialization.protocol.xip.XipSignal;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpRequestEncoder
  extends OneToOneEncoder
{
  private static final Logger logger = LoggerFactory.getLogger(HttpRequestEncoder.class);
  private BeanFieldCodec byteBeanCodec;
  private int dumpBytes = 256;
  private boolean isDebugEnabled;
  private byte[] encryptKey;
  private Integer reserved;
  
  protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg)
    throws Exception
  {
    HttpRequest request = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, "/");
    request.setHeader("Host", channel.getRemoteAddress());
    if ((msg instanceof XipSignal))
    {
      byte[] bytes = encodeXip((XipSignal)msg);
      request.setHeader("Content-Length", Integer.valueOf(bytes.length));
      request.setContent(ChannelBuffers.wrappedBuffer(bytes));
    }
    else if ((msg instanceof byte[]))
    {
      byte[] bytes = (byte[])msg;
      request.setHeader("Content-Length", Integer.valueOf(bytes.length));
      request.setContent(ChannelBuffers.wrappedBuffer(bytes));
    }
    return request;
  }
  
  public byte[] encodeXip(XipSignal signal)
    throws Exception
  {
    byte[] bytesBody = getByteBeanCodec().encode(getByteBeanCodec().getEncContextFactory().createEncContext(signal, signal.getClass(), null));
    if (reserved != null && reserved!=XipHeader.CONTENT_DES) {
      try
      {
        bytesBody = DESUtil.encrypt(bytesBody, getEncryptKey());
      }
      catch (Exception e)
      {
        throw new RuntimeException("encode encryption faield.");
      }
    }
    SignalCode attr = (SignalCode)signal.getClass().getAnnotation(SignalCode.class);
    if (null == attr) {
      throw new RuntimeException("invalid signal, no messageCode defined.");
    }
    XipHeader header = createHeader((byte)1, signal.getIdentification(), attr.messageCode(), bytesBody.length);
    

    header.setTypeForClass(signal.getClass());
    
    byte[] bytes = ArrayUtils.addAll(getByteBeanCodec().encode(getByteBeanCodec().getEncContextFactory().createEncContext(header, XipHeader.class, null)), bytesBody);
    if ((logger.isDebugEnabled()) && (this.isDebugEnabled))
    {
      logger.debug("encode XipSignal", ToStringBuilder.reflectionToString(signal));
      logger.debug("and XipSignal raw bytes -->");
      logger.debug(ByteUtil.bytesAsHexString(bytes, this.dumpBytes));
    }
    return bytes;
  }
  
  private XipHeader createHeader(byte basicVer, UUID id, int messageCode, int messageLen)
  {
    XipHeader header = new XipHeader();
    header.setTransaction(id);
    int headerSize = getByteBeanCodec().getStaticByteSize(XipHeader.class);
    header.setLength(headerSize + messageLen);
    header.setMessageCode(messageCode);
    header.setBasicVer(basicVer);
    header.setReserved(reserved);
    
    return header;
  }
  
  public void setByteBeanCodec(BeanFieldCodec byteBeanCodec)
  {
    this.byteBeanCodec = byteBeanCodec;
  }
  
  public BeanFieldCodec getByteBeanCodec()
  {
    if (this.byteBeanCodec == null)
    {
      DefaultCodecProvider codecProvider = new DefaultCodecProvider();
      

      codecProvider.addCodec(new AnyCodec()).addCodec(new ByteCodec()).addCodec(new ShortCodec()).addCodec(new IntCodec()).addCodec(new LongCodec()).addCodec(new CStyleStringCodec()).addCodec(new LenByteArrayCodec()).addCodec(new LenListCodec()).addCodec(new LenArrayCodec()).addCodec(new FloatCodec());
      


      EarlyStopBeanCodec byteBeanCodec = new EarlyStopBeanCodec(new DefaultField2Desc());
      codecProvider.addCodec(byteBeanCodec);
      
      DefaultEncContextFactory encContextFactory = new DefaultEncContextFactory();
      DefaultDecContextFactory decContextFactory = new DefaultDecContextFactory();
      
      encContextFactory.setCodecProvider(codecProvider);
      encContextFactory.setNumberCodec(DefaultNumberCodecs.getLittleEndianNumberCodec());
      
      decContextFactory.setCodecProvider(codecProvider);
      decContextFactory.setNumberCodec(DefaultNumberCodecs.getLittleEndianNumberCodec());
      
      byteBeanCodec.setDecContextFactory(decContextFactory);
      byteBeanCodec.setEncContextFactory(encContextFactory);
      
      this.byteBeanCodec = byteBeanCodec;
    }
    return this.byteBeanCodec;
  }
  
  public int getDumpBytes()
  {
    return this.dumpBytes;
  }
  
  public void setDumpBytes(int dumpBytes)
  {
    this.dumpBytes = dumpBytes;
  }
  
  public boolean isDebugEnabled()
  {
    return this.isDebugEnabled;
  }
  
  public void setDebugEnabled(boolean isDebugEnabled)
  {
    this.isDebugEnabled = isDebugEnabled;
  }
  
  public byte[] getEncryptKey()
  {
    return this.encryptKey;
  }
  
  public void setEncryptKey(byte[] encryptKey)
  {
    this.encryptKey = encryptKey;
  }

  public int getReserved() {
	return reserved;
  }

  public void setReserved(int reserved) {
	this.reserved = reserved;
  }
  
  
}
