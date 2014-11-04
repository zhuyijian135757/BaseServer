package net.flyingfat.common.biz.xip;

import net.flyingfat.common.xip.AbstractXipResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


public class BaseXipResponse extends AbstractXipResponse {

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
  }
}
