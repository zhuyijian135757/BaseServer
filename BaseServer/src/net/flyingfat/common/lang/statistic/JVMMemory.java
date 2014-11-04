package net.flyingfat.common.lang.statistic;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class JVMMemory
{
  private final MemoryMXBean memoryMXBean;
  
  public JVMMemory()
  {
    this.memoryMXBean = ManagementFactory.getMemoryMXBean();
  }
  
  public long getHeapMemoryUsed()
  {
    return this.memoryMXBean.getHeapMemoryUsage().getUsed();
  }
  
  public long getHeapMemoryMax()
  {
    return this.memoryMXBean.getHeapMemoryUsage().getMax();
  }
  
  public long getHeapMemoryCommitted()
  {
    return this.memoryMXBean.getHeapMemoryUsage().getCommitted();
  }
  
  public long getHeapMemoryInit()
  {
    return this.memoryMXBean.getHeapMemoryUsage().getInit();
  }
  
  public double getHeapMemoryUsedInMBytes()
  {
    return getHeapMemoryUsed() / 1048576.0D;
  }
  
  public double getHeapMemoryMaxInMBytes()
  {
    return getHeapMemoryMax() / 1048576.0D;
  }
  
  public double getHeapMemoryCommittedInMBytes()
  {
    return getHeapMemoryCommitted() / 1048576.0D;
  }
  
  public double getHeapMemoryInitInMBytes()
  {
    return getHeapMemoryInit() / 1048576.0D;
  }
}
