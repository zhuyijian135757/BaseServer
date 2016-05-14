package net.flyingfat.common.telnet;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.flyingfat.common.http.ServerBootstrap;

import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class TelnetServer
{
  private ServerBootstrap bootstrap;
  private ITelnetService telnetService;
  public int acceptPort;
  public String acceptIp;
  private TelnetServerHandler telnetServerHandler;
  
  public TelnetServer()
  {
    this.bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
  }
  
  public void start()
  {
    this.telnetServerHandler = new TelnetServerHandler();
    this.telnetServerHandler.setTelnetService(this.telnetService);
    this.bootstrap.setPipelineFactory(new TelnetServerPipelineFactory(this.telnetServerHandler));
    this.bootstrap.setOption("allIdleTime", Integer.valueOf(300));
    this.bootstrap.setOption("child.keepAlive", Boolean.valueOf(true));
    
    this.bootstrap.bind(new InetSocketAddress(this.acceptIp, this.acceptPort));
  }
  
  public void setTelnetService(ITelnetService telnetService)
  {
    this.telnetService = telnetService;
  }
  
  public void setAcceptPort(int acceptPort)
  {
    this.acceptPort = acceptPort;
  }
  
  public void setAcceptIp(String acceptIp)
  {
    this.acceptIp = acceptIp;
  }
}
