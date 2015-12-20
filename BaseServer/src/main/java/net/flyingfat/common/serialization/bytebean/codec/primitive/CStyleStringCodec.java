package net.flyingfat.common.serialization.bytebean.codec.primitive;

import java.io.UnsupportedEncodingException;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CStyleStringCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(CStyleStringCodec.class);
  private static final String XIP_STR_CHARSET = "UTF-8";
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { String.class };
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    Object ret = null;
    
    int index = ArrayUtils.indexOf(bytes, (byte)0);
    if (-1 == index)
    {
      String errmsg = "CStyleString: could not found \\0 for string terminated.";
      if (null != ctx.getField()) {
        errmsg = errmsg + "/ cause field is [" + ctx.getField() + "]";
      }
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    try
    {
      byte[] tmp = ArrayUtils.subarray(bytes, 0, index);
      ret = new String(tmp, "UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      logger.error("CStyleString", e);
    }
    return new DecResult(ret, ArrayUtils.subarray(bytes, index + 1, bytes.length));
  }
  
  public byte[] encode(EncContext ctx)
  {
    String value = (String)ctx.getEncObject();
    
    byte[] bytes = null;
    if (null == value) {
      return new byte[] { 0 };
    }
    try
    {
      bytes = value.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e)
    {
      logger.error("CStyleString", e);
    }
    return ArrayUtils.add(bytes, (byte)0);
  }
}
