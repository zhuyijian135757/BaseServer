package net.flyingfat.common.lang;

public abstract interface Transformer<FROM, TO>
{
  public abstract TO transform(FROM paramFROM);
}
