package net.flyingfat.common.serialization.bytebean.codec.primitive;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(LongCodec.class);
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { Long.TYPE, Long.class };
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    int byteLength = ctx.getByteSize();
    NumberCodec numberCodec = ctx.getNumberCodec();
    if (byteLength > bytes.length)
    {
      String errmsg = "LongCodec: not enough bytes for decode, need [" + byteLength + "], actually [" + bytes.length + "].";
      if (null != ctx.getField()) {
        errmsg = errmsg + "/ cause field is [" + ctx.getField() + "]";
      }
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    return new DecResult(Long.valueOf(numberCodec.bytes2Long(bytes, byteLength)), ArrayUtils.subarray(bytes, byteLength, bytes.length));
  }
  
  public byte[] encode(EncContext ctx)
  {
    long enc = ((Long)ctx.getEncObject()).longValue();
    int byteLength = ctx.getByteSize();
    NumberCodec numberCodec = ctx.getNumberCodec();
    
    return numberCodec.long2Bytes(enc, byteLength);
  }
}
