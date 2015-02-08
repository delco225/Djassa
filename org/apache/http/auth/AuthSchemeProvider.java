package org.apache.http.auth;

import org.apache.http.protocol.HttpContext;

public abstract interface AuthSchemeProvider
{
  public abstract AuthScheme create(HttpContext paramHttpContext);
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.AuthSchemeProvider
 * JD-Core Version:    0.7.0.1
 */