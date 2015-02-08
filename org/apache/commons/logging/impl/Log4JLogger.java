package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger
  implements Log, Serializable
{
  private static final String FQCN;
  static Class class$org$apache$commons$logging$impl$Log4JLogger;
  static Class class$org$apache$log4j$Level;
  static Class class$org$apache$log4j$Priority;
  private static final long serialVersionUID = 5160705895411730424L;
  private static final Priority traceLevel;
  private volatile transient Logger logger = null;
  private final String name;
  
  static
  {
    Class localClass1;
    Class localClass2;
    label39:
    Class localClass3;
    if (class$org$apache$commons$logging$impl$Log4JLogger == null)
    {
      localClass1 = class$("org.apache.commons.logging.impl.Log4JLogger");
      class$org$apache$commons$logging$impl$Log4JLogger = localClass1;
      FQCN = localClass1.getName();
      if (class$org$apache$log4j$Priority != null) {
        break label80;
      }
      localClass2 = class$("org.apache.log4j.Priority");
      class$org$apache$log4j$Priority = localClass2;
      if (class$org$apache$log4j$Level != null) {
        break label87;
      }
      localClass3 = class$("org.apache.log4j.Level");
      class$org$apache$log4j$Level = localClass3;
    }
    for (;;)
    {
      if (localClass2.isAssignableFrom(localClass3)) {
        break label94;
      }
      throw new InstantiationError("Log4J 1.2 not available");
      localClass1 = class$org$apache$commons$logging$impl$Log4JLogger;
      break;
      label80:
      localClass2 = class$org$apache$log4j$Priority;
      break label39;
      label87:
      localClass3 = class$org$apache$log4j$Level;
    }
    for (;;)
    {
      try
      {
        label94:
        if (class$org$apache$log4j$Level != null) {
          continue;
        }
        localClass4 = class$("org.apache.log4j.Level");
        class$org$apache$log4j$Level = localClass4;
        localObject = (Priority)localClass4.getDeclaredField("TRACE").get(null);
      }
      catch (Exception localException)
      {
        Class localClass4;
        Object localObject = Level.DEBUG;
        continue;
      }
      traceLevel = (Priority)localObject;
      return;
      localClass4 = class$org$apache$log4j$Level;
    }
  }
  
  public Log4JLogger()
  {
    this.name = null;
  }
  
  public Log4JLogger(String paramString)
  {
    this.name = paramString;
    this.logger = getLogger();
  }
  
  public Log4JLogger(Logger paramLogger)
  {
    if (paramLogger == null) {
      throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
    }
    this.name = paramLogger.getName();
    this.logger = paramLogger;
  }
  
  static Class class$(String paramString)
  {
    try
    {
      Class localClass = Class.forName(paramString);
      return localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
    }
  }
  
  public void debug(Object paramObject)
  {
    getLogger().log(FQCN, Level.DEBUG, paramObject, null);
  }
  
  public void debug(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, Level.DEBUG, paramObject, paramThrowable);
  }
  
  public void error(Object paramObject)
  {
    getLogger().log(FQCN, Level.ERROR, paramObject, null);
  }
  
  public void error(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, Level.ERROR, paramObject, paramThrowable);
  }
  
  public void fatal(Object paramObject)
  {
    getLogger().log(FQCN, Level.FATAL, paramObject, null);
  }
  
  public void fatal(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, Level.FATAL, paramObject, paramThrowable);
  }
  
  public Logger getLogger()
  {
    Logger localLogger1 = this.logger;
    if (localLogger1 == null) {
      try
      {
        Logger localLogger2 = this.logger;
        if (localLogger2 == null)
        {
          localLogger2 = Logger.getLogger(this.name);
          this.logger = localLogger2;
        }
        return localLogger2;
      }
      finally {}
    }
    return localLogger1;
  }
  
  public void info(Object paramObject)
  {
    getLogger().log(FQCN, Level.INFO, paramObject, null);
  }
  
  public void info(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, Level.INFO, paramObject, paramThrowable);
  }
  
  public boolean isDebugEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isErrorEnabled()
  {
    return getLogger().isEnabledFor(Level.ERROR);
  }
  
  public boolean isFatalEnabled()
  {
    return getLogger().isEnabledFor(Level.FATAL);
  }
  
  public boolean isInfoEnabled()
  {
    return getLogger().isInfoEnabled();
  }
  
  public boolean isTraceEnabled()
  {
    return getLogger().isEnabledFor(traceLevel);
  }
  
  public boolean isWarnEnabled()
  {
    return getLogger().isEnabledFor(Level.WARN);
  }
  
  public void trace(Object paramObject)
  {
    getLogger().log(FQCN, traceLevel, paramObject, null);
  }
  
  public void trace(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, traceLevel, paramObject, paramThrowable);
  }
  
  public void warn(Object paramObject)
  {
    getLogger().log(FQCN, Level.WARN, paramObject, null);
  }
  
  public void warn(Object paramObject, Throwable paramThrowable)
  {
    getLogger().log(FQCN, Level.WARN, paramObject, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.Log4JLogger
 * JD-Core Version:    0.7.0.1
 */