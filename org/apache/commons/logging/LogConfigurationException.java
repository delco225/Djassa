package org.apache.commons.logging;

public class LogConfigurationException
  extends RuntimeException
{
  private static final long serialVersionUID = 8486587136871052495L;
  protected Throwable cause = null;
  
  public LogConfigurationException() {}
  
  public LogConfigurationException(String paramString)
  {
    super(paramString);
  }
  
  public LogConfigurationException(String paramString, Throwable paramThrowable)
  {
    super(paramString + " (Caused by " + paramThrowable + ")");
    this.cause = paramThrowable;
  }
  
  public LogConfigurationException(Throwable paramThrowable) {}
  
  public Throwable getCause()
  {
    return this.cause;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogConfigurationException
 * JD-Core Version:    0.7.0.1
 */