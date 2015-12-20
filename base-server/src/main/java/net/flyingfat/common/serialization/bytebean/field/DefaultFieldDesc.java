package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;

import net.flyingfat.common.serialization.bytebean.ByteBeanUtil;

public class DefaultFieldDesc
  extends ByteBeanUtil
  implements ByteFieldDesc
{
  private Field field;
  private int maxByteSize = -1;
  private int index;
  private int byteSize = -1;
  private Field lengthField = null;
  private String charset;
  private int bytesPerChar = 1;
  private String description;
  private int fixedLength = -1;
  
  public DefaultFieldDesc setField(Field field)
  {
    this.field = field;
    this.maxByteSize = ByteBeanUtil.type2DefaultByteSize(this.field.getType());
    return this;
  }
  
  public DefaultFieldDesc setIndex(int index)
  {
    this.index = index;
    return this;
  }
  
  public DefaultFieldDesc setByteSize(int byteSize)
  {
    this.byteSize = byteSize;
    return this;
  }
  
  public DefaultFieldDesc setLengthField(Field lengthField)
  {
    this.lengthField = lengthField;
    return this;
  }
  
  public DefaultFieldDesc setCharset(String charset)
  {
    this.charset = charset;
    if (charset.startsWith("UTF-16")) {
      this.bytesPerChar = 2;
    } else {
      this.bytesPerChar = 1;
    }
    return this;
  }
  
  public DefaultFieldDesc setFixedLength(int fixedLength)
  {
    this.fixedLength = fixedLength;
    return this;
  }
  
  public DefaultFieldDesc setDescription(String description)
  {
    this.description = description;
    return this;
  }
  
  public int getIndex()
  {
    return this.index;
  }
  
  public int getByteSize()
  {
    return -1 == this.byteSize ? this.maxByteSize : Math.min(this.byteSize, this.maxByteSize);
  }
  
  public Field getField()
  {
    return this.field;
  }
  
  public Class<?> getFieldType()
  {
    return this.field.getType();
  }
  
  public boolean hasLength()
  {
    return null != this.lengthField;
  }
  
  public int getLength(Object owner)
  {
    if (null == owner) {
      return -1;
    }
    if (null == this.lengthField) {
      return -1;
    }
    this.lengthField.setAccessible(true);
    try
    {
      Object value = this.lengthField.get(owner);
      if (null == value) {
        return -1;
      }
      if ((value instanceof Long)) {
        return ((Long)value).intValue();
      }
      if ((value instanceof Integer)) {
        return ((Integer)value).intValue();
      }
      if ((value instanceof Short)) {
        return ((Short)value).intValue();
      }
      if ((value instanceof Byte)) {
        return ((Byte)value).intValue();
      }
      return -1;
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
      return -1;
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    return -1;
  }
  
  public int getStringLengthInBytes(Object owner)
  {
    return getLength(owner) * this.bytesPerChar;
  }
  
  public String getCharset()
  {
    return this.charset;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public int getFixedLength()
  {
    return this.fixedLength;
  }
}
