package net.flyingfat.common.serialization.bytebean.context;

public class DecResult
{
  private Object value;
  private byte[] bytes;
  
  public DecResult(Object value, byte[] bytes)
  {
    this.value = value;
    this.bytes = bytes;
  }
  
  public Object getValue()
  {
    return this.value;
  }
  
  public byte[] getRemainBytes()
  {
    return this.bytes;
  }
}
