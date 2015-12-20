package net.flyingfat.common.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.flyingfat.common.biz.xip.ReportActionReq;
import net.flyingfat.common.biz.xip.ReportActionResp;
import net.flyingfat.common.dispatcher.course.BizMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainCourse extends BaseCourse {

  private static Logger     logger   = LoggerFactory.getLogger(MainCourse.class);


  @BizMethod
  public void onReportAdsActionReq(ReportActionReq req) {
    if (logger.isDebugEnabled()) {
      logger.debug("----------->receive action req , req = [{}]", req.toString());
    }

    ReportActionResp resp = new ReportActionResp();
    this.sendBaseNormalResponse(req, resp);
    System.out.println("req info:"+req+" success resp");

  }

  
  
  
}
