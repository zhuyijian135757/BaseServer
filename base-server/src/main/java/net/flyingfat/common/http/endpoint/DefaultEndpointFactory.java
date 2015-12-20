package net.flyingfat.common.http.endpoint;

import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.holder.DefaultHolder;
import net.flyingfat.common.lang.holder.Holder;
import net.flyingfat.common.lang.transport.Receiver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponse;

public class DefaultEndpointFactory
  implements EndpointFactory
{
  private Receiver messageClosure = null;
  private Holder responseContext = new DefaultHolder();
  
  public Endpoint createEndpoint(Channel channel, Transformer<Object, HttpResponse> responseEncoder)
  {
    ServerEndpoint endpoint = new ServerEndpoint();
    
    endpoint.setChannel(channel);
    endpoint.setMessageClosure(this.messageClosure);
    endpoint.setResponseContext(this.responseContext);
    endpoint.setResponseEncoder(responseEncoder);
    endpoint.start();
    
    return endpoint;
  }
  
  public void setMessageClosure(Receiver messageClosure)
  {
    this.messageClosure = messageClosure;
  }
  
  public void setResponseContext(Holder responseContext)
  {
    this.responseContext = responseContext;
  }
}
