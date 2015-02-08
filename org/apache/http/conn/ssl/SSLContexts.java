package org.apache.http.conn.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.apache.http.annotation.Immutable;

@Immutable
public class SSLContexts
{
  public static SSLContext createDefault()
    throws SSLInitializationException
  {
    try
    {
      SSLContext localSSLContext = SSLContext.getInstance("TLS");
      localSSLContext.init(null, null, null);
      return localSSLContext;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      throw new SSLInitializationException(localNoSuchAlgorithmException.getMessage(), localNoSuchAlgorithmException);
    }
    catch (KeyManagementException localKeyManagementException)
    {
      throw new SSLInitializationException(localKeyManagementException.getMessage(), localKeyManagementException);
    }
  }
  
  public static SSLContext createSystemDefault()
    throws SSLInitializationException
  {
    try
    {
      SSLContext localSSLContext = SSLContext.getInstance("Default");
      return localSSLContext;
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {}
    return createDefault();
  }
  
  public static SSLContextBuilder custom()
  {
    return new SSLContextBuilder();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.SSLContexts
 * JD-Core Version:    0.7.0.1
 */