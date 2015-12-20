package net.flyingfat.common.http.endpoint;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import net.flyingfat.common.http.TransportUtil;
import net.flyingfat.common.http.codec.HttpResponseEncoder;
import net.flyingfat.common.http.response.DefaultHttpResponseSender;
import net.flyingfat.common.http.response.HttpResponseSender;
import net.flyingfat.common.lang.Holder;
import net.flyingfat.common.lang.IpPortPair;
import net.flyingfat.common.lang.KeyTransformer;
import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.transport.Receiver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEndpoint
  implements Endpoint
{
  private static final Logger logger = LoggerFactory.getLogger(ServerEndpoint.class);
  private Receiver messageClosure = null;
  private Holder responseContext = null;
  private KeyTransformer keyTransformer = new KeyTransformer();
  private Channel channel = null;
  private HttpResponseSender httpResponseSender = new DefaultHttpResponseSender();
  private Transformer<Object, HttpResponse> responseEncoder = new HttpResponseEncoder();
  private InetSocketAddress addr = null;
  
  public void send(Object bean)
  {
    if (null != bean)
    {
      Object key = this.keyTransformer.transform(bean);
      if (key == null) {
        return;
      }
      if (this.responseContext == null)
      {
        logger.warn("responseContext is null");
        return;
      }
      HttpRequest req = (HttpRequest)getResponseContext().getAndRemove(key);
      if (req == null) {
        return;
      }
      TransportUtil.attachRequest(bean, req);
      
      doSend(bean);
    }
  }
  
  public void send(Object bean, Receiver receiver)
  {
    throw new UnsupportedOperationException("not implemented yet!");
  }
  
  public void messageReceived(Object msg)
  {
    Object key = this.keyTransformer.transform(msg);
    if (key != null) {
      getResponseContext().put(key, TransportUtil.getRequestOf(msg));
    }
    if (this.messageClosure != null) {
      this.messageClosure.messageReceived(msg);
    }
  }
  
  public void stop()
  {
    this.responseContext = null;
    this.messageClosure = null;
    this.channel = null;
  }
  
  public void start() {}
  
  private void doSend(Object bean)
  {
    if (bean != null)
    {
      HttpResponse response = (HttpResponse)this.responseEncoder.transform(bean);
      this.httpResponseSender.sendResponse(this.channel, response);
    }
  }
  
  public void setChannel(Channel channel)
  {
    this.channel = channel;
  }
  
  public void setResponseEncoder(Transformer<Object, HttpResponse> responseEncoder)
  {
    this.responseEncoder = responseEncoder;
  }
  
  public void setMessageClosure(Receiver messageClosure)
  {
    this.messageClosure = messageClosure;
  }
  
  public void setResponseContext(Holder responseContext)
  {
    this.responseContext = responseContext;
  }
  
  public Holder getResponseContext()
  {
    return this.responseContext;
  }
  
  public void setKeyTransformer(KeyTransformer keyTransformer)
  {
    this.keyTransformer = keyTransformer;
  }
  
  public IpPortPair getRemoteAddress()
  {
    if (this.addr != null) {
      return new IpPortPair(this.addr.getAddress().getHostAddress(), this.addr.getPort());
    }
    InetSocketAddress addr = (InetSocketAddress)this.channel.getRemoteAddress();
    return new IpPortPair(addr.getAddress().getHostAddress(), addr.getPort());
  }
  
  public void setAddr(InetSocketAddress addr)
  {
    this.addr = addr;
  }
}
