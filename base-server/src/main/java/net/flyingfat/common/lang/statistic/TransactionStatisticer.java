package net.flyingfat.common.lang.statistic;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionStatisticer
{
  private static class Metric
  {
    public AtomicLong handledTransaction = new AtomicLong(0L);
    public AtomicLong finishedTransaction = new AtomicLong(0L);
    public AtomicLong handledThroughput = new AtomicLong(0L);
    public AtomicLong finishedThroughput = new AtomicLong(0L);
    private long lastHandledTransaction = 0L;
    private long lastFinishedTransaction = 0L;
    
    public void calculatePerformance(long interval)
    {
      long handledTransactionNow = this.handledTransaction.get();
      long finishedTransactionNow = this.finishedTransaction.get();
      
      this.handledThroughput.set((int)((handledTransactionNow - this.lastHandledTransaction) * 1000L / interval));
      this.lastHandledTransaction = handledTransactionNow;
      
      this.finishedThroughput.set((int)((finishedTransactionNow - this.lastFinishedTransaction) * 1000L / interval));
      this.lastFinishedTransaction = finishedTransactionNow;
    }
  }
  
  private Metric metric = new Metric();
  private Timer timer = new Timer();
  private long caculateInterval = 1000L;
  private long lastTimestamp = 0L;
  
  private void calculatePerformance()
  {
    long now = System.currentTimeMillis();
    long interval = now - this.lastTimestamp;
    if (interval > 0L) {
      this.metric.calculatePerformance(interval);
    }
    this.lastTimestamp = now;
  }
  
  public void start()
  {
    this.timer.scheduleAtFixedRate(new TimerTask()
    {
      public void run()
      {
        TransactionStatisticer.this.calculatePerformance();
      }
    }, this.caculateInterval, this.caculateInterval);
  }
  
  public void stop()
  {
    this.timer.cancel();
  }
  
  public long getCaculateInterval()
  {
    return this.caculateInterval;
  }
  
  public void setCaculateInterval(long caculateInterval)
  {
    this.caculateInterval = (caculateInterval * 1000L);
  }
  
  public long getLastTimestamp()
  {
    return this.lastTimestamp;
  }
  
  public AtomicLong getHandledTransaction()
  {
    return this.metric.handledTransaction;
  }
  
  public AtomicLong getFinishedTransaction()
  {
    return this.metric.finishedTransaction;
  }
  
  public AtomicLong getHandledThroughput()
  {
    return this.metric.handledThroughput;
  }
  
  public AtomicLong getFinishedThroughput()
  {
    return this.metric.finishedThroughput;
  }
  
  public void incHandledTransactionEnd()
  {
    getFinishedTransaction().incrementAndGet();
  }
  
  public void incHandledTransactionStart()
  {
    getHandledTransaction().incrementAndGet();
  }
}
