package net.flyingfat.common.serialization.bytebean.codec.primitive;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LenByteArrayCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(LenByteArrayCodec.class);
  
  public DecResult decode(DecContext ctx)
  {
    DecResult ret = ctx.getCodecOf(Integer.TYPE).decode(ctx.getDecContextFactory().createDecContext(ctx.getDecBytes(), Integer.TYPE, ctx.getDecOwner(), null));
    

    int arrayLength = ((Integer)ret.getValue()).intValue();
    byte[] bytes = ret.getRemainBytes();
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
    

    return (byte[])ArrayUtils.addAll(ctx.getCodecOf(Integer.TYPE).encode(ctx.getEncContextFactory().createEncContext(Integer.valueOf(null == array ? 0 : array.length), Integer.TYPE, null)), array);
  }
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { byte[].class };
  }
}
