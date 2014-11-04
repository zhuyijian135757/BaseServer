package net.flyingfat.common.lang;

import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunctorAsync
  implements Closure
{
  private static final Logger logger = LoggerFactory.getLogger(FunctorAsync.class);
  private Executor exec;
  private Closure functor;
  
  public void execute(final Object... args)
  {
    this.exec.execute(new Runnable()
    {
      public void run()
      {
        try
        {
          FunctorAsync.this.functor.execute(args);
        }
        catch (Exception e)
        {
          FunctorAsync.logger.error("execute:", e);
        }
      }
    });
  }
  
  public void setExec(Executor exec)
  {
    this.exec = exec;
  }
  
  public void setFunctor(Closure functor)
  {
    this.functor = functor;
  }
  
  public boolean equals(Object obj)
  {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    FunctorAsync other = (FunctorAsync)obj;
    if (this.functor == null)
    {
      if (other.functor != null) {
        return false;
      }
    }
    else if (!this.functor.equals(other.functor)) {
      return false;
    }
    return true;
  }
}
