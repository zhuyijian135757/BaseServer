package net.flyingfat.common.serialization.bytebean;

public class ByteBeanUtil
{
  public static int type2DefaultByteSize(Class<?> type)
  {
    int ret = -1;
    if ((type.equals(Byte.TYPE)) || (type.equals(Byte.class))) {
      ret = 1;
    } else if ((type.equals(Short.TYPE)) || (type.equals(Short.class))) {
      ret = 2;
    } else if ((type.equals(Integer.TYPE)) || (type.equals(Integer.class))) {
      ret = 4;
    } else if ((type.equals(Long.TYPE)) || (type.equals(Long.class))) {
      ret = 8;
    } else if ((type.equals(Float.TYPE)) || (type.equals(Float.class))) {
      ret = 4;
    } else if ((type.equals(Double.TYPE)) || (type.equals(Double.class))) {
      ret = 8;
    } else {
      ret = -1;
    }
    return ret;
  }
}
