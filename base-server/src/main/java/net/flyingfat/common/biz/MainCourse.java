package net.flyingfat.common.biz;

import net.flyingfat.common.biz.xip.SimpleBizReq;
import net.flyingfat.common.biz.xip.SimpleBizResp;
import net.flyingfat.common.dispatcher.course.BizMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MainCourse extends BaseCourse {

  private static Logger     logger   = LoggerFactory.getLogger(MainCourse.class);


  @BizMethod
  public void onSimpleBizReq(SimpleBizReq req) {
    if (logger.isDebugEnabled()) {
      logger.debug("----------->receive action req , req = [{}]", req.toString());
    }

    SimpleBizResp resp = new SimpleBizResp();
    this.sendBaseNormalResponse(req, resp);
    System.out.println("req info:"+req+" success resp");
  }

  
  
  
}
