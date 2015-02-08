package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public class NTUserPrincipal
  implements Principal, Serializable
{
  private static final long serialVersionUID = -6870169797924406894L;
  private final String domain;
  private final String ntname;
  private final String username;
  
  public NTUserPrincipal(String paramString1, String paramString2)
  {
    Args.notNull(paramString2, "User name");
    this.username = paramString2;
    if (paramString1 != null) {}
    for (this.domain = paramString1.toUpperCase(Locale.ENGLISH); (this.domain != null) && (this.domain.length() > 0); this.domain = null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append(this.domain);
      localStringBuilder.append('\\');
      localStringBuilder.append(this.username);
      this.ntname = localStringBuilder.toString();
      return;
    }
    this.ntname = this.username;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    NTUserPrincipal localNTUserPrincipal;
    do
    {
      return true;
      if (!(paramObject instanceof NTUserPrincipal)) {
        break;
      }
      localNTUserPrincipal = (NTUserPrincipal)paramObject;
    } while ((LangUtils.equals(this.username, localNTUserPrincipal.username)) && (LangUtils.equals(this.domain, localNTUserPrincipal.domain)));
    return false;
  }
  
  public String getDomain()
  {
    return this.domain;
  }
  
  public String getName()
  {
    return this.ntname;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public int hashCode()
  {
    return LangUtils.hashCode(LangUtils.hashCode(17, this.username), this.domain);
  }
  
  public String toString()
  {
    return this.ntname;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.NTUserPrincipal
 * JD-Core Version:    0.7.0.1
 */