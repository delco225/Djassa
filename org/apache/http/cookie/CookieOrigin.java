package org.apache.http.cookie;

import java.util.Locale;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public final class CookieOrigin
{
  private final String host;
  private final String path;
  private final int port;
  private final boolean secure;
  
  public CookieOrigin(String paramString1, int paramInt, String paramString2, boolean paramBoolean)
  {
    Args.notBlank(paramString1, "Host");
    Args.notNegative(paramInt, "Port");
    Args.notNull(paramString2, "Path");
    this.host = paramString1.toLowerCase(Locale.ENGLISH);
    this.port = paramInt;
    if (paramString2.trim().length() != 0) {}
    for (this.path = paramString2;; this.path = "/")
    {
      this.secure = paramBoolean;
      return;
    }
  }
  
  public String getHost()
  {
    return this.host;
  }
  
  public String getPath()
  {
    return this.path;
  }
  
  public int getPort()
  {
    return this.port;
  }
  
  public boolean isSecure()
  {
    return this.secure;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    if (this.secure) {
      localStringBuilder.append("(secure)");
    }
    localStringBuilder.append(this.host);
    localStringBuilder.append(':');
    localStringBuilder.append(Integer.toString(this.port));
    localStringBuilder.append(this.path);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.CookieOrigin
 * JD-Core Version:    0.7.0.1
 */