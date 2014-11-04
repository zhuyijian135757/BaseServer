package net.flyingfat.common.http.reactor;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.handler.codec.http.HttpRequest;

public abstract interface HttpReactor
{
  public abstract void onHttpRequest(Channel paramChannel, HttpRequest paramHttpRequest);
}
