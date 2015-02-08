package org.apache.http.impl.auth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.RequestLine;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueFormatter;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
public class DigestScheme
  extends RFC2617Scheme
{
  private static final char[] HEXADECIMAL = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102 };
  private static final int QOP_AUTH = 2;
  private static final int QOP_AUTH_INT = 1;
  private static final int QOP_MISSING = 0;
  private static final int QOP_UNKNOWN = -1;
  private String a1;
  private String a2;
  private String cnonce;
  private boolean complete;
  private String lastNonce;
  private long nounceCount;
  
  public DigestScheme()
  {
    this(Consts.ASCII);
  }
  
  public DigestScheme(Charset paramCharset)
  {
    super(paramCharset);
    this.complete = false;
  }
  
  @Deprecated
  public DigestScheme(ChallengeState paramChallengeState)
  {
    super(paramChallengeState);
  }
  
  public static String createCnonce()
  {
    SecureRandom localSecureRandom = new SecureRandom();
    byte[] arrayOfByte = new byte[8];
    localSecureRandom.nextBytes(arrayOfByte);
    return encode(arrayOfByte);
  }
  
  private Header createDigestHeader(Credentials paramCredentials, HttpRequest paramHttpRequest)
    throws AuthenticationException
  {
    String str1 = getParameter("uri");
    String str2 = getParameter("realm");
    String str3 = getParameter("nonce");
    String str4 = getParameter("opaque");
    String str5 = getParameter("methodname");
    String str6 = getParameter("algorithm");
    if (str6 == null) {
      str6 = "MD5";
    }
    HashSet localHashSet = new HashSet(8);
    int i = -1;
    String str7 = getParameter("qop");
    if (str7 != null)
    {
      StringTokenizer localStringTokenizer = new StringTokenizer(str7, ",");
      while (localStringTokenizer.hasMoreTokens()) {
        localHashSet.add(localStringTokenizer.nextToken().trim().toLowerCase(Locale.US));
      }
      if (((paramHttpRequest instanceof HttpEntityEnclosingRequest)) && (localHashSet.contains("auth-int"))) {
        i = 1;
      }
    }
    while (i == -1)
    {
      throw new AuthenticationException("None of the qop methods is supported: " + str7);
      if (localHashSet.contains("auth"))
      {
        i = 2;
        continue;
        i = 0;
      }
    }
    String str8 = getParameter("charset");
    if (str8 == null) {
      str8 = "ISO-8859-1";
    }
    String str9 = str6;
    if (str9.equalsIgnoreCase("MD5-sess")) {
      str9 = "MD5";
    }
    CharArrayBuffer localCharArrayBuffer;
    label1090:
    label1366:
    for (;;)
    {
      MessageDigest localMessageDigest;
      String str10;
      String str11;
      StringBuilder localStringBuilder1;
      String str12;
      String str13;
      String str14;
      String str16;
      String str19;
      int m;
      boolean bool1;
      try
      {
        localMessageDigest = createMessageDigest(str9);
        str10 = paramCredentials.getUserPrincipal().getName();
        str11 = paramCredentials.getPassword();
        if (str3.equals(this.lastNonce))
        {
          this.nounceCount = (1L + this.nounceCount);
          localStringBuilder1 = new StringBuilder(256);
          Formatter localFormatter = new Formatter(localStringBuilder1, Locale.US);
          Object[] arrayOfObject = new Object[1];
          arrayOfObject[0] = Long.valueOf(this.nounceCount);
          localFormatter.format("%08x", arrayOfObject);
          localFormatter.close();
          str12 = localStringBuilder1.toString();
          if (this.cnonce == null) {
            this.cnonce = createCnonce();
          }
          this.a1 = null;
          this.a2 = null;
          if (!str6.equalsIgnoreCase("MD5-sess")) {
            break label1090;
          }
          localStringBuilder1.setLength(0);
          localStringBuilder1.append(str10).append(':').append(str2).append(':').append(str11);
          String str20 = encode(localMessageDigest.digest(EncodingUtils.getBytes(localStringBuilder1.toString(), str8)));
          localStringBuilder1.setLength(0);
          localStringBuilder1.append(str20).append(':').append(str3).append(':').append(this.cnonce);
          this.a1 = localStringBuilder1.toString();
          str13 = encode(localMessageDigest.digest(EncodingUtils.getBytes(this.a1, str8)));
          if (i != 2) {
            break label1136;
          }
          this.a2 = (str5 + ':' + str1);
          str14 = encode(localMessageDigest.digest(EncodingUtils.getBytes(this.a2, str8)));
          if (i != 0) {
            break label1366;
          }
          localStringBuilder1.setLength(0);
          localStringBuilder1.append(str13).append(':').append(str3).append(':').append(str14);
          str16 = localStringBuilder1.toString();
          String str17 = encode(localMessageDigest.digest(EncodingUtils.getAsciiBytes(str16)));
          localCharArrayBuffer = new CharArrayBuffer(128);
          if (!isProxy()) {
            break label1463;
          }
          localCharArrayBuffer.append("Proxy-Authorization");
          localCharArrayBuffer.append(": Digest ");
          ArrayList localArrayList = new ArrayList(20);
          BasicNameValuePair localBasicNameValuePair1 = new BasicNameValuePair("username", str10);
          localArrayList.add(localBasicNameValuePair1);
          BasicNameValuePair localBasicNameValuePair2 = new BasicNameValuePair("realm", str2);
          localArrayList.add(localBasicNameValuePair2);
          BasicNameValuePair localBasicNameValuePair3 = new BasicNameValuePair("nonce", str3);
          localArrayList.add(localBasicNameValuePair3);
          BasicNameValuePair localBasicNameValuePair4 = new BasicNameValuePair("uri", str1);
          localArrayList.add(localBasicNameValuePair4);
          BasicNameValuePair localBasicNameValuePair5 = new BasicNameValuePair("response", str17);
          localArrayList.add(localBasicNameValuePair5);
          if (i != 0)
          {
            if (i != 1) {
              break label1474;
            }
            str19 = "auth-int";
            BasicNameValuePair localBasicNameValuePair9 = new BasicNameValuePair("qop", str19);
            localArrayList.add(localBasicNameValuePair9);
            BasicNameValuePair localBasicNameValuePair10 = new BasicNameValuePair("nc", str12);
            localArrayList.add(localBasicNameValuePair10);
            localArrayList.add(new BasicNameValuePair("cnonce", this.cnonce));
          }
          BasicNameValuePair localBasicNameValuePair6 = new BasicNameValuePair("algorithm", str6);
          localArrayList.add(localBasicNameValuePair6);
          if (str4 != null)
          {
            BasicNameValuePair localBasicNameValuePair7 = new BasicNameValuePair("opaque", str4);
            localArrayList.add(localBasicNameValuePair7);
          }
          int j = 0;
          int k = localArrayList.size();
          if (j >= k) {
            break;
          }
          BasicNameValuePair localBasicNameValuePair8 = (BasicNameValuePair)localArrayList.get(j);
          if (j > 0) {
            localCharArrayBuffer.append(", ");
          }
          String str18 = localBasicNameValuePair8.getName();
          if ((!"nc".equals(str18)) && (!"qop".equals(str18)) && (!"algorithm".equals(str18))) {
            break label1481;
          }
          m = 1;
          BasicHeaderValueFormatter localBasicHeaderValueFormatter = BasicHeaderValueFormatter.INSTANCE;
          if (m != 0) {
            break label1487;
          }
          bool1 = true;
          localBasicHeaderValueFormatter.formatNameValuePair(localCharArrayBuffer, localBasicNameValuePair8, bool1);
          j++;
          continue;
        }
        this.nounceCount = 1L;
      }
      catch (UnsupportedDigestAlgorithmException localUnsupportedDigestAlgorithmException)
      {
        throw new AuthenticationException("Unsuppported digest algorithm: " + str9);
      }
      this.cnonce = null;
      this.lastNonce = str3;
      continue;
      localStringBuilder1.setLength(0);
      localStringBuilder1.append(str10).append(':').append(str2).append(':').append(str11);
      this.a1 = localStringBuilder1.toString();
      continue;
      label1136:
      if (i == 1)
      {
        boolean bool2 = paramHttpRequest instanceof HttpEntityEnclosingRequest;
        HttpEntity localHttpEntity = null;
        if (bool2) {
          localHttpEntity = ((HttpEntityEnclosingRequest)paramHttpRequest).getEntity();
        }
        if ((localHttpEntity != null) && (!localHttpEntity.isRepeatable()))
        {
          if (localHashSet.contains("auth"))
          {
            i = 2;
            this.a2 = (str5 + ':' + str1);
          }
          else
          {
            throw new AuthenticationException("Qop auth-int cannot be used with a non-repeatable entity");
          }
        }
        else
        {
          HttpEntityDigester localHttpEntityDigester = new HttpEntityDigester(localMessageDigest);
          if (localHttpEntity != null) {}
          try
          {
            localHttpEntity.writeTo(localHttpEntityDigester);
            localHttpEntityDigester.close();
            this.a2 = (str5 + ':' + str1 + ':' + encode(localHttpEntityDigester.getDigest()));
          }
          catch (IOException localIOException)
          {
            AuthenticationException localAuthenticationException = new AuthenticationException("I/O error reading entity content", localIOException);
            throw localAuthenticationException;
          }
        }
      }
      else
      {
        this.a2 = (str5 + ':' + str1);
        continue;
        localStringBuilder1.setLength(0);
        StringBuilder localStringBuilder2 = localStringBuilder1.append(str13).append(':').append(str3).append(':').append(str12).append(':').append(this.cnonce).append(':');
        if (i == 1) {}
        for (String str15 = "auth-int";; str15 = "auth")
        {
          localStringBuilder2.append(str15).append(':').append(str14);
          str16 = localStringBuilder1.toString();
          break;
        }
        localCharArrayBuffer.append("Authorization");
        continue;
        str19 = "auth";
        continue;
        m = 0;
        continue;
        bool1 = false;
      }
    }
    label1463:
    label1474:
    label1481:
    label1487:
    BufferedHeader localBufferedHeader = new BufferedHeader(localCharArrayBuffer);
    return localBufferedHeader;
  }
  
  private static MessageDigest createMessageDigest(String paramString)
    throws UnsupportedDigestAlgorithmException
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance(paramString);
      return localMessageDigest;
    }
    catch (Exception localException)
    {
      throw new UnsupportedDigestAlgorithmException("Unsupported algorithm in HTTP Digest authentication: " + paramString);
    }
  }
  
  static String encode(byte[] paramArrayOfByte)
  {
    int i = paramArrayOfByte.length;
    char[] arrayOfChar = new char[i * 2];
    for (int j = 0; j < i; j++)
    {
      int k = 0xF & paramArrayOfByte[j];
      int m = (0xF0 & paramArrayOfByte[j]) >> 4;
      arrayOfChar[(j * 2)] = HEXADECIMAL[m];
      arrayOfChar[(1 + j * 2)] = HEXADECIMAL[k];
    }
    return new String(arrayOfChar);
  }
  
  @Deprecated
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest)
    throws AuthenticationException
  {
    return authenticate(paramCredentials, paramHttpRequest, new BasicHttpContext());
  }
  
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    Args.notNull(paramCredentials, "Credentials");
    Args.notNull(paramHttpRequest, "HTTP request");
    if (getParameter("realm") == null) {
      throw new AuthenticationException("missing realm in challenge");
    }
    if (getParameter("nonce") == null) {
      throw new AuthenticationException("missing nonce in challenge");
    }
    getParameters().put("methodname", paramHttpRequest.getRequestLine().getMethod());
    getParameters().put("uri", paramHttpRequest.getRequestLine().getUri());
    if (getParameter("charset") == null) {
      getParameters().put("charset", getCredentialsCharset(paramHttpRequest));
    }
    return createDigestHeader(paramCredentials, paramHttpRequest);
  }
  
  String getA1()
  {
    return this.a1;
  }
  
  String getA2()
  {
    return this.a2;
  }
  
  String getCnonce()
  {
    return this.cnonce;
  }
  
  public String getSchemeName()
  {
    return "digest";
  }
  
  public boolean isComplete()
  {
    if ("true".equalsIgnoreCase(getParameter("stale"))) {
      return false;
    }
    return this.complete;
  }
  
  public boolean isConnectionBased()
  {
    return false;
  }
  
  public void overrideParamter(String paramString1, String paramString2)
  {
    getParameters().put(paramString1, paramString2);
  }
  
  public void processChallenge(Header paramHeader)
    throws MalformedChallengeException
  {
    super.processChallenge(paramHeader);
    this.complete = true;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append("DIGEST [complete=").append(this.complete).append(", nonce=").append(this.lastNonce).append(", nc=").append(this.nounceCount).append("]");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.DigestScheme
 * JD-Core Version:    0.7.0.1
 */