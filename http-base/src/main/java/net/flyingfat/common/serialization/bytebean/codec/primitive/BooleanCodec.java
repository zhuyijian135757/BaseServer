package net.flyingfat.common.serialization.bytebean.codec.primitive;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BooleanCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(BooleanCodec.class);
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { Boolean.TYPE, Boolean.class };
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    if (bytes.length < 1)
    {
      String errmsg = "BooleanCodec: not enough bytes for decode, need [1], actually [" + bytes.length + "].";
      if (null != ctx.getField()) {
        errmsg = errmsg + "/ cause field is [" + ctx.getField() + "]";
      }
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    return new DecResult(Boolean.valueOf(bytes[0] != 0), ArrayUtils.subarray(bytes, 1, bytes.length));
  }
  
  public byte[] encode(EncContext ctx)
  {
    return ctx.getNumberCodec().short2Bytes((short)(((Boolean)ctx.getEncObject()).booleanValue() ? 1 : 0), 1);
  }
}
