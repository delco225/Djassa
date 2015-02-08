package org.apache.http.conn.ssl;

import org.apache.http.annotation.Immutable;

@Immutable
public class AllowAllHostnameVerifier
  extends AbstractVerifier
{
  public final String toString()
  {
    return "ALLOW_ALL";
  }
  
  public final void verify(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2) {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.AllowAllHostnameVerifier
 * JD-Core Version:    0.7.0.1
 */