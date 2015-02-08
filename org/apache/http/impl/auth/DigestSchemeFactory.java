package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeFactory;
import org.apache.http.auth.AuthSchemeProvider;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

@Immutable
public class DigestSchemeFactory
  implements AuthSchemeFactory, AuthSchemeProvider
{
  private final Charset charset;
  
  public DigestSchemeFactory()
  {
    this(null);
  }
  
  public DigestSchemeFactory(Charset paramCharset)
  {
    this.charset = paramCharset;
  }
  
  public AuthScheme create(HttpContext paramHttpContext)
  {
    return new DigestScheme(this.charset);
  }
  
  public AuthScheme newInstance(HttpParams paramHttpParams)
  {
    return new DigestScheme();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.DigestSchemeFactory
 * JD-Core Version:    0.7.0.1
 */