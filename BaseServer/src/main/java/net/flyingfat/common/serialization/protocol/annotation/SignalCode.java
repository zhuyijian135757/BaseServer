package net.flyingfat.common.serialization.protocol.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SignalCode
{
  int messageCode();
}
