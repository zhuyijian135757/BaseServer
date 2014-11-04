package net.flyingfat.common.serialization.bytebean.field;

import java.lang.reflect.Field;

import net.flyingfat.common.serialization.bytebean.annotation.ByteField;

public class DefaultField2Desc
  implements Field2Desc
{
  public ByteFieldDesc genDesc(Field field)
  {
    ByteField byteField = (ByteField)field.getAnnotation(ByteField.class);
    Class<?> clazz = field.getDeclaringClass();
    if (null != byteField) {
      try
      {
        return new DefaultFieldDesc().setField(field).setIndex(byteField.index()).setByteSize(byteField.bytes()).setCharset(byteField.charset()).setLengthField(byteField.length().equals("") ? null : clazz.getDeclaredField(byteField.length())).setFixedLength(byteField.fixedLength());
      }
      catch (SecurityException e)
      {
        e.printStackTrace();
      }
      catch (NoSuchFieldException e)
      {
        e.printStackTrace();
      }
    }
    return null;
  }
}
