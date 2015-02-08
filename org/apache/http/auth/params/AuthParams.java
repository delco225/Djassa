package org.apache.http.auth.params;

import java.nio.charset.Charset;
import org.apache.http.annotation.Immutable;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public final class AuthParams
{
  public static String getCredentialCharset(HttpParams paramHttpParams)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    String str = (String)paramHttpParams.getParameter("http.auth.credential-charset");
    if (str == null) {
      str = HTTP.DEF_PROTOCOL_CHARSET.name();
    }
    return str;
  }
  
  public static void setCredentialCharset(HttpParams paramHttpParams, String paramString)
  {
    Args.notNull(paramHttpParams, "HTTP parameters");
    paramHttpParams.setParameter("http.auth.credential-charset", paramString);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.auth.params.AuthParams
 * JD-Core Version:    0.7.0.1
 */