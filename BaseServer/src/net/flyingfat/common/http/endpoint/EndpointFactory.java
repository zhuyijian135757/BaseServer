package net.flyingfat.common.http.endpoint;

import net.flyingfat.common.lang.Holder;
import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.transport.Receiver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpResponse;

public abstract interface EndpointFactory
{
  public abstract Endpoint createEndpoint(Channel paramChannel, Transformer<Object, HttpResponse> paramTransformer);
  
  public abstract void setMessageClosure(Receiver paramReceiver);
  
  public abstract void setResponseContext(Holder paramHolder);
}
