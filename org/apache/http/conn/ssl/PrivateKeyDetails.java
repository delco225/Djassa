package org.apache.http.conn.ssl;

import java.security.cert.X509Certificate;
import java.util.Arrays;
import org.apache.http.util.Args;

public final class PrivateKeyDetails
{
  private final X509Certificate[] certChain;
  private final String type;
  
  public PrivateKeyDetails(String paramString, X509Certificate[] paramArrayOfX509Certificate)
  {
    this.type = ((String)Args.notNull(paramString, "Private key type"));
    this.certChain = paramArrayOfX509Certificate;
  }
  
  public X509Certificate[] getCertChain()
  {
    return this.certChain;
  }
  
  public String getType()
  {
    return this.type;
  }
  
  public String toString()
  {
    return this.type + ':' + Arrays.toString(this.certChain);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.PrivateKeyDetails
 * JD-Core Version:    0.7.0.1
 */