package org.apache.http.conn.ssl;

import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.annotation.NotThreadSafe;

@NotThreadSafe
public class SSLContextBuilder
{
  static final String SSL = "SSL";
  static final String TLS = "TLS";
  private Set<KeyManager> keymanagers = new HashSet();
  private String protocol;
  private SecureRandom secureRandom;
  private Set<TrustManager> trustmanagers = new HashSet();
  
  public SSLContext build()
    throws NoSuchAlgorithmException, KeyManagementException
  {
    String str;
    SSLContext localSSLContext;
    KeyManager[] arrayOfKeyManager;
    if (this.protocol != null)
    {
      str = this.protocol;
      localSSLContext = SSLContext.getInstance(str);
      if (this.keymanagers.isEmpty()) {
        break label111;
      }
      arrayOfKeyManager = (KeyManager[])this.keymanagers.toArray(new KeyManager[this.keymanagers.size()]);
      label54:
      if (this.trustmanagers.isEmpty()) {
        break label116;
      }
    }
    label111:
    label116:
    for (TrustManager[] arrayOfTrustManager = (TrustManager[])this.trustmanagers.toArray(new TrustManager[this.trustmanagers.size()]);; arrayOfTrustManager = null)
    {
      localSSLContext.init(arrayOfKeyManager, arrayOfTrustManager, this.secureRandom);
      return localSSLContext;
      str = "TLS";
      break;
      arrayOfKeyManager = null;
      break label54;
    }
  }
  
  public SSLContextBuilder loadKeyMaterial(KeyStore paramKeyStore, char[] paramArrayOfChar)
    throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
  {
    loadKeyMaterial(paramKeyStore, paramArrayOfChar, null);
    return this;
  }
  
  public SSLContextBuilder loadKeyMaterial(KeyStore paramKeyStore, char[] paramArrayOfChar, PrivateKeyStrategy paramPrivateKeyStrategy)
    throws NoSuchAlgorithmException, KeyStoreException, UnrecoverableKeyException
  {
    KeyManagerFactory localKeyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    localKeyManagerFactory.init(paramKeyStore, paramArrayOfChar);
    KeyManager[] arrayOfKeyManager = localKeyManagerFactory.getKeyManagers();
    if (arrayOfKeyManager != null)
    {
      if (paramPrivateKeyStrategy != null) {
        for (int k = 0; k < arrayOfKeyManager.length; k++)
        {
          KeyManager localKeyManager2 = arrayOfKeyManager[k];
          if ((localKeyManager2 instanceof X509KeyManager)) {
            arrayOfKeyManager[k] = new KeyManagerDelegate((X509KeyManager)localKeyManager2, paramPrivateKeyStrategy);
          }
        }
      }
      int i = arrayOfKeyManager.length;
      for (int j = 0; j < i; j++)
      {
        KeyManager localKeyManager1 = arrayOfKeyManager[j];
        this.keymanagers.add(localKeyManager1);
      }
    }
    return this;
  }
  
  public SSLContextBuilder loadTrustMaterial(KeyStore paramKeyStore)
    throws NoSuchAlgorithmException, KeyStoreException
  {
    return loadTrustMaterial(paramKeyStore, null);
  }
  
  public SSLContextBuilder loadTrustMaterial(KeyStore paramKeyStore, TrustStrategy paramTrustStrategy)
    throws NoSuchAlgorithmException, KeyStoreException
  {
    TrustManagerFactory localTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    localTrustManagerFactory.init(paramKeyStore);
    TrustManager[] arrayOfTrustManager = localTrustManagerFactory.getTrustManagers();
    if (arrayOfTrustManager != null)
    {
      if (paramTrustStrategy != null) {
        for (int k = 0; k < arrayOfTrustManager.length; k++)
        {
          TrustManager localTrustManager2 = arrayOfTrustManager[k];
          if ((localTrustManager2 instanceof X509TrustManager)) {
            arrayOfTrustManager[k] = new TrustManagerDelegate((X509TrustManager)localTrustManager2, paramTrustStrategy);
          }
        }
      }
      int i = arrayOfTrustManager.length;
      for (int j = 0; j < i; j++)
      {
        TrustManager localTrustManager1 = arrayOfTrustManager[j];
        this.trustmanagers.add(localTrustManager1);
      }
    }
    return this;
  }
  
