package org.apache.http.cookie;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;

@Immutable
public class MalformedCookieException
  extends ProtocolException
{
  private static final long serialVersionUID = -6695462944287282185L;
  
  public MalformedCookieException() {}
  
  public MalformedCookieException(String paramString)
  {
    super(paramString);
  }
  
  public MalformedCookieException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.MalformedCookieException
 * JD-Core Version:    0.7.0.1
 */