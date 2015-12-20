package net.flyingfat.common.telnet;

import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class TelnetServerHandler
  extends SimpleChannelUpstreamHandler
{
  private static final Logger logger = Logger.getLogger(TelnetServerHandler.class.getName());
  private ITelnetService telnetService;
  
  public void handleUpstream(ChannelHandlerContext ctx, ChannelEvent e)
    throws Exception
  {
    if ((e instanceof ChannelStateEvent)) {
      logger.info(e.toString());
    }
    super.handleUpstream(ctx, e);
  }
  
  public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
    throws Exception
  {
    e.getChannel().write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!" + System.getProperty("line.separator", "/n"));
    e.getChannel().write("It is " + new Date() + " now." + System.getProperty("line.separator", "/n"));
  }
  
  public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
  {
    String request = (String)e.getMessage();
    


    boolean close = false;
    String response;
    if (request.length() == 0)
    {
      response = "Please type something.";
    }
    else if (request.toLowerCase().equals("bye"))
    {
      response = "Have a good day!";
      close = true;
    }
    else
    {
      response = this.telnetService.telnet(request);
    }
    ChannelFuture future = e.getChannel().write(response + System.getProperty("line.separator", "/n"));
    if (close) {
      future.addListener(ChannelFutureListener.CLOSE);
    }
  }
  
  public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
  {
    logger.log(Level.WARNING, "Unexpected exception from downstream.", e.getCause());
    e.getChannel().close();
  }
  
  public ITelnetService getTelnetService()
  {
    return this.telnetService;
  }
  
  public void setTelnetService(ITelnetService telnetService)
  {
    this.telnetService = telnetService;
  }
}
