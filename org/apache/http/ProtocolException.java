package org.apache.http;

public class ProtocolException
  extends HttpException
{
  private static final long serialVersionUID = -2143571074341228994L;
  
  public ProtocolException() {}
  
  public ProtocolException(String paramString)
  {
    super(paramString);
  }
  
  public ProtocolException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.ProtocolException
 * JD-Core Version:    0.7.0.1
 */