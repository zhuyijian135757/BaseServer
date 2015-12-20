package net.flyingfat.common.telnet;

import java.nio.charset.Charset;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

public class TelnetServerPipelineFactory
  implements ChannelPipelineFactory
{
  private TelnetServerHandler telnetServerHandler;
  
  public TelnetServerPipelineFactory(TelnetServerHandler telnetServerHandler)
  {
    this.telnetServerHandler = telnetServerHandler;
  }
  
  public ChannelPipeline getPipeline()
    throws Exception
  {
    ChannelPipeline pipeline = Channels.pipeline();
    

    pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("encoder", new StringEncoder(Charset.forName("utf-8")));
    

    pipeline.addLast("handler", this.telnetServerHandler);
    
    return pipeline;
  }
}
