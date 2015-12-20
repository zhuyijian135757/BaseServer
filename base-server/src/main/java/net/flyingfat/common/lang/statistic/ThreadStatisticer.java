package net.flyingfat.common.lang.statistic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;

public class ThreadStatisticer
{
  private ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
  private int stackDepth = 20;
  
  public void setContentionTracing(boolean val)
  {
    this.threadBean.setThreadContentionMonitoringEnabled(val);
  }
  
  public void setStackDepth(int stackDepth)
  {
    this.stackDepth = stackDepth;
  }
  
  public String getThreadInfo()
  {
    boolean contention = this.threadBean.isThreadContentionMonitoringEnabled();
    long[] threadIds = this.threadBean.getAllThreadIds();
    
    StringBuffer buffer = new StringBuffer("Process Thread Dump: ");
    buffer.append("\n");
    buffer.append(threadIds.length + " active threads");
    buffer.append("\n");
    for (long tid : threadIds)
    {
      ThreadInfo info = this.threadBean.getThreadInfo(tid, this.stackDepth);
      if (info == null)
      {
        buffer.append("  Inactive");
        buffer.append("\n");
      }
      else
      {
        buffer.append("Thread " + getTaskName(info.getThreadId(), info.getThreadName()) + ":");
        

        buffer.append("\n");
        Thread.State state = info.getThreadState();
        buffer.append("  State: " + state);
        buffer.append("\n");
        buffer.append("  Blocked count: " + info.getBlockedCount());
        buffer.append("\n");
        buffer.append("  Waited count: " + info.getWaitedCount());
        buffer.append("\n");
        if (contention)
        {
          buffer.append("  Blocked time: " + info.getBlockedTime());
          buffer.append("\n");
          buffer.append("  Waited time: " + info.getWaitedTime());
          buffer.append("\n");
        }
        if (state == Thread.State.WAITING)
        {
          buffer.append("  Waiting on " + info.getLockName());
          buffer.append("\n");
        }
        else if (state == Thread.State.BLOCKED)
        {
          buffer.append("  Blocked on " + info.getLockName());
          buffer.append("\n");
          buffer.append("  Blocked by " + getTaskName(info.getLockOwnerId(), info.getLockOwnerName()));
          

          buffer.append("\n");
        }
        buffer.append("  Stack:");
        buffer.append("\n");
        for (StackTraceElement frame : info.getStackTrace())
        {
          buffer.append("    " + frame.toString());
          buffer.append("\n");
        }
      }
    }
    return buffer.toString();
  }
  
  private String getTaskName(long id, String name)
  {
    if (name == null) {
      return Long.toString(id);
    }
    return id + " (" + name + ")";
  }
}
