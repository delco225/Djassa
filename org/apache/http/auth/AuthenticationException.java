package org.apache.http.auth;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class AuthenticationException
  extends ProtocolException
{
  private static final long serialVersionUID = -6794031905674764776L;
  
  public AuthenticationException() {}
  
  public AuthenticationException(String paramString)
  {
    super(paramString);
  }
  
  public AuthenticationException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.AuthenticationException
 * JD-Core Version:    0.7.0.1
 */