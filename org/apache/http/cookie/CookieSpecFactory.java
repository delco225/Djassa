package org.apache.http.cookie;

import org.apache.http.params.HttpParams;

@Deprecated
public abstract interface CookieSpecFactory
{
  public abstract CookieSpec newInstance(HttpParams paramHttpParams);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.CookieSpecFactory
 * JD-Core Version:    0.7.0.1
 */