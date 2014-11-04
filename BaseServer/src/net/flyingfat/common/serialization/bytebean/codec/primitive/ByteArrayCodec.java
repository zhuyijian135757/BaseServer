package net.flyingfat.common.serialization.bytebean.codec.primitive;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ByteArrayCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(ByteArrayCodec.class);
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { byte[].class };
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    ByteFieldDesc desc = ctx.getFieldDesc();
    int arrayLength = 0;
    if (null == desc) {
      throw new RuntimeException("invalid bytearray env.");
    }
    if (desc.hasLength()) {
      arrayLength = desc.getLength(ctx.getDecOwner());
    } else if (desc.getFixedLength() > 0) {
      arrayLength = desc.getFixedLength();
    } else {
      throw new RuntimeException("invalid bytearray env.");
    }
    if (bytes.length < arrayLength)
    {
      String errmsg = "ByteArrayCodec: not enough bytes for decode, need [" + arrayLength + "], actually [" + bytes.length + "].";
      if (null != ctx.getField()) {
        errmsg = errmsg + "/ cause field is [" + ctx.getField() + "]";
      }
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    return new DecResult((byte[])ArrayUtils.subarray(bytes, 0, arrayLength), ArrayUtils.subarray(bytes, arrayLength, bytes.length));
  }
  
  public byte[] encode(EncContext ctx)
  {
    byte[] array = (byte[])ctx.getEncObject();
    ByteFieldDesc desc = ctx.getFieldDesc();
    byte[] bytes = null;
    if (null == desc) {
      throw new RuntimeException("invalid bytearray env.");
    }
    if ((!desc.hasLength()) && (desc.getFixedLength() < 0)) {
      throw new RuntimeException("invalid bytearray env.");
    }
    return ArrayUtils.addAll(bytes, array);
  }
}
