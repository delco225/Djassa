package org.apache.http.client;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class NonRepeatableRequestException
  extends ProtocolException
{
  private static final long serialVersionUID = 82685265288806048L;
  
  public NonRepeatableRequestException() {}
  
  public NonRepeatableRequestException(String paramString)
  {
    super(paramString);
  }
  
  public NonRepeatableRequestException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.NonRepeatableRequestException
 * JD-Core Version:    0.7.0.1
 */