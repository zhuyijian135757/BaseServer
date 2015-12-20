/*******************************************************************************
 * CopyRight (c) 2005-2013 flyingfat Ltd. All rights reserved. 
 * Filename: BaseCourse.java
 * Creator: terry.zhu
 * Version: 8:15:26 PM Aug 6, 2013
 * Description:
 *******************************************************************************/
package net.flyingfat.common.biz;

import net.flyingfat.common.biz.xip.BaseXipRequest;
import net.flyingfat.common.biz.xip.BaseXipResponse;
import net.flyingfat.common.dispatcher.course.BusinessCourse;
import net.flyingfat.common.http.TransportUtil;
import net.flyingfat.common.lang.transport.Sender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BaseCourse implements BusinessCourse {


  private Logger               logger = LoggerFactory.getLogger(BaseCourse.class);

  public void sendBaseNormalResponse(BaseXipRequest req, BaseXipResponse resp) {
    resp.setIdentification(req.getIdentification());
    Sender sender = TransportUtil.getSenderOf(req);
    sender.send(resp);
    if (logger.isDebugEnabled()) {
      logger.debug("<<<<<<<<<<Send {}=[{}]", resp.getClass().getSimpleName(), resp.toString());
    }
  }



}
