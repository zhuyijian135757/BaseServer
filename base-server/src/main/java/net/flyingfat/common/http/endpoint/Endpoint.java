package net.flyingfat.common.http.endpoint;

import java.net.InetSocketAddress;

import net.flyingfat.common.lang.IpPortPair;
import net.flyingfat.common.lang.transport.Receiver;
import net.flyingfat.common.lang.transport.Sender;

import org.jboss.netty.channel.Channel;

public abstract interface Endpoint
  extends Sender, Receiver
{
  public abstract void stop();
  
  public abstract void start();
  
  public abstract void setChannel(Channel paramChannel);
  
  public abstract IpPortPair getRemoteAddress();
  
  public abstract void setAddr(InetSocketAddress paramInetSocketAddress);
}
