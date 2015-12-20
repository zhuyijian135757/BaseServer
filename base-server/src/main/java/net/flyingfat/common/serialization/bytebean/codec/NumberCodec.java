package net.flyingfat.common.serialization.bytebean.codec;

public abstract interface NumberCodec
{
  public abstract String convertCharset(String paramString);
  
  public abstract byte[] short2Bytes(short paramShort, int paramInt);
  
  public abstract byte[] int2Bytes(int paramInt1, int paramInt2);
  
  public abstract byte[] long2Bytes(long paramLong, int paramInt);
  
  public abstract byte[] float2Bytes(float paramFloat, int paramInt);
  
  public abstract byte[] double2Bytes(double paramDouble, int paramInt);
  
  public abstract short bytes2Short(byte[] paramArrayOfByte, int paramInt);
  
  public abstract int bytes2Int(byte[] paramArrayOfByte, int paramInt);
  
  public abstract long bytes2Long(byte[] paramArrayOfByte, int paramInt);
  
  public abstract float bytes2Float(byte[] paramArrayOfByte, int paramInt);
  
  public abstract double bytes2Double(byte[] paramArrayOfByte, int paramInt);
}
