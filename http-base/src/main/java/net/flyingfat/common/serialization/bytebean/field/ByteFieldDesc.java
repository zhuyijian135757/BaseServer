package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;
import java.util.Comparator;

public abstract interface ByteFieldDesc
{
  public static final Comparator<ByteFieldDesc> comparator = new Comparator<ByteFieldDesc>()
  {
    public int compare(ByteFieldDesc desc1, ByteFieldDesc desc2)
    {
      int ret = desc1.getIndex() - desc2.getIndex();
      if (0 == ret) {
        throw new RuntimeException("field1:" + desc1.getField() + "/field2:" + desc2.getField() + " has the same index value, internal error.");
      }
      return ret;
    }

  };
  
  public abstract int getIndex();
  
  public abstract int getByteSize();
  
  public abstract Field getField();
  
  public abstract Class<?> getFieldType();
  
  public abstract boolean hasLength();
  
  public abstract int getLength(Object paramObject);
  
  public abstract int getStringLengthInBytes(Object paramObject);
  
  public abstract String getCharset();
  
  public abstract String getDescription();
  
  public abstract int getFixedLength();
  
}
