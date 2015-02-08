package org.apache.http.auth;

import java.io.Serializable;
import java.security.Principal;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Immutable
public final class BasicUserPrincipal
  implements Principal, Serializable
{
  private static final long serialVersionUID = -2266305184969850467L;
  private final String username;
  
  public BasicUserPrincipal(String paramString)
  {
    Args.notNull(paramString, "User name");
    this.username = paramString;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {}
    BasicUserPrincipal localBasicUserPrincipal;
    do
    {
      return true;
      if (!(paramObject instanceof BasicUserPrincipal)) {
        break;
      }
      localBasicUserPrincipal = (BasicUserPrincipal)paramObject;
    } while (LangUtils.equals(this.username, localBasicUserPrincipal.username));
    return false;
  }
  
  public String getName()
  {
    return this.username;
  }
  
  public int hashCode()
  {
    return LangUtils.hashCode(17, this.username);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("[principal: ");
    localStringBuilder.append(this.username);
    localStringBuilder.append("]");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.BasicUserPrincipal
 * JD-Core Version:    0.7.0.1
 */