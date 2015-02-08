package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public class UsernamePasswordCredentials
  implements Credentials, Serializable
{
  private static final long serialVersionUID = 243343858802739403L;
  private final String password;
  private final BasicUserPrincipal principal;
  
  public UsernamePasswordCredentials(String paramString)
  {
    Args.notNull(paramString, "Username:password string");
    int i = paramString.indexOf(':');
    if (i >= 0)
    {
      this.principal = new BasicUserPrincipal(paramString.substring(0, i));
      this.password = paramString.substring(i + 1);
      return;
    }
    this.principal = new BasicUserPrincipal(paramString);
    this.password = null;
  }
  
  public UsernamePasswordCredentials(String paramString1, String paramString2)
  {
    Args.notNull(paramString1, "Username");
    this.principal = new BasicUserPrincipal(paramString1);
    this.password = paramString2;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    UsernamePasswordCredentials localUsernamePasswordCredentials;
    do
    {
      return true;
      if (!(paramObject instanceof UsernamePasswordCredentials)) {
        break;
      }
      localUsernamePasswordCredentials = (UsernamePasswordCredentials)paramObject;
    } while (LangUtils.equals(this.principal, localUsernamePasswordCredentials.principal));
    return false;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public String getUserName()
  {
    return this.principal.getName();
  }
  
  public Principal getUserPrincipal()
  {
    return this.principal;
  }
  
  public int hashCode()
  {
    return this.principal.hashCode();
  }
  
  public String toString()
  {
    return this.principal.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.UsernamePasswordCredentials
 * JD-Core Version:    0.7.0.1
 */