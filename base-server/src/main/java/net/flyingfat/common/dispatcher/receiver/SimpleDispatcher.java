package net.flyingfat.common.dispatcher.receiver;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.flyingfat.common.dispatcher.course.BizMethod;
import net.flyingfat.common.dispatcher.course.BusinessCourse;
import net.flyingfat.common.lang.ClassUtil;
import net.flyingfat.common.lang.SimpleCache;
import net.flyingfat.common.lang.statistic.TransactionStatisticer;
import net.flyingfat.common.lang.transport.Receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleDispatcher
  implements Receiver
{
  private static final Logger logger = LoggerFactory.getLogger(SimpleDispatcher.class);
  private Map<Class<?>, BusinessCourse> courseTable = new HashMap();
  private ExecutorService mainExecutor = Executors.newSingleThreadExecutor();
  private TransactionStatisticer statisticer = new TransactionStatisticer();
  private static final Method EMPTY_METHOD;
  
  private static final class Key
  {
    private Class<?> courseClass;
    private Class<?> beanClass;
    
    public Key(Class<?> courseClass, Class<?> beanClass)
    {
      this.courseClass = courseClass;
      this.beanClass = beanClass;
    }
    
    public int hashCode()
    {
      int prime = 31;
      int result = 1;
      result = 31 * result + (this.beanClass == null ? 0 : this.beanClass.hashCode());
      
      result = 31 * result + (this.courseClass == null ? 0 : this.courseClass.hashCode());
      
      return result;
    }
    
    public boolean equals(Object obj)
    {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Key)) {
        return false;
      }
      Key other = (Key)obj;
      if (this.beanClass == null)
      {
        if (other.beanClass != null) {
          return false;
        }
      }
      else if (!this.beanClass.equals(other.beanClass)) {
        return false;
      }
      if (this.courseClass == null)
      {
        if (other.courseClass != null) {
          return false;
        }
      }
      else if (!this.courseClass.equals(other.courseClass)) {
        return false;
      }
      return true;
    }
  }
  
  static
  {
    Method tmp = null;
    try
    {
      tmp = Key.class.getMethod("hashCode", new Class[0]);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    EMPTY_METHOD = tmp;
  }
  
  private SimpleCache<Key, Method> bizMethodCache = new SimpleCache<Key, Method>();
  
  private Method getBizMethod(final Class<?> courseClass, final Class<?> beanClass)
  {
    Method ret = (Method)bizMethodCache.get(new Key(courseClass, beanClass), new Callable<Method>()
    {
      public Method call()
        throws Exception
      {
        Method[] methods = ClassUtil.getAllMethodOf(courseClass);
        for (Method method : methods)
        {
          BizMethod biz = (BizMethod)method.getAnnotation(BizMethod.class);
          if (null != biz)
          {
            Class<?>[] params = method.getParameterTypes();
            if (params.length < 1)
            {
              if (SimpleDispatcher.logger.isWarnEnabled()) {
                SimpleDispatcher.logger.warn("Method [" + method.getName() + "] found but only [" + params.length + "] parameters found, need to be 1.");
              }
            }
            else if (params[0].isAssignableFrom(beanClass)) {
              return method;
            }
          }
        }
        return SimpleDispatcher.EMPTY_METHOD;
      }
    });
    return ret == EMPTY_METHOD ? null : ret;
  }
  
  public void messageReceived(final Object input)
  {
    Runnable task = new Runnable()
    {
      public void run()
      {
        Object message = input;
        
        BusinessCourse course = getCourse(message.getClass());
        if (course == null)
        {
          if (logger.isErrorEnabled()) {
            logger.error("No course class found for [" + message.getClass().getName() + "]. Process stopped.");
          }
          return;
        }
        try
        {
          if (statisticer != null) {
            statisticer.incHandledTransactionStart();
          }
          	invokeBizMethod(course, message);
          if (statisticer != null) {
              statisticer.incHandledTransactionEnd();
          }
        }
        catch (Exception e)
        {
          logger.error("biz error.", e);
        }
      }
    };
    if (mainExecutor != null) {
       mainExecutor.submit(task);
    } else {
      task.run();
    }
  }
  
  private void invokeBizMethod(BusinessCourse course, Object msg)
  {
    Method bizMethod = getBizMethod(course.getClass(), msg.getClass());
    if (null != bizMethod) {
      try
      {
        bizMethod.invoke(course, new Object[] { msg });
      }
      catch (Exception e)
      {
        if (logger.isErrorEnabled()) {
          logger.error("Invoke biz method [" + bizMethod.getName() + "] failed. ", e);
        }
      }
    } else if (logger.isErrorEnabled()) {
      logger.error("No biz method found for message [" + msg.getClass().getName() + "]. No process execute.");
    }
  }
  
  private BusinessCourse getCourse(Class<?> clazz)
  {
    return (BusinessCourse)this.courseTable.get(clazz);
  }
  
  public void setCourses(Collection<BusinessCourse> courses)
  {
    for (BusinessCourse course : courses)
    {
      Method[] methods = ClassUtil.getAllMethodOf(course.getClass());
      for (Method method : methods)
      {
        BizMethod biz = (BizMethod)method.getAnnotation(BizMethod.class);
        if (null != biz)
        {
          Class<?>[] params = method.getParameterTypes();
          if (params.length >= 1) {
            this.courseTable.put(params[0], course);
          }
        }
      }
    }
  }
  
  public void setThreads(int threads)
  {
    this.mainExecutor = Executors.newFixedThreadPool(threads);
  }
  
  public void setStatisticer(TransactionStatisticer statisticer)
  {
    this.statisticer = statisticer;
  }
}
