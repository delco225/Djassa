package org.apache.http.auth;

import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class AuthOption
{
  private final AuthScheme authScheme;
  private final Credentials creds;
  
  public AuthOption(AuthScheme paramAuthScheme, Credentials paramCredentials)
  {
    Args.notNull(paramAuthScheme, "Auth scheme");
    Args.notNull(paramCredentials, "User credentials");
    this.authScheme = paramAuthScheme;
    this.creds = paramCredentials;
  }
  
  public AuthScheme getAuthScheme()
  {
    return this.authScheme;
  }
  
  public Credentials getCredentials()
  {
    return this.creds;
  }
  
  public String toString()
  {
    return this.authScheme.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.AuthOption
 * JD-Core Version:    0.7.0.1
 */