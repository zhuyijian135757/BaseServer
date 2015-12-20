package net.flyingfat.common.lang;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JVMUtil
{
  public static boolean appendtoClassPath(String name)
  {
    try
    {
      ClassLoader clsLoader = ClassLoader.getSystemClassLoader();
      Method appendToClassPathMethod = clsLoader.getClass().getDeclaredMethod("appendToClassPathForInstrumentation", new Class[] { String.class });
      if (null != appendToClassPathMethod)
      {
        appendToClassPathMethod.setAccessible(true);
        appendToClassPathMethod.invoke(clsLoader, new Object[] { name });
      }
      return true;
    }
    catch (SecurityException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchMethodException e)
    {
      e.printStackTrace();
    }
    catch (IllegalArgumentException e)
    {
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      e.printStackTrace();
    }
    catch (InvocationTargetException e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public static String[] addAllJarsToClassPath(String dirName)
  {
    List<String> ret = new ArrayList();
    
    File dir = new File(dirName);
    if (dir.isDirectory())
    {
      File[] files = dir.listFiles();
      for (File file : files) {
        if (file.isDirectory())
        {
          ret.addAll(Arrays.asList(addAllJarsToClassPath(file.getAbsolutePath())));
        }
        else
        {
          String filename = file.getName().toLowerCase();
          if ((filename.endsWith(".jar")) && 
            (appendtoClassPath(file.getAbsolutePath()))) {
            ret.add(file.getAbsolutePath());
          }
        }
      }
    }
    return (String[])ret.toArray(new String[0]);
  }
}
