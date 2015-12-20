package net.flyingfat.common.biz.test;
/*******************************************************************************
 * CopyRight (c) 2005-2013 flyingfat Ltd. All rights reserved. 
 * Filename: AdsTestCase.java 
 * Creator: terry.zhu
 * Version: 1.0 Date: Jan. 3, 2014 5:15:08 PM
 * Description:
 *******************************************************************************/

import java.util.ArrayList;
import java.util.List;
import net.flyingfat.common.biz.xip.ReportActionReq;
import net.flyingfat.common.biz.xip.ReportActionResp;
import net.flyingfat.common.http.HttpConnector;
import net.flyingfat.common.http.codec.HttpRequestEncoder;
import net.flyingfat.common.http.codec.HttpResponseDecoder;
import net.flyingfat.common.serialization.protocol.meta.DefaultMsgCode2TypeMetainfo;
import net.flyingfat.common.serialization.protocol.meta.MetainfoUtils;
import org.junit.Test;

import junit.framework.TestCase;


public class MainTestCase extends TestCase {


  @Test
  public void testReportAdsActionReq() {
	  
	  
	 List<String> pkgs=new ArrayList<String>();
	 pkgs.add("net.flyingfat.common.biz.xip.*");
	 DefaultMsgCode2TypeMetainfo typeMetaInfo=MetainfoUtils.createTypeMetainfo(pkgs);
  
	 HttpRequestEncoder reqEncoder=new HttpRequestEncoder();
	 reqEncoder.setDebugEnabled(true);
	 reqEncoder.setEncryptKey("__jDlog_".getBytes());
		
	 HttpResponseDecoder respDecoder=new HttpResponseDecoder();
	 respDecoder.setDebugEnabled(true);
	 respDecoder.setEncryptKey("__jDlog_".getBytes());
	 respDecoder.setTypeMetaInfo(typeMetaInfo);
	  
	  HttpConnector httpConnector=new HttpConnector("connector");  
	  httpConnector.setDestIp("127.0.0.1");
	  httpConnector.setDestPort(8088);
	  httpConnector.setKeepAlive(false);
	  httpConnector.setDecoder(respDecoder);
	  httpConnector.setEncoder(reqEncoder);
	  httpConnector.start();
	  
	  ArrayList<String> list=new ArrayList<String>();
	  list.add("xxx");
	  ReportActionReq req=new ReportActionReq();
	  req.setUid("ddd");
	  req.setId(1l);
	  req.setOrderId(10);
	  req.setList(list);
	  
	  ReportActionResp resp = (ReportActionResp) httpConnector.sendAndWait(req);
	  System.out.println(resp.toString());
	  
	  
    /*ReportAdsActionReq req = new ReportAdsActionReq();
    req.setIdentification(UUID.randomUUID());
    HttpConnector sender = (HttpConnector) ctx.getBean("connector_test");

    ReportAdsActionResp resp = (ReportAdsActionResp) sender.sendAndWait(req);
    System.out.println(resp.toString());*/
  }

  
}
