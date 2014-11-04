package net.flyingfat.common.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.flyingfat.common.biz.xip.ReportAdsActionReq;
import net.flyingfat.common.biz.xip.ReportAdsActionResp;
import net.flyingfat.common.dispatcher.course.BizMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AdsCourse extends BaseCourse {

  private static Logger     logger   = LoggerFactory.getLogger(AdsCourse.class);


  @BizMethod
  public void onReportAdsActionReq(ReportAdsActionReq req) {
    if (logger.isDebugEnabled()) {
      logger.debug("----------->receive report ads action req , req = [{}]", req.toString());
    }

    ReportAdsActionResp resp = new ReportAdsActionResp();
    this.sendBaseNormalResponse(req, resp);
    System.out.println("req info:"+req+" success resp");

  }

  
  
  
}
