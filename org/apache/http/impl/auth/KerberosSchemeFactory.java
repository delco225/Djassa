package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class KerberosSchemeFactory
  implements AuthSchemeFactory, AuthSchemeProvider
{
  private final boolean stripPort;
  
  public KerberosSchemeFactory()
  {
    this(false);
  }
  
  public KerberosSchemeFactory(boolean paramBoolean)
  {
    this.stripPort = paramBoolean;
  }
  
  public AuthScheme create(HttpContext paramHttpContext)
  {
    return new KerberosScheme(this.stripPort);
  }
  
  public boolean isStripPort()
  {
    return this.stripPort;
  }
  
  public AuthScheme newInstance(HttpParams paramHttpParams)
  {
    return new KerberosScheme(this.stripPort);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.KerberosSchemeFactory
 * JD-Core Version:    0.7.0.1
 */