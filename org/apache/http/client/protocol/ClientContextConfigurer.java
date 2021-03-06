package org.apache.http.client.protocol;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.cookie.CookieSpecRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
@NotThreadSafe
public class ClientContextConfigurer
  implements ClientContext
{
  private final HttpContext context;
  
  public ClientContextConfigurer(HttpContext paramHttpContext)
  {
    Args.notNull(paramHttpContext, "HTTP context");
    this.context = paramHttpContext;
  }
  
  public void setAuthSchemeRegistry(AuthSchemeRegistry paramAuthSchemeRegistry)
  {
    this.context.setAttribute("http.authscheme-registry", paramAuthSchemeRegistry);
  }
  
  public void setCookieSpecRegistry(CookieSpecRegistry paramCookieSpecRegistry)
  {
    this.context.setAttribute("http.cookiespec-registry", paramCookieSpecRegistry);
  }
  
  public void setCookieStore(CookieStore paramCookieStore)
  {
    this.context.setAttribute("http.cookie-store", paramCookieStore);
  }
  
  public void setCredentialsProvider(CredentialsProvider paramCredentialsProvider)
  {
    this.context.setAttribute("http.auth.credentials-provider", paramCredentialsProvider);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.protocol.ClientContextConfigurer
 * JD-Core Version:    0.7.0.1
 */