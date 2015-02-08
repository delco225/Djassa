package org.apache.http.conn.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.security.auth.x500.X500Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.conn.util.InetAddressUtils;

@Immutable
public abstract class AbstractVerifier
  implements X509HostnameVerifier
{
  private static final String[] BAD_COUNTRY_2LDS = { "ac", "co", "com", "ed", "edu", "go", "gouv", "gov", "info", "lg", "ne", "net", "or", "org" };
  private final Log log = LogFactory.getLog(getClass());
  
  static
  {
    Arrays.sort(BAD_COUNTRY_2LDS);
  }
  
  @Deprecated
  public static boolean acceptableCountryWildcard(String paramString)
  {
    String[] arrayOfString = paramString.split("\\.");
    if ((arrayOfString.length != 3) || (arrayOfString[2].length() != 2)) {}
    while (Arrays.binarySearch(BAD_COUNTRY_2LDS, arrayOfString[1]) < 0) {
      return true;
    }
    return false;
  }
  
  public static int countDots(String paramString)
  {
    int i = 0;
    for (int j = 0; j < paramString.length(); j++) {
      if (paramString.charAt(j) == '.') {
        i++;
      }
    }
    return i;
  }
  
  public static String[] getCNs(X509Certificate paramX509Certificate)
  {
    LinkedList localLinkedList = new LinkedList();
    StringTokenizer localStringTokenizer = new StringTokenizer(paramX509Certificate.getSubjectX500Principal().toString(), ",+");
    while (localStringTokenizer.hasMoreTokens())
    {
      String str = localStringTokenizer.nextToken().trim();
      if ((str.length() > 3) && (str.substring(0, 3).equalsIgnoreCase("CN="))) {
        localLinkedList.add(str.substring(3));
      }
    }
    if (!localLinkedList.isEmpty())
    {
      String[] arrayOfString = new String[localLinkedList.size()];
      localLinkedList.toArray(arrayOfString);
      return arrayOfString;
    }
    return null;
  }
  
  public static String[] getDNSSubjectAlts(X509Certificate paramX509Certificate)
  {
    return getSubjectAlts(paramX509Certificate, null);
  }
  
  private static String[] getSubjectAlts(X509Certificate paramX509Certificate, String paramString)
  {
    if (isIPAddress(paramString)) {}
    LinkedList localLinkedList;
    for (int i = 7;; i = 2)
    {
      localLinkedList = new LinkedList();
      try
      {
        Collection localCollection2 = paramX509Certificate.getSubjectAlternativeNames();
        localCollection1 = localCollection2;
      }
      catch (CertificateParsingException localCertificateParsingException)
      {
        for (;;)
        {
          Iterator localIterator;
          String[] arrayOfString;
          Collection localCollection1 = null;
        }
      }
      if (localCollection1 == null) {
        break;
      }
      localIterator = localCollection1.iterator();
      while (localIterator.hasNext())
      {
        List localList = (List)localIterator.next();
        if (((Integer)localList.get(0)).intValue() == i) {
          localLinkedList.add((String)localList.get(1));
        }
      }
    }
    if (!localLinkedList.isEmpty())
    {
      arrayOfString = new String[localLinkedList.size()];
      localLinkedList.toArray(arrayOfString);
      return arrayOfString;
    }
    return null;
  }
  
  private static boolean isIPAddress(String paramString)
  {
    return (paramString != null) && ((InetAddressUtils.isIPv4Address(paramString)) || (InetAddressUtils.isIPv6Address(paramString)));
  }
  
  private String normaliseIPv6Address(String paramString)
  {
    if ((paramString == null) || (!InetAddressUtils.isIPv6Address(paramString))) {
      return paramString;
    }
    try
    {
      String str = InetAddress.getByName(paramString).getHostAddress();
      return str;
    }
    catch (UnknownHostException localUnknownHostException)
    {
      this.log.error("Unexpected error converting " + paramString, localUnknownHostException);
    }
    return paramString;
  }
  
  boolean validCountryWildcard(String paramString)
  {
    String[] arrayOfString = paramString.split("\\.");
    if ((arrayOfString.length != 3) || (arrayOfString[2].length() != 2)) {}
    while (Arrays.binarySearch(BAD_COUNTRY_2LDS, arrayOfString[1]) < 0) {
      return true;
    }
    return false;
  }
  
  public final void verify(String paramString, X509Certificate paramX509Certificate)
    throws SSLException
  {
    verify(paramString, getCNs(paramX509Certificate), getSubjectAlts(paramX509Certificate, paramString));
  }
  
  public final void verify(String paramString, SSLSocket paramSSLSocket)
    throws IOException
  {
    if (paramString == null) {
      throw new NullPointerException("host to verify is null");
    }
    SSLSession localSSLSession = paramSSLSocket.getSession();
    if (localSSLSession == null)
    {
      paramSSLSocket.getInputStream().available();
      localSSLSession = paramSSLSocket.getSession();
      if (localSSLSession == null)
      {
        paramSSLSocket.startHandshake();
        localSSLSession = paramSSLSocket.getSession();
      }
    }
    verify(paramString, (X509Certificate)localSSLSession.getPeerCertificates()[0]);
  }
  
  public final void verify(String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, boolean paramBoolean)
    throws SSLException
  {
    LinkedList localLinkedList = new LinkedList();
    if ((paramArrayOfString1 != null) && (paramArrayOfString1.length > 0) && (paramArrayOfString1[0] != null)) {
      localLinkedList.add(paramArrayOfString1[0]);
    }
    if (paramArrayOfString2 != null)
    {
      int j = paramArrayOfString2.length;
      for (int k = 0; k < j; k++)
      {
        String str8 = paramArrayOfString2[k];
        if (str8 != null) {
          localLinkedList.add(str8);
        }
      }
    }
    if (localLinkedList.isEmpty())
    {
      String str7 = "Certificate for <" + paramString + "> doesn't contain CN or DNS subjectAlt";
      SSLException localSSLException = new SSLException(str7);
      throw localSSLException;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    String str1 = normaliseIPv6Address(paramString.trim().toLowerCase(Locale.US));
    boolean bool = false;
    Iterator localIterator = localLinkedList.iterator();
    label438:
    label444:
    label450:
    label466:
    label472:
    label486:
    for (;;)
    {
      String str2;
      int i;
      if (localIterator.hasNext())
      {
        str2 = ((String)localIterator.next()).toLowerCase(Locale.US);
        localStringBuilder.append(" <");
        localStringBuilder.append(str2);
        localStringBuilder.append('>');
        if (localIterator.hasNext()) {
          localStringBuilder.append(" OR");
        }
        String[] arrayOfString = str2.split("\\.");
        if ((arrayOfString.length < 3) || (!arrayOfString[0].endsWith("*")) || (!validCountryWildcard(str2)) || (isIPAddress(paramString))) {
          break label438;
        }
        i = 1;
        if (i == 0) {
          break label472;
        }
        String str3 = arrayOfString[0];
        if (str3.length() <= 1) {
          break label450;
        }
        String str4 = str3.substring(0, -1 + str3.length());
        String str5 = str2.substring(str3.length());
        String str6 = str1.substring(str4.length());
        if ((!str1.startsWith(str4)) || (!str6.endsWith(str5))) {
          break label444;
        }
        bool = true;
        label363:
        if ((bool) && (paramBoolean))
        {
          if (countDots(str1) != countDots(str2)) {
            break label466;
          }
          bool = true;
        }
      }
      for (;;)
      {
        if (!bool) {
          break label486;
        }
        if (bool) {
          return;
        }
        throw new SSLException("hostname in certificate didn't match: <" + paramString + "> !=" + localStringBuilder);
        i = 0;
        break;
        bool = false;
        break label363;
        bool = str1.endsWith(str2.substring(1));
        break label363;
        bool = false;
        continue;
        bool = str1.equals(normaliseIPv6Address(str2));
      }
    }
  }
  
  public final boolean verify(String paramString, SSLSession paramSSLSession)
  {
    try
    {
      verify(paramString, (X509Certificate)paramSSLSession.getPeerCertificates()[0]);
      return true;
    }
    catch (SSLException localSSLException) {}
    return false;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.conn.ssl.AbstractVerifier
 * JD-Core Version:    0.7.0.1
 */