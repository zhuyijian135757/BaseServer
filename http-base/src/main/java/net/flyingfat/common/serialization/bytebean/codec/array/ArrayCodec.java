package net.flyingfat.common.serialization.bytebean.codec.array;

import java.lang.reflect.Array;

import net.flyingfat.common.serialization.bytebean.codec.AbstractCategoryCodec;
import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecCategory;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

import org.apache.commons.lang.ArrayUtils;

public class ArrayCodec
  extends AbstractCategoryCodec
  implements ByteFieldCodec
{
  public FieldCodecCategory getCategory()
  {
    return FieldCodecCategory.ARRAY;
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    Class<?> fieldClass = ctx.getDecClass();
    
    Class<?> compomentClass = fieldClass.getComponentType();
    ByteFieldDesc desc = ctx.getFieldDesc();
    int arrayLength = 0;
    if ((null == desc) || (!desc.hasLength())) {
      throw new RuntimeException("invalid array env.");
    }
    arrayLength = desc.getLength(ctx.getDecOwner());
    

    Object array = null;
    if (arrayLength > 0)
    {
      array = Array.newInstance(compomentClass, arrayLength);
      ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
      for (int idx = 0; idx < arrayLength; idx++)
      {
        DecResult ret = anyCodec.decode(ctx.getDecContextFactory().createDecContext(bytes, compomentClass, ctx.getDecOwner(), null));
        

        Array.set(array, idx, ret.getValue());
        bytes = ret.getRemainBytes();
      }
    }
    return new DecResult(array, bytes);
  }
  
  public byte[] encode(EncContext ctx)
  {
    Object array = ctx.getEncObject();
    Class<?> fieldClass = ctx.getEncClass();
    Class<?> compomentClass = fieldClass.getComponentType();
    int arrayLength = null != array ? Array.getLength(array) : 0;
    
    ByteFieldDesc desc = ctx.getFieldDesc();
    byte[] bytes = null;
    if ((null == desc) || (!desc.hasLength())) {
      throw new RuntimeException("invalid array env.");
    }
    ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
    for (int idx = 0; idx < arrayLength; idx++) {
      bytes = ArrayUtils.addAll(bytes, anyCodec.encode(ctx.getEncContextFactory().createEncContext(Array.get(array, idx), compomentClass, null)));
    }
    return bytes;
  }
}
