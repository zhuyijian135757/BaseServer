package net.flyingfat.common.serialization.bytebean.context;

import java.lang.reflect.Field;

import net.flyingfat.common.serialization.bytebean.ByteBeanUtil;
import net.flyingfat.common.serialization.bytebean.codec.ByteFieldCodec;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecCategory;
import net.flyingfat.common.serialization.bytebean.codec.FieldCodecProvider;
import net.flyingfat.common.serialization.bytebean.codec.NumberCodec;
import net.flyingfat.common.serialization.bytebean.field.ByteFieldDesc;

public class AbstractCodecContext
  extends ByteBeanUtil
  implements FieldCodecContext
{
  protected FieldCodecProvider codecProvider = null;
  protected ByteFieldDesc fieldDesc;
  protected NumberCodec numberCodec;
  protected Class<?> targetType;
  
  public ByteFieldDesc getFieldDesc()
  {
    return this.fieldDesc;
  }
  
  public Field getField()
  {
    if (null != this.fieldDesc) {
      return this.fieldDesc.getField();
    }
    return null;
  }
  
  public NumberCodec getNumberCodec()
  {
    return this.numberCodec;
  }
  
  public int getByteSize()
  {
    int ret = -1;
    if (null != this.fieldDesc) {
      ret = this.fieldDesc.getByteSize();
    } else if (null != this.targetType) {
      ret = ByteBeanUtil.type2DefaultByteSize(this.targetType);
    }
    return ret;
  }
  
  public ByteFieldCodec getCodecOf(FieldCodecCategory type)
  {
    if (null != this.codecProvider) {
      return this.codecProvider.getCodecOf(type);
    }
    return null;
  }
  
  public ByteFieldCodec getCodecOf(Class<?> clazz)
  {
    if (null != this.codecProvider) {
      return this.codecProvider.getCodecOf(clazz);
    }
    return null;
  }
}
