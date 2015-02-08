package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk14Logger
  implements Log, Serializable
{
  protected static final Level dummyLevel = Level.FINE;
  private static final long serialVersionUID = 4784713551416303804L;
  protected transient Logger logger = null;
  protected String name = null;
  
  public Jdk14Logger(String paramString)
  {
    this.name = paramString;
    this.logger = getLogger();
  }
  
  public void debug(Object paramObject)
  {
    log(Level.FINE, String.valueOf(paramObject), null);
  }
  
  public void debug(Object paramObject, Throwable paramThrowable)
  {
    log(Level.FINE, String.valueOf(paramObject), paramThrowable);
  }
  
  public void error(Object paramObject)
  {
    log(Level.SEVERE, String.valueOf(paramObject), null);
  }
  
  public void error(Object paramObject, Throwable paramThrowable)
  {
    log(Level.SEVERE, String.valueOf(paramObject), paramThrowable);
  }
  
  public void fatal(Object paramObject)
  {
    log(Level.SEVERE, String.valueOf(paramObject), null);
  }
  
  public void fatal(Object paramObject, Throwable paramThrowable)
  {
    log(Level.SEVERE, String.valueOf(paramObject), paramThrowable);
  }
  
  public Logger getLogger()
  {
    if (this.logger == null) {
      this.logger = Logger.getLogger(this.name);
    }
    return this.logger;
  }
  
  public void info(Object paramObject)
  {
    log(Level.INFO, String.valueOf(paramObject), null);
  }
  
  public void info(Object paramObject, Throwable paramThrowable)
  {
    log(Level.INFO, String.valueOf(paramObject), paramThrowable);
  }
  
  public boolean isDebugEnabled()
  {
    return getLogger().isLoggable(Level.FINE);
  }
  
  public boolean isErrorEnabled()
  {
    return getLogger().isLoggable(Level.SEVERE);
  }
  
  public boolean isFatalEnabled()
  {
    return getLogger().isLoggable(Level.SEVERE);
  }
  
  public boolean isInfoEnabled()
  {
    return getLogger().isLoggable(Level.INFO);
  }
  
  public boolean isTraceEnabled()
  {
    return getLogger().isLoggable(Level.FINEST);
  }
  
  public boolean isWarnEnabled()
  {
    return getLogger().isLoggable(Level.WARNING);
  }
  
  protected void log(Level paramLevel, String paramString, Throwable paramThrowable)
  {
    Logger localLogger = getLogger();
    String str1;
    String str2;
    if (localLogger.isLoggable(paramLevel))
    {
      StackTraceElement[] arrayOfStackTraceElement = new Throwable().getStackTrace();
      str1 = this.name;
      str2 = "unknown";
      if ((arrayOfStackTraceElement != null) && (arrayOfStackTraceElement.length > 2)) {
        str2 = arrayOfStackTraceElement[2].getMethodName();
      }
      if (paramThrowable == null) {
        localLogger.logp(paramLevel, str1, str2, paramString);
      }
    }
    else
    {
      return;
    }
    localLogger.logp(paramLevel, str1, str2, paramString, paramThrowable);
  }
  
  public void trace(Object paramObject)
  {
    log(Level.FINEST, String.valueOf(paramObject), null);
  }
  
  public void trace(Object paramObject, Throwable paramThrowable)
  {
    log(Level.FINEST, String.valueOf(paramObject), paramThrowable);
  }
  
  public void warn(Object paramObject)
  {
    log(Level.WARNING, String.valueOf(paramObject), null);
  }
  
  public void warn(Object paramObject, Throwable paramThrowable)
  {
    log(Level.WARNING, String.valueOf(paramObject), paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.Jdk14Logger
 * JD-Core Version:    0.7.0.1
 */