package net.flyingfat.common.serialization.bytebean.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ByteField
{
  int index();
  
  int bytes() default -1;
  
  String length() default "";
  
  String charset() default "UTF-16";
  
  int fixedLength() default -1;
  
  String description() default "";
}