  public SSLContextBuilder setSecureRandom(SecureRandom paramSecureRandom)
  {
    this.secureRandom = paramSecureRandom;
    return this;
  }
  
  public SSLContextBuilder useProtocol(String paramString)
  {
    this.protocol = paramString;
    return this;
  }
  
  public SSLContextBuilder useSSL()
  {
    this.protocol = "SSL";
    return this;
  }
  
  public SSLContextBuilder useTLS()
  {
    this.protocol = "TLS";
    return this;
  }
  
  static class KeyManagerDelegate
    implements X509KeyManager
  {
    private final PrivateKeyStrategy aliasStrategy;
    private final X509KeyManager keyManager;
    
    KeyManagerDelegate(X509KeyManager paramX509KeyManager, PrivateKeyStrategy paramPrivateKeyStrategy)
    {
      this.keyManager = paramX509KeyManager;
      this.aliasStrategy = paramPrivateKeyStrategy;
    }
    
    public String chooseClientAlias(String[] paramArrayOfString, Principal[] paramArrayOfPrincipal, Socket paramSocket)
    {
      HashMap localHashMap = new HashMap();
      int i = paramArrayOfString.length;
      for (int j = 0; j < i; j++)
      {
        String str1 = paramArrayOfString[j];
        String[] arrayOfString = this.keyManager.getClientAliases(str1, paramArrayOfPrincipal);
        if (arrayOfString != null)
        {
          int k = arrayOfString.length;
          for (int m = 0; m < k; m++)
          {
            String str2 = arrayOfString[m];
            localHashMap.put(str2, new PrivateKeyDetails(str1, this.keyManager.getCertificateChain(str2)));
          }
        }
      }
      return this.aliasStrategy.chooseAlias(localHashMap, paramSocket);
    }
    
    public String chooseServerAlias(String paramString, Principal[] paramArrayOfPrincipal, Socket paramSocket)
    {
      HashMap localHashMap = new HashMap();
      String[] arrayOfString = this.keyManager.getServerAliases(paramString, paramArrayOfPrincipal);
      if (arrayOfString != null)
      {
        int i = arrayOfString.length;
        for (int j = 0; j < i; j++)
        {
          String str = arrayOfString[j];
          localHashMap.put(str, new PrivateKeyDetails(paramString, this.keyManager.getCertificateChain(str)));
        }
      }
      return this.aliasStrategy.chooseAlias(localHashMap, paramSocket);
    }
    
    public X509Certificate[] getCertificateChain(String paramString)
    {
      return this.keyManager.getCertificateChain(paramString);
    }
    
    public String[] getClientAliases(String paramString, Principal[] paramArrayOfPrincipal)
    {
      return this.keyManager.getClientAliases(paramString, paramArrayOfPrincipal);
    }
    
    public PrivateKey getPrivateKey(String paramString)
    {
      return this.keyManager.getPrivateKey(paramString);
    }
    
    public String[] getServerAliases(String paramString, Principal[] paramArrayOfPrincipal)
    {
      return this.keyManager.getServerAliases(paramString, paramArrayOfPrincipal);
    }
  }
  
  static class TrustManagerDelegate
    implements X509TrustManager
  {
    private final X509TrustManager trustManager;
    private final TrustStrategy trustStrategy;
    
    TrustManagerDelegate(X509TrustManager paramX509TrustManager, TrustStrategy paramTrustStrategy)
    {
      this.trustManager = paramX509TrustManager;
      this.trustStrategy = paramTrustStrategy;
    }
    
    public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
      throws CertificateException
    {
      this.trustManager.checkClientTrusted(paramArrayOfX509Certificate, paramString);
    }
    
    public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString)
      throws CertificateException
    {
      if (!this.trustStrategy.isTrusted(paramArrayOfX509Certificate, paramString)) {
        this.trustManager.checkServerTrusted(paramArrayOfX509Certificate, paramString);
      }
    }
    
    public X509Certificate[] getAcceptedIssuers()
    {
      return this.trustManager.getAcceptedIssuers();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.SSLContextBuilder
 * JD-Core Version:    0.7.0.1
 */