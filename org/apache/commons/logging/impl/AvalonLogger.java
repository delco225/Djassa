package org.apache.commons.logging.impl;

import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.logging.Log;

public class AvalonLogger
  implements Log
{
  private static volatile Logger defaultLogger = null;
  private final transient Logger logger;
  
  public AvalonLogger(String paramString)
  {
    if (defaultLogger == null) {
      throw new NullPointerException("default logger has to be specified if this constructor is used!");
    }
    this.logger = defaultLogger.getChildLogger(paramString);
  }
  
  public AvalonLogger(Logger paramLogger)
  {
    this.logger = paramLogger;
  }
  
  public static void setDefaultLogger(Logger paramLogger)
  {
    defaultLogger = paramLogger;
  }
  
  public void debug(Object paramObject)
  {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(String.valueOf(paramObject));
    }
  }
  
  public void debug(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(String.valueOf(paramObject), paramThrowable);
    }
  }
  
  public void error(Object paramObject)
  {
    if (getLogger().isErrorEnabled()) {
      getLogger().error(String.valueOf(paramObject));
    }
  }
  
  public void error(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isErrorEnabled()) {
      getLogger().error(String.valueOf(paramObject), paramThrowable);
    }
  }
  
  public void fatal(Object paramObject)
  {
    if (getLogger().isFatalErrorEnabled()) {
      getLogger().fatalError(String.valueOf(paramObject));
    }
  }
  
  public void fatal(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isFatalErrorEnabled()) {
      getLogger().fatalError(String.valueOf(paramObject), paramThrowable);
    }
  }
  
  public Logger getLogger()
  {
    return this.logger;
  }
  
  public void info(Object paramObject)
  {
    if (getLogger().isInfoEnabled()) {
      getLogger().info(String.valueOf(paramObject));
    }
  }
  
  public void info(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isInfoEnabled()) {
      getLogger().info(String.valueOf(paramObject), paramThrowable);
    }
  }
  
  public boolean isDebugEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isErrorEnabled()
  {
    return getLogger().isErrorEnabled();
  }
  
  public boolean isFatalEnabled()
  {
    return getLogger().isFatalErrorEnabled();
  }
  
  public boolean isInfoEnabled()
  {
    return getLogger().isInfoEnabled();
  }
  
  public boolean isTraceEnabled()
  {
    return getLogger().isDebugEnabled();
  }
  
  public boolean isWarnEnabled()
  {
    return getLogger().isWarnEnabled();
  }
  
  public void trace(Object paramObject)
  {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(String.valueOf(paramObject));
    }
  }
  
  public void trace(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isDebugEnabled()) {
      getLogger().debug(String.valueOf(paramObject), paramThrowable);
    }
  }
  
  public void warn(Object paramObject)
  {
    if (getLogger().isWarnEnabled()) {
      getLogger().warn(String.valueOf(paramObject));
    }
  }
  
  public void warn(Object paramObject, Throwable paramThrowable)
  {
    if (getLogger().isWarnEnabled()) {
      getLogger().warn(String.valueOf(paramObject), paramThrowable);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.AvalonLogger
 * JD-Core Version:    0.7.0.1
 */