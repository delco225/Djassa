package org.apache.http.impl.conn;

import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.util.Args;

@Immutable
public class DefaultSchemePortResolver
  implements SchemePortResolver
{
  public static final DefaultSchemePortResolver INSTANCE = new DefaultSchemePortResolver();
  
  public int resolve(HttpHost paramHttpHost)
    throws UnsupportedSchemeException
  {
    Args.notNull(paramHttpHost, "HTTP host");
    int i = paramHttpHost.getPort();
    if (i > 0) {
      return i;
    }
    String str = paramHttpHost.getSchemeName();
    if (str.equalsIgnoreCase("http")) {
      return 80;
    }
    if (str.equalsIgnoreCase("https")) {
      return 443;
    }
    throw new UnsupportedSchemeException(str + " protocol is not supported");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultSchemePortResolver
 * JD-Core Version:    0.7.0.1
 */