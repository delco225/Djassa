package org.apache.http.conn.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public abstract interface TrustStrategy
{
  public abstract boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
    throws CertificateException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.TrustStrategy
 * JD-Core Version:    0.7.0.1
 */