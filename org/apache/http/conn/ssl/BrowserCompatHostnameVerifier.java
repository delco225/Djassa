package org.apache.http.conn.ssl;

import javax.net.ssl.SSLException;
import org.apache.http.annotation.Immutable;

@Immutable
public class BrowserCompatHostnameVerifier
  extends AbstractVerifier
{
  public final String toString()
  {
    return "BROWSER_COMPATIBLE";
  }
  
  boolean validCountryWildcard(String paramString)
  {
    return true;
  }
  
  public final void verify(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2)
    throws SSLException
  {
    verify(paramString, paramArrayOfString1, paramArrayOfString2, false);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.BrowserCompatHostnameVerifier
 * JD-Core Version:    0.7.0.1
 */