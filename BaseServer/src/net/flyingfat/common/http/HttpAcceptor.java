package net.flyingfat.common.http;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import net.flyingfat.common.http.endpoint.DefaultEndpointFactory;
import net.flyingfat.common.http.endpoint.Endpoint;
import net.flyingfat.common.http.endpoint.EndpointFactory;
import net.flyingfat.common.http.reactor.ConstantResponseReactor;
import net.flyingfat.common.http.reactor.HttpReactor;
import net.flyingfat.common.http.response.ConstantResponse;
import net.flyingfat.common.lang.Holder;
import net.flyingfat.common.lang.Transformer;
import net.flyingfat.common.lang.transport.Receiver;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelException;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.socket.DefaultServerSocketChannelConfig;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.handler.codec.http.DefaultHttpRequest;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpServerCodec;
import org.jboss.netty.handler.timeout.IdleState;
import org.jboss.netty.handler.timeout.IdleStateAwareChannelUpstreamHandler;
import org.jboss.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpAcceptor
{
  private static final Logger logger = LoggerFactory.getLogger(HttpAcceptor.class);
  private static final int MAX_RETRY = 20;
  private static final long RETRY_TIMEOUT = 30000L;
  private ServerBootstrap bootstrap;
  private Channel channel;
  private String acceptIp = "0.0.0.0";
  private int acceptPort = 8080;
  private int idleTime = 30;
  private Transformer<HttpRequest, Object> requestDecoder = null;
  private Transformer<Object, HttpResponse> responseEncoder = null;
  private HttpReactor errorReactor = new ConstantResponseReactor(ConstantResponse.get400NobodyResponse());
  private EndpointFactory endpointFactory = new DefaultEndpointFactory();
  private int maxContentLength = 104857600;
  
  public HttpAcceptor()
  {
    this.bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
  }
  
  public void start()
    throws IOException
  {
    this.bootstrap.setPipelineFactory(new ChannelPipelineFactory()
    {
      public ChannelPipeline getPipeline()
        throws Exception
      {
        ChannelPipeline pipeline = new DefaultChannelPipeline();
        
        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpChunkAggregator(HttpAcceptor.this.maxContentLength));
        pipeline.addLast("handler", new HttpRequestHandler());
        return pipeline;
      }
    });
    this.bootstrap.setOption("allIdleTime", Integer.valueOf(this.idleTime));
    this.bootstrap.setOption("child.keepAlive", Boolean.valueOf(true));
    this.bootstrap.setOption("child.tcpNoDelay", Boolean.valueOf(true));
    this.bootstrap.setOption("child.soLinger", Integer.valueOf(-1));
    this.bootstrap.setOption("child.sendBufferSize", Integer.valueOf(-1));
    
    int retryCount = 0;
    boolean binded = false;
    do
    {
      try
      {
        this.channel = this.bootstrap.bind(new InetSocketAddress(this.acceptIp, this.acceptPort));
        binded = true;
      }
      catch (ChannelException e)
      {
        logger.warn("start failed : " + e + ", and retry...");
        

        retryCount++;
        if (retryCount >= 20) {
          throw e;
        }
        try
        {
          Thread.sleep(30000L);
        }
        catch (InterruptedException e1) {}
      }
    } while (!binded);
    DefaultServerSocketChannelConfig config = (DefaultServerSocketChannelConfig)this.channel.getConfig();
    config.setBacklog(10240);
    config.setReuseAddress(true);
    config.setReceiveBufferSize(1024);
    
    logger.info("start succeed in " + this.acceptIp + ":" + this.acceptPort);
  }
  
  public void stop()
  {
    if (null != this.channel)
    {
      this.channel.unbind();
      this.channel = null;
    }
  }
  
  private class HttpRequestHandler
    extends IdleStateAwareChannelUpstreamHandler
  {
    private final Logger logger = LoggerFactory.getLogger(HttpRequestHandler.class);
    
    private HttpRequestHandler() {}
    
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
      throws Exception
    {
      if (this.logger.isTraceEnabled()) {
        this.logger.trace("message received {}", e.getMessage());
      }
      DefaultHttpRequest request = (DefaultHttpRequest)e.getMessage();
      Object signal = HttpAcceptor.this.requestDecoder.transform(request);
      if (null != signal)
      {
        Endpoint endpoint = TransportUtil.getEndpointOfSession(e.getChannel());
        if (null != endpoint)
        {
          TransportUtil.attachSender(signal, endpoint);
          TransportUtil.attachRequest(signal, request);
          endpoint.messageReceived(signal);
        }
        else
        {
          this.logger.warn("missing endpoint, ignore incoming msg:", signal);
        }
      }
      else if (null != HttpAcceptor.this.errorReactor)
      {
        this.logger.error("content is null, try send back client empty HttpResponse.");
        HttpAcceptor.this.errorReactor.onHttpRequest(null, request);
      }
      else
      {
        this.logger.warn("Can not transform bean for req [" + request + "], and missing errorHandler.");
      }
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
      throws Exception
    {
      if (!(e.getCause() instanceof IOException)) {
        this.logger.error("exceptionCaught: ", e.getCause());
      }
      ctx.getChannel().close();
    }
    
    public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e)
      throws Exception
    {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("channelClosed: [" + e.getChannel().getRemoteAddress() + "]");
      }
      Endpoint endpoint = TransportUtil.getEndpointOfSession(e.getChannel());
      if (null != endpoint) {
        endpoint.stop();
      }
      TransportUtil.detachEndpointToSession(e.getChannel());
    }
    
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e)
      throws Exception
    {
      if (this.logger.isDebugEnabled()) {
        this.logger.debug("channelOpen: [" + e.getChannel().getRemoteAddress() + "]");
      }
      Endpoint endpoint = HttpAcceptor.this.endpointFactory.createEndpoint(e.getChannel(), HttpAcceptor.this.responseEncoder);
      if (null != endpoint)
      {
        TransportUtil.attachEndpointToSession(e.getChannel(), endpoint);
        endpoint.setAddr((InetSocketAddress)e.getChannel().getRemoteAddress());
      }
    }
    
    public void channelIdle(ChannelHandlerContext ctx, IdleStateEvent e)
      throws Exception
    {
      if (this.logger.isInfoEnabled()) {
        this.logger.info("channelIdle: " + e.getState().name() + " for " + (System.currentTimeMillis() - e.getLastActivityTimeMillis()) + " milliseconds, close channel[" + e.getChannel().getRemoteAddress() + "]");
      }
      e.getChannel().close();
    }
  }
  
  public void setMaxContentLength(int maxContentLength)
  {
    this.maxContentLength = maxContentLength;
  }
  
  public void setAcceptIp(String acceptIp)
  {
    this.acceptIp = acceptIp;
  }
  
  public void setAcceptPort(int acceptPort)
  {
    this.acceptPort = acceptPort;
  }
  
  public void setIdleTime(int idleTime)
  {
    this.idleTime = idleTime;
  }
  
  public void setRequestDecoder(Transformer<HttpRequest, Object> requestDecoder)
  {
    this.requestDecoder = requestDecoder;
  }
  
  public void setErrorReactor(HttpReactor errorReactor)
  {
    this.errorReactor = errorReactor;
  }
  
  public void setMessageClosure(Receiver messageClosure)
  {
    this.endpointFactory.setMessageClosure(messageClosure);
  }
  
  public void setResponseContext(Holder responseContext)
  {
    this.endpointFactory.setResponseContext(responseContext);
  }
  
  public void setEndpointFactory(EndpointFactory endpointFactory)
  {
    this.endpointFactory = endpointFactory;
  }
  
  public void setResponseEncoder(Transformer<Object, HttpResponse> responseEncoder)
  {
    this.responseEncoder = responseEncoder;
  }
}
