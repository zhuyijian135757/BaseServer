package net.flyingfat.common.http.codec;

import java.util.UUID;

import net.flyingfat.common.http.TransportUtil;
import net.flyingfat.common.lang.ByteUtil;
import net.flyingfat.common.lang.DESUtil;
import net.flyingfat.common.lang.Transformer;
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
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponseEncoder
  implements Transformer<Object, HttpResponse>
{
  private static final Logger logger = LoggerFactory.getLogger(HttpResponseEncoder.class);
  private BeanFieldCodec byteBeanCodec;
  private int dumpBytes = 256;
  private boolean isDebugEnabled;
  private byte[] encryptKey;
  
  public HttpResponse transform(Object signal)
  {
    DefaultHttpResponse resp = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    
    resp.setStatus(HttpResponseStatus.OK);
    resp.setHeader("Content-Type", "application/x-tar");
    if ((signal instanceof XipSignal))
    {
      byte[] bytes = encodeXip((XipSignal)signal);
      if ((logger.isDebugEnabled()) && (this.isDebugEnabled)) {
        logger.debug("signal as hex:{} \r\n{} ", ByteUtil.bytesAsHexString(bytes, this.dumpBytes));
      }
      if (null != bytes)
      {
        resp.setContent(ChannelBuffers.wrappedBuffer(bytes));
        resp.setHeader("Content-Length", Integer.valueOf(bytes.length));
      }
    }
    HttpRequest req = TransportUtil.getRequestOf(signal);
    if (req != null)
    {
      String uuid = req.getHeader("uuid");
      if (uuid != null) {
        resp.setHeader("uuid", uuid);
      }
      String keepAlive = req.getHeader("Connection");
      if (keepAlive != null) {
        resp.setHeader("Connection", keepAlive);
      }
    }
    return resp;
  }
  
  private byte[] encodeXip(XipSignal signal)
  {
    byte[] bytesBody = getByteBeanCodec().encode(getByteBeanCodec().getEncContextFactory().createEncContext(signal, signal.getClass(), null));
    if (getEncryptKey() != null) {
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
      logger.debug("encode XipSignal:" + signal);
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
}
