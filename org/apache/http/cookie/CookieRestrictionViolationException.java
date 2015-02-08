package org.apache.http.cookie;

import org.apache.http.annotation.Immutable;

@Immutable
public class CookieRestrictionViolationException
  extends MalformedCookieException
{
  private static final long serialVersionUID = 7371235577078589013L;
  
  public CookieRestrictionViolationException() {}
  
  public CookieRestrictionViolationException(String paramString)
  {
    super(paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.CookieRestrictionViolationException
 * JD-Core Version:    0.7.0.1
 */