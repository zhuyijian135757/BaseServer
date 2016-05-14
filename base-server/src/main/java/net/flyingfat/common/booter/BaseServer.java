package net.flyingfat.common.booter;

import java.util.ArrayList;
import java.util.List;

import net.flyingfat.common.biz.MainCourse;
import net.flyingfat.common.dispatcher.course.BusinessCourse;
import net.flyingfat.common.dispatcher.receiver.SimpleDispatcher;
import net.flyingfat.common.http.HttpAcceptor;
import net.flyingfat.common.http.codec.HttpRequestDecoder;
import net.flyingfat.common.http.codec.HttpResponseEncoder;
import net.flyingfat.common.serialization.protocol.meta.DefaultMsgCode2TypeMetainfo;
import net.flyingfat.common.serialization.protocol.meta.MetainfoUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseServer {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseServer.class);

	public static void main(String[] args) {
		
		try {
			
			List<String> pkgs=new ArrayList<String>();
			pkgs.add("net.flyingfat.common.biz.xip.*");
			DefaultMsgCode2TypeMetainfo typeMetaInfo=MetainfoUtils.createTypeMetainfo(pkgs);
			
			HttpRequestDecoder reqDecoder=new HttpRequestDecoder();
			reqDecoder.setDebugEnabled(true);
			reqDecoder.setEncryptKey("__jDlog_".getBytes());
			reqDecoder.setTypeMetaInfo(typeMetaInfo);
			
			HttpResponseEncoder respEncoder=new HttpResponseEncoder();
			respEncoder.setDebugEnabled(true);
			respEncoder.setEncryptKey("__jDlog_".getBytes());
			
			SimpleDispatcher disPatcher=new SimpleDispatcher();
			disPatcher.setThreads(3);
			List<BusinessCourse> bCourse=new ArrayList<BusinessCourse>();
			bCourse.add(new MainCourse());
			disPatcher.setCourses(bCourse);
			
			HttpAcceptor httpAcceptor=new HttpAcceptor();
			httpAcceptor.setAcceptIp("0.0.0.0");
			httpAcceptor.setAcceptPort(8088);
			httpAcceptor.setIdleTime(300);
			httpAcceptor.setRequestDecoder(reqDecoder);
			httpAcceptor.setResponseEncoder(respEncoder);
			httpAcceptor.setMessageClosure(disPatcher);
			
			httpAcceptor.start();
			
			System.out.println("httpAcceptor started");
			
		} catch (Exception e) {
			logger.error("{}",e);
		}
		
	}

}
