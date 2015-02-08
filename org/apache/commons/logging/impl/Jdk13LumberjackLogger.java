package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk13LumberjackLogger
  implements Log, Serializable
{
  protected static final Level dummyLevel = Level.FINE;
  private static final long serialVersionUID = -8649807923527610591L;
  private boolean classAndMethodFound = false;
  protected transient Logger logger = null;
  protected String name = null;
  private String sourceClassName = "unknown";
  private String sourceMethodName = "unknown";
  
  public Jdk13LumberjackLogger(String paramString)
  {
    this.name = paramString;
    this.logger = getLogger();
  }
  
  private void getClassAndMethod()
  {
    try
    {
      Throwable localThrowable = new Throwable();
      localThrowable.fillInStackTrace();
      StringWriter localStringWriter = new StringWriter();
      localThrowable.printStackTrace(new PrintWriter(localStringWriter));
      StringTokenizer localStringTokenizer = new StringTokenizer(localStringWriter.getBuffer().toString(), "\n");
      localStringTokenizer.nextToken();
      for (String str1 = localStringTokenizer.nextToken(); str1.indexOf(getClass().getName()) == -1; str1 = localStringTokenizer.nextToken()) {}
      while (str1.indexOf(getClass().getName()) >= 0) {
        str1 = localStringTokenizer.nextToken();
      }
      String str2 = str1.substring(3 + str1.indexOf("at "), str1.indexOf('('));
      int i = str2.lastIndexOf('.');
      this.sourceClassName = str2.substring(0, i);
      this.sourceMethodName = str2.substring(i + 1);
    }
    catch (Exception localException)
    {
      label175:
      break label175;
    }
    this.classAndMethodFound = true;
  }
  
  private void log(Level paramLevel, String paramString, Throwable paramThrowable)
  {
    if (getLogger().isLoggable(paramLevel))
    {
      LogRecord localLogRecord = new LogRecord(paramLevel, paramString);
      if (!this.classAndMethodFound) {
        getClassAndMethod();
      }
      localLogRecord.setSourceClassName(this.sourceClassName);
      localLogRecord.setSourceMethodName(this.sourceMethodName);
      if (paramThrowable != null) {
        localLogRecord.setThrown(paramThrowable);
      }
      getLogger().log(localLogRecord);
    }
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
 * Qualified Name:     org.apache.commons.logging.impl.Jdk13LumberjackLogger
 * JD-Core Version:    0.7.0.1
 */