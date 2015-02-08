package org.apache.http;

public class HttpException
  extends Exception
{
  private static final long serialVersionUID = -5437299376222011036L;
  
  public HttpException() {}
  
  public HttpException(String paramString)
  {
    super(paramString);
  }
  
  public HttpException(String paramString, Throwable paramThrowable)
  {
    super(paramString);
    initCause(paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.HttpException
 * JD-Core Version:    0.7.0.1
 */