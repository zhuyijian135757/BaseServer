package net.flyingfat.common.http;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import net.flyingfat.common.lang.Holder;
import net.flyingfat.common.lang.KeyTransformer;
import net.flyingfat.common.lang.transport.DefaultHolder;
import net.flyingfat.common.lang.transport.Receiver;
import net.flyingfat.common.lang.transport.Sender;
import net.flyingfat.common.lang.transport.SenderSync;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelDownstreamHandler;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ChannelUpstreamHandler;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.HttpChunkAggregator;
import org.jboss.netty.handler.codec.http.HttpClientCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnector
  implements Sender, SenderSync
{
  private final Logger logger = LoggerFactory.getLogger(HttpConnector.class);
  private String name = "HTTPConnector";
  private String destIp = null;
  private int destPort = -1;
  private ClientBootstrap bootstrap;
  private ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
  private ChannelDownstreamHandler encoder;
  private ChannelUpstreamHandler decoder;
  private Channel channel;
  private Receiver messageClosure;
  private boolean keepAlive = false;
  private long retryTimeout = 1000L;
  private KeyTransformer keyTransformer = new KeyTransformer();
  private Holder context = null;
  private int waitTimeout = 10000;
  private int maxContentLength = 104857600;
  
  class ResponseFuture<V>
    extends FutureTask<V>
  {
   
   public ResponseFuture()
   {
        super(new Callable<V>() {
			@Override
			public V call() throws Exception {
				return null;
			}
		});
   }

	public void set(V v)
    {
      super.set(v);
    }
  }
  
  public HttpConnector(String name)
  {
    this.name = name;
    this.bootstrap = new ClientBootstrap();
  }
  
  public void start()
  {
    if ((null == this.destIp) || (this.destIp.equals("")))
    {
      this.logger.warn(" destIp is null, disable this connector.");
      return;
    }
    this.bootstrap.setFactory(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    
    this.bootstrap.setPipelineFactory(new ChannelPipelineFactory()
    {
      public ChannelPipeline getPipeline()
        throws Exception
      {
        ChannelPipeline pipeline = new DefaultChannelPipeline();
        pipeline.addLast("codec", new HttpClientCodec());
        pipeline.addLast("aggregator", new HttpChunkAggregator(HttpConnector.this.maxContentLength));
        
        pipeline.addLast("nettyEncoder", HttpConnector.this.encoder);
        pipeline.addLast("nettyDecoder", HttpConnector.this.decoder);
        
        pipeline.addLast("handler", new HttpResponseHandler());
        return pipeline;
      }
    });
    this.bootstrap.setOption("tcpNoDelay", Boolean.valueOf(true));
    this.bootstrap.setOption("keepAlive", Boolean.valueOf(this.keepAlive));
    
    doConnect();
  }
  
  public void stop()
  {
    this.exec.shutdownNow();
    this.channel.disconnect();
    this.bootstrap.releaseExternalResources();
  }
  
  public void send(Object message)
  {
    if (message != null) {
      if (this.channel != null) {
        this.channel.write(message);
      } else {
        this.logger.warn("missing channel, message droped.", message);
      }
    }
  }
  
  public void send(Object bean, Receiver receiver)
  {
    if (null != bean) {
      if (this.channel != null)
      {
        Object key = this.keyTransformer.transform(bean);
        getContext().put(key, receiver);
        this.channel.write(bean);
      }
      else
      {
        this.logger.warn("missing channel, message droped.", bean);
      }
    }
  }
  
  public Object sendAndWait(Object bean)
  {
    return sendAndWait(bean, this.waitTimeout, TimeUnit.MILLISECONDS);
  }
  
  public Object sendAndWait(Object bean, long duration, TimeUnit units)
  {
    if (null == bean) {
      return null;
    }
    if (this.channel != null)
    {
      Object key = this.keyTransformer.transform(bean);
      ResponseFuture responseFuture = new ResponseFuture();
      getContext().put(key, responseFuture);
      
      this.channel.write(bean);
      try
      {
        return responseFuture.get(duration, units);
      }
      catch (Exception e)
      {
        this.logger.error("", e);
        return null;
      }
      finally
      {
        responseFuture = (ResponseFuture)getContext().getAndRemove(key);
        if (responseFuture != null) {
          responseFuture.cancel(false);
        }
      }
    }
    this.logger.warn("missing channel, message droped.", bean);
    return null;
  }
  
  private class HttpResponseHandler
    extends SimpleChannelUpstreamHandler
  {
    private final Logger logger = LoggerFactory.getLogger(HttpResponseHandler.class);
    
    private HttpResponseHandler() {}
    
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e)
      throws Exception
    {
      this.logger.debug("channelConnected: " + e.getChannel());
      HttpConnector.this.channel = e.getChannel();
    }
    
    public void channelDisconnected(ChannelHandlerContext ctx, final ChannelStateEvent e)
      throws Exception
    {
      this.logger.debug("channelDisconnected: " + e.getChannel());
      HttpConnector.this.channel = null;
      HttpConnector.this.exec.submit(new Runnable()
      {
        public void run()
        {
          HttpConnector.this.onSessionClosed(e.getChannel());
        }
      });
    }
    
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
      throws Exception
    {
      this.logger.error("exceptionCaught:", e.getCause());
      e.getChannel().close();
    }
    
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
      throws Exception
    {
      if (this.logger.isTraceEnabled()) {
        this.logger.trace("messageReceived: " + e.getMessage());
      }
      Object key = HttpConnector.this.keyTransformer.transform(e.getMessage());
      if (key != null)
      {
        Object context = HttpConnector.this.getContext().getAndRemove(key);
        if (null != context) {
          try
          {
            if ((context instanceof HttpConnector.ResponseFuture)) {
              ((HttpConnector.ResponseFuture)context).set(e.getMessage());
            }
            if ((context instanceof Receiver))
            {
              ((Receiver)context).messageReceived(e.getMessage());
              return;
            }
          }
          catch (Exception e1)
          {
            this.logger.error("messageReceived error.", e1);
          }
        } else if (null != HttpConnector.this.messageClosure) {
          HttpConnector.this.messageClosure.messageReceived(e.getMessage());
        } else {
          this.logger.warn("missing closure, ignore incoming msg:" + e.getMessage());
        }
      }
    }
  }
  
  private void onSessionClosed(Channel channel)
  {
    if (this.logger.isInfoEnabled()) {
      this.logger.info(getName() + " channel : " + channel + " closed, retry connect...");
    }
    doConnect();
  }
  
  private void doConnect()
  {
    if ((null == this.destIp) || (this.destIp.equals("")))
    {
      this.logger.warn(getName() + " destIp is null, disable this connector.");
      return;
    }
    ChannelFuture connectFuture = this.bootstrap.connect(new InetSocketAddress(this.destIp, this.destPort));
    connectFuture.addListener(new ChannelFutureListener()
    {
      public void operationComplete(final ChannelFuture connectFuture)
        throws Exception
      {
        HttpConnector.this.exec.submit(new Runnable()
        {
          public void run()
          {
            HttpConnector.this.onConnectComplete(connectFuture);
          }
        });
      }
    });
  }
  
  private void onConnectComplete(ChannelFuture connectFuture)
  {
    if (!connectFuture.isSuccess())
    {
      if (this.logger.isInfoEnabled()) {
        this.logger.info(getName() + " connect [" + this.destIp + ":" + this.destPort + "] failed, retry...");
      }
      this.exec.schedule(new Runnable()
      {
        public void run()
        {
          HttpConnector.this.doConnect();
        }
      }, this.retryTimeout, TimeUnit.MILLISECONDS);
    }
  }
  
  public void setMessageClosure(Receiver messageClosure)
  {
    this.messageClosure = messageClosure;
  }
  
  public void setRetryTimeout(long retryTimeout)
  {
    this.retryTimeout = retryTimeout;
  }
  
  public Channel getChannel()
  {
    return this.channel;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setDestIp(String destIp)
  {
    this.destIp = destIp;
  }
  
  public void setDestPort(int destPort)
  {
    this.destPort = destPort;
  }
  
  public void setKeepAlive(boolean keepAlive)
  {
    this.keepAlive = keepAlive;
  }
  
  public void setEncoder(ChannelDownstreamHandler encoder)
  {
    this.encoder = encoder;
  }
  
  public void setDecoder(ChannelUpstreamHandler decoder)
  {
    this.decoder = decoder;
  }
  
  public void setContext(Holder context)
  {
    this.context = context;
  }
  
  public Holder getContext()
  {
    if (this.context == null) {
      this.context = new DefaultHolder();
    }
    return this.context;
  }
  
  public void setKeyTransformer(KeyTransformer keyTransformer)
  {
    this.keyTransformer = keyTransformer;
  }
  
  public void setMaxContentLength(int maxContentLength)
  {
    this.maxContentLength = maxContentLength;
  }
}
