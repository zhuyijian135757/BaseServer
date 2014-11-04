package net.flyingfat.common.serialization.bytebean.codec;

import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnyCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(AnyCodec.class);
  
  public FieldCodecCategory getCategory()
  {
    return FieldCodecCategory.ANY;
  }
  
  public Class<?>[] getFieldType()
  {
    return null;
  }
  
  public DecResult decode(DecContext ctx)
  {
    Class<?> clazz = ctx.getDecClass();
    
    ByteFieldCodec codec = ctx.getCodecOf(clazz);
    if (null == codec) {
      if (clazz.isArray()) {
        codec = ctx.getCodecOf(FieldCodecCategory.ARRAY);
      } else {
        codec = ctx.getCodecOf(FieldCodecCategory.BEAN);
      }
    }
    if (null != codec) {
      return codec.decode(ctx);
    }
    logger.error("decode : can not find matched codec for field [" + ctx.getField() + "].");
    

    return new DecResult(null, ctx.getDecBytes());
  }
  
  public byte[] encode(EncContext ctx)
  {
    Class<?> clazz = ctx.getEncClass();
    
    ByteFieldCodec codec = ctx.getCodecOf(clazz);
    if (null == codec) {
      if (clazz.isArray()) {
        codec = ctx.getCodecOf(FieldCodecCategory.ARRAY);
      } else {
        codec = ctx.getCodecOf(FieldCodecCategory.BEAN);
      }
    }
    if (null != codec) {
      return codec.encode(ctx);
    }
    logger.error("encode : can not find matched codec for field [" + ctx.getField() + "].");
    


    return new byte[0];
  }
}
