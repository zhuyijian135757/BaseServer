package net.flyingfat.common.serialization.bytebean.codec.array;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecCategory;
import net.flyingfat.common.serialization.bytebean.codec.primitive.AbstractPrimitiveCodec;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LenListCodec
  extends AbstractPrimitiveCodec
  implements ByteFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(LenListCodec.class);
  public int maxArryListLen = 30;
  
  public FieldCodecCategory getCategory()
  {
    return null;
  }
  
  public DecResult decode(DecContext ctx)
  {
    DecResult ret = ctx.getCodecOf(Integer.TYPE).decode(ctx.getDecContextFactory().createDecContext(ctx.getDecBytes(), Integer.TYPE, ctx.getDecOwner(), null));
    int listLength = ((Integer)ret.getValue()).intValue();
    byte[] bytes = ret.getRemainBytes();
    Class<?> compomentClass = getCompomentClass(ctx.getField());
    ArrayList<Object> list;
    if (listLength <= this.maxArryListLen) {
      list = new ArrayList(listLength);
    } else {
      list = new ArrayList(this.maxArryListLen);
    }
    if (listLength > 0)
    {
      ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
      for (int idx = 0; idx < listLength; idx++)
      {
        ret = anyCodec.decode(ctx.getDecContextFactory().createDecContext(bytes, compomentClass, ctx.getDecOwner(), null));
        list.add(ret.getValue());
        bytes = ret.getRemainBytes();
      }
    }
    return new DecResult(list, bytes);
  }
  
  public byte[] encode(EncContext ctx)
  {
    ArrayList<Object> list = (ArrayList)ctx.getEncObject();
    int listLength = null != list ? list.size() : 0;
    Class<?> compomentClass = getCompomentClass(ctx.getField());
    

    byte[] bytes = ctx.getCodecOf(Integer.TYPE).encode(ctx.getEncContextFactory().createEncContext(Integer.valueOf(listLength), Integer.TYPE, null));
    if (listLength > 0)
    {
      ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
      for (int idx = 0; idx < listLength; idx++) {
        bytes = ArrayUtils.addAll(bytes, anyCodec.encode(ctx.getEncContextFactory().createEncContext(list.get(idx), compomentClass, null)));
      }
    }
    return bytes;
  }
  
  public Class<?>[] getFieldType()
  {
    return new Class[] { ArrayList.class };
  }
  
  public Class<?> getCompomentClass(Field field)
  {
    if (null == field)
    {
      String errmsg = "LenListCodec: field is null, can't get compoment class.";
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    Type type = field.getGenericType();
    if ((null == type) || (!(type instanceof ParameterizedType)))
    {
      String errmsg = "LenListCodec: getGenericType invalid, can't get compoment class./ cause field is [" + field + "]";
      logger.error(errmsg);
      throw new RuntimeException(errmsg);
    }
    ParameterizedType parameterizedType = (ParameterizedType)type;
    Class<?> clazz = (Class)parameterizedType.getActualTypeArguments()[0];
    return clazz;
  }
  
  public int getMaxArryListLen()
  {
    return this.maxArryListLen;
  }
  
  public void setMaxArryListLen(int maxArryListLen)
  {
    this.maxArryListLen = maxArryListLen;
  }
  
  public static void main(String[] args)
  {
    ArrayList<Integer> list = new ArrayList(2);
    for (int i = 0; i < 10; i++) {
      list.add(Integer.valueOf(i));
    }
    System.out.println(list.size());
  }
}
