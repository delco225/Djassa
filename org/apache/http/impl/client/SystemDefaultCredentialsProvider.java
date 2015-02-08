package org.apache.http.impl.client;

import java.net.Authenticator;
import java.net.Authenticator.RequestorType;
import java.net.PasswordAuthentication;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.NTCredentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.util.Args;

@ThreadSafe
public class SystemDefaultCredentialsProvider
  implements CredentialsProvider
{
  private static final Map<String, String> SCHEME_MAP = new ConcurrentHashMap();
  private final BasicCredentialsProvider internal = new BasicCredentialsProvider();
  
  static
  {
    SCHEME_MAP.put("Basic".toUpperCase(Locale.ENGLISH), "Basic");
    SCHEME_MAP.put("Digest".toUpperCase(Locale.ENGLISH), "Digest");
    SCHEME_MAP.put("NTLM".toUpperCase(Locale.ENGLISH), "NTLM");
    SCHEME_MAP.put("negotiate".toUpperCase(Locale.ENGLISH), "SPNEGO");
    SCHEME_MAP.put("Kerberos".toUpperCase(Locale.ENGLISH), "Kerberos");
  }
  
  private static PasswordAuthentication getSystemCreds(AuthScope paramAuthScope, Authenticator.RequestorType paramRequestorType)
  {
    String str1 = paramAuthScope.getHost();
    int i = paramAuthScope.getPort();
    if (i == 443) {}
    for (String str2 = "https";; str2 = "http") {
      return Authenticator.requestPasswordAuthentication(str1, null, i, str2, null, translateScheme(paramAuthScope.getScheme()), null, paramRequestorType);
    }
  }
  
  private static String translateScheme(String paramString)
  {
    String str;
    if (paramString == null) {
      str = null;
    }
    do
    {
      return str;
      str = (String)SCHEME_MAP.get(paramString);
    } while (str != null);
    return paramString;
  }
  
  public void clear()
  {
    this.internal.clear();
  }
  
  public Credentials getCredentials(AuthScope paramAuthScope)
  {
    Args.notNull(paramAuthScope, "Auth scope");
    Credentials localCredentials = this.internal.getCredentials(paramAuthScope);
    if (localCredentials != null) {
      return localCredentials;
    }
    if (paramAuthScope.getHost() != null)
    {
      PasswordAuthentication localPasswordAuthentication = getSystemCreds(paramAuthScope, Authenticator.RequestorType.SERVER);
      if (localPasswordAuthentication == null) {
        localPasswordAuthentication = getSystemCreds(paramAuthScope, Authenticator.RequestorType.PROXY);
      }
      if (localPasswordAuthentication != null)
      {
        String str = System.getProperty("http.auth.ntlm.domain");
        if (str != null) {
          return new NTCredentials(localPasswordAuthentication.getUserName(), new String(localPasswordAuthentication.getPassword()), null, str);
        }
        if ("NTLM".equalsIgnoreCase(paramAuthScope.getScheme())) {
          return new NTCredentials(localPasswordAuthentication.getUserName(), new String(localPasswordAuthentication.getPassword()), null, null);
        }
        return new UsernamePasswordCredentials(localPasswordAuthentication.getUserName(), new String(localPasswordAuthentication.getPassword()));
      }
    }
    return null;
  }
  
  public void setCredentials(AuthScope paramAuthScope, Credentials paramCredentials)
  {
    this.internal.setCredentials(paramAuthScope, paramCredentials);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.SystemDefaultCredentialsProvider
 * JD-Core Version:    0.7.0.1
 */