package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class NTLMSchemeFactory
  implements AuthSchemeFactory, AuthSchemeProvider
{
  public AuthScheme create(HttpContext paramHttpContext)
  {
    return new NTLMScheme();
  }
  
  public AuthScheme newInstance(HttpParams paramHttpParams)
  {
    return new NTLMScheme();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.NTLMSchemeFactory
 * JD-Core Version:    0.7.0.1
 */