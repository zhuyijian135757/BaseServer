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

import org.apache.commons.lang.ArrayUtils;

public class LenArrayCodec
  extends AbstractCategoryCodec
  implements ByteFieldCodec
{
  public FieldCodecCategory getCategory()
  {
    return FieldCodecCategory.ARRAY;
  }
  
  public DecResult decode(DecContext ctx)
  {
    DecResult ret = ctx.getCodecOf(Integer.TYPE).decode(ctx.getDecContextFactory().createDecContext(ctx.getDecBytes(), Integer.TYPE, ctx.getDecOwner(), null));
    

    int arrayLength = ((Integer)ret.getValue()).intValue();
    byte[] bytes = ret.getRemainBytes();
    
    Object array = null;
    if (arrayLength > 0)
    {
      Class<?> fieldClass = ctx.getDecClass();
      Class<?> compomentClass = fieldClass.getComponentType();
      
      array = Array.newInstance(compomentClass, arrayLength);
      ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
      for (int idx = 0; idx < arrayLength; idx++)
      {
        ret = anyCodec.decode(ctx.getDecContextFactory().createDecContext(bytes, compomentClass, ctx.getDecOwner(), null));
        

        Array.set(array, idx, ret.getValue());
        bytes = ret.getRemainBytes();
      }
    }
    return new DecResult(array, bytes);
  }
  
  public byte[] encode(EncContext ctx)
  {
    Object array = ctx.getEncObject();
    int arrayLength = null != array ? Array.getLength(array) : 0;
    

    byte[] bytes = ctx.getCodecOf(Integer.TYPE).encode(ctx.getEncContextFactory().createEncContext(Integer.valueOf(arrayLength), Integer.TYPE, null));
    if (arrayLength > 0)
    {
      Class<?> fieldClass = ctx.getEncClass();
      Class<?> compomentClass = fieldClass.getComponentType();
      
      ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
      for (int idx = 0; idx < arrayLength; idx++) {
        bytes = ArrayUtils.addAll(bytes, anyCodec.encode(ctx.getEncContextFactory().createEncContext(Array.get(array, idx), compomentClass, null)));
      }
    }
    return bytes;
  }
}
