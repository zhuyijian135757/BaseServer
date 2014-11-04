package net.flyingfat.common.serialization.bytebean.codec.bean;

import java.lang.reflect.Field;
import java.util.List;

import net.flyingfat.common.serialization.bytebean.codec.AbstractCategoryCodec;
import net.flyingfat.common.serialization.bytebean.codec.AnyCodec;
import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecCategory;
import net.flyingfat.common.serialization.bytebean.context.DecContext;
import net.flyingfat.common.serialization.bytebean.context.DecContextFactory;
import net.flyingfat.common.serialization.bytebean.context.DecResult;
import net.flyingfat.common.serialization.bytebean.context.EncContext;
import net.flyingfat.common.serialization.bytebean.context.EncContextFactory;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;
import net.flyingfat.common.serialization.bytebean.field.Field2Desc;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeanCodec
  extends AbstractCategoryCodec
  implements BeanFieldCodec
{
  private static final Logger logger = LoggerFactory.getLogger(BeanCodec.class);
  private DecContextFactory decContextFactory;
  private EncContextFactory encContextFactory;
  private BeanCodecUtil util;
  
  public BeanCodec(Field2Desc field2Desc)
  {
    this.util = new BeanCodecUtil(field2Desc);
  }
  
  public DecResult decode(DecContext ctx)
  {
    byte[] bytes = ctx.getDecBytes();
    Class<?> clazz = ctx.getDecClass();
    
    Object target = null;
    try
    {
      target = clazz.newInstance();
      
      List<ByteFieldDesc> desces = this.util.getFieldDesces(clazz);
      AnyCodec anyCodec = (AnyCodec) ctx.getCodecOf(FieldCodecCategory.ANY);
      for (ByteFieldDesc desc : desces)
      {
        Field field = desc.getField();
        
        Class<?> fieldClass = field.getType();
        
        DecResult ret = anyCodec.decode(this.decContextFactory.createDecContext(bytes, fieldClass, target, desc));
        
        Object fieldValue = ret.getValue();
        bytes = ret.getRemainBytes();
        
        field.setAccessible(true);
        field.set(target, fieldValue);
      }
    }
    catch (InstantiationException e)
    {
      ByteFieldCodec anyCodec;
      logger.error("BeanCodec:", e);
    }
    catch (IllegalAccessException e)
    {
      logger.error("BeanCodec:", e);
    }
    return new DecResult(target, bytes);
  }
  
  public byte[] encode(EncContext ctx)
  {
    Object bean = ctx.getEncObject();
    if (null == bean)
    {
      String errmsg = "BeanCodec: bean is null";
      if (null != ctx.getField()) {
        errmsg = errmsg + "/ cause field is [" + ctx.getField() + "]";
      } else {
        errmsg = errmsg + "/ cause type is [" + ctx.getEncClass() + "]";
      }
      return new byte[0];
    }
    List<ByteFieldDesc> desces = this.util.getFieldDesces(bean.getClass());
    byte[] ret = new byte[0];
    ByteFieldCodec anyCodec = ctx.getCodecOf(FieldCodecCategory.ANY);
    for (ByteFieldDesc desc : desces)
    {
      Field field = desc.getField();
      Class<?> fieldClass = field.getType();
      field.setAccessible(true);
      Object fieldValue = null;
      try
      {
        fieldValue = field.get(bean);
      }
      catch (IllegalArgumentException e)
      {
        logger.error("BeanCodec:", e);
      }
      catch (IllegalAccessException e)
      {
        logger.error("BeanCodec:", e);
      }
      ret = (byte[])ArrayUtils.addAll(ret, anyCodec.encode(this.encContextFactory.createEncContext(fieldValue, fieldClass, desc)));
    }
    return ret;
  }
  
  public FieldCodecCategory getCategory()
  {
    return FieldCodecCategory.BEAN;
  }
  
  public int getStaticByteSize(Class<?> clazz)
  {
    List<ByteFieldDesc> desces = this.util.getFieldDesces(clazz);
    if ((null == desces) || (desces.isEmpty())) {
      return -1;
    }
    int staticByteSize = 0;
    for (ByteFieldDesc desc : desces)
    {
      int fieldByteSize = desc.getByteSize();
      if (fieldByteSize <= 0) {
        fieldByteSize = getStaticByteSize(desc.getFieldType());
      }
      if (fieldByteSize <= 0) {
        return -1;
      }
      staticByteSize += fieldByteSize;
    }
    return staticByteSize;
  }
  
  public DecContextFactory getDecContextFactory()
  {
    return this.decContextFactory;
  }
  
  public void setDecContextFactory(DecContextFactory decContextFactory)
  {
    this.decContextFactory = decContextFactory;
  }
  
  public EncContextFactory getEncContextFactory()
  {
    return this.encContextFactory;
  }
  
  public void setEncContextFactory(EncContextFactory encContextFactory)
  {
    this.encContextFactory = encContextFactory;
  }
}
