package net.flyingfat.common.http.response;

import java.io.UnsupportedEncodingException;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;

public abstract interface HttpResponseSender
{
  public abstract void sendResponse(Channel paramChannel, HttpResponse paramHttpResponse);
  
  public abstract void sendResponse(Channel paramChannel, HttpResponseStatus paramHttpResponseStatus, String paramString);
  
  public abstract void sendResponse(Channel paramChannel, HttpResponseStatus paramHttpResponseStatus, String paramString1, String paramString2)
    throws UnsupportedEncodingException;
  
  public abstract void sendRedirectResponse(Channel paramChannel, String paramString);
  
  public abstract String sendFile(Channel paramChannel, byte[] paramArrayOfByte, int paramInt1, int paramInt2);
}
