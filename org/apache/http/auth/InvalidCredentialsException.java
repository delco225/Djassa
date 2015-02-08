package org.apache.http.auth;

import org.apache.http.annotation.Immutable;

@Immutable
public class InvalidCredentialsException
  extends AuthenticationException
{
  private static final long serialVersionUID = -4834003835215460648L;
  
  public InvalidCredentialsException() {}
  
  public InvalidCredentialsException(String paramString)
  {
    super(paramString);
  }
  
  public InvalidCredentialsException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.InvalidCredentialsException
 * JD-Core Version:    0.7.0.1
 */