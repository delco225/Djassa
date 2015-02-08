package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Set;
import org.apache.commons.logging.impl.NoOpLog;

public class LogSource
{
  protected static boolean jdk14IsAvailable = false;
  protected static boolean log4jIsAvailable;
  protected static Constructor logImplctor = null;
  protected static Hashtable logs;
  
  static
  {
    bool1 = true;
    logs = new Hashtable();
    log4jIsAvailable = false;
    for (;;)
    {
      try
      {
        if (Class.forName("org.apache.log4j.Logger") != null)
        {
          bool2 = bool1;
          log4jIsAvailable = bool2;
        }
      }
      catch (Throwable localThrowable1)
      {
        boolean bool2;
        Object localObject;
        log4jIsAvailable = false;
        continue;
        bool1 = false;
        continue;
      }
      try
      {
        if ((Class.forName("java.util.logging.Logger") == null) || (Class.forName("org.apache.commons.logging.impl.Jdk14Logger") == null)) {
          continue;
        }
        jdk14IsAvailable = bool1;
      }
      catch (Throwable localThrowable2)
      {
        jdk14IsAvailable = false;
        continue;
        try
        {
          if (!log4jIsAvailable) {
            break label152;
          }
          setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
          return;
        }
        catch (Throwable localThrowable4)
        {
          try
          {
            setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
            return;
          }
          catch (Throwable localThrowable5)
          {
            return;
          }
          if (!jdk14IsAvailable) {
            break label164;
          }
          setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
          return;
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
          return;
        }
      }
      localObject = null;
      try
      {
        localObject = System.getProperty("org.apache.commons.logging.log");
        if (localObject == null)
        {
          String str = System.getProperty("org.apache.commons.logging.Log");
          localObject = str;
        }
      }
      catch (Throwable localThrowable3)
      {
        continue;
      }
      if (localObject == null) {
        break label129;
      }
      try
      {
        setLogImplementation((String)localObject);
        return;
      }
      catch (Throwable localThrowable6)
      {
        try
        {
          setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
          return;
        }
        catch (Throwable localThrowable7)
        {
          return;
        }
      }
      bool2 = false;
    }
  }
  
  public static Log getInstance(Class paramClass)
  {
    return getInstance(paramClass.getName());
  }
  
  public static Log getInstance(String paramString)
  {
    Log localLog = (Log)logs.get(paramString);
    if (localLog == null)
    {
      localLog = makeNewLogInstance(paramString);
      logs.put(paramString, localLog);
    }
    return localLog;
  }
  
  public static String[] getLogNames()
  {
    return (String[])logs.keySet().toArray(new String[logs.size()]);
  }
  
  public static Log makeNewLogInstance(String paramString)
  {
    try
    {
      Object[] arrayOfObject = { paramString };
      localObject = (Log)logImplctor.newInstance(arrayOfObject);
      if (localObject == null) {
        localObject = new NoOpLog(paramString);
      }
      return localObject;
    }
    catch (Throwable localThrowable)
    {
      for (;;)
      {
        Object localObject = null;
      }
    }
  }
  
  public static void setLogImplementation(Class paramClass)
    throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException
  {
    Class[] arrayOfClass = new Class[1];
    arrayOfClass[0] = "".getClass();
    logImplctor = paramClass.getConstructor(arrayOfClass);
  }
  
  public static void setLogImplementation(String paramString)
    throws LinkageError, NoSuchMethodException, SecurityException, ClassNotFoundException
  {
    try
    {
      Class localClass = Class.forName(paramString);
      Class[] arrayOfClass = new Class[1];
      arrayOfClass[0] = "".getClass();
      logImplctor = localClass.getConstructor(arrayOfClass);
      return;
    }
    catch (Throwable localThrowable)
    {
      logImplctor = null;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogSource
 * JD-Core Version:    0.7.0.1
 */