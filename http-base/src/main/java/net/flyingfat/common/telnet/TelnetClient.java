package net.flyingfat.common.telnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TelnetClient
{
  ClientBootstrap bootstrap = null;
  ChannelFuture future = null;
  
  public TelnetClient(String ip, int port)
  {
    this.bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    
    this.bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
    

    this.future = this.bootstrap.connect(new InetSocketAddress(ip, port));
  }
  
  /**
   * 这个方法返回的是上一次telnet命令是否成功
   * @param command
   * @return
   */
  public boolean telnet(String command)
  {
    Channel channel = this.future.awaitUninterruptibly().getChannel();
    if (!this.future.isSuccess())
    {
      this.future.getCause().printStackTrace();
      this.bootstrap.releaseExternalResources();
      return false;
    }
    ChannelFuture lastWriteFuture = null;
    

    lastWriteFuture = channel.write(command + System.getProperty("line.separator", "/n"));
    if (command.toLowerCase().equals("bye"))
    {
      channel.getCloseFuture().awaitUninterruptibly();
      if (lastWriteFuture != null) {
        lastWriteFuture.awaitUninterruptibly();
      }
      channel.close().awaitUninterruptibly();
      

      this.bootstrap.releaseExternalResources();
    }
    return lastWriteFuture.isSuccess();
  }
  
  public static void main(String[] args)
    throws Exception
  {
    if (args.length != 2)
    {
      System.err.println("Usage: " + TelnetClient.class.getSimpleName() + " <host> <port>");
      return;
    }
    String host = args[0];
    int port = Integer.parseInt(args[1]);
    

    ClientBootstrap bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    

    bootstrap.setPipelineFactory(new TelnetClientPipelineFactory());
    

    ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
    

    Channel channel = future.awaitUninterruptibly().getChannel();
    if (!future.isSuccess())
    {
      future.getCause().printStackTrace();
      bootstrap.releaseExternalResources();
      return;
    }
    ChannelFuture lastWriteFuture = null;
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    for (;;)
    {
      String line = in.readLine();
      if (line == null) {
        break;
      }
      lastWriteFuture = channel.write(line + "\r\n");
      if (line.toLowerCase().equals("bye"))
      {
        channel.getCloseFuture().awaitUninterruptibly();
        break;
      }
    }
    if (lastWriteFuture != null) {
      lastWriteFuture.awaitUninterruptibly();
    }
    channel.close().awaitUninterruptibly();
    

    bootstrap.releaseExternalResources();
  }
}
