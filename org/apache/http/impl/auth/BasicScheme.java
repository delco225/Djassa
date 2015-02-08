package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import java.security.Principal;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BufferedHeader;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
public class BasicScheme
  extends RFC2617Scheme
{
  private final Base64 base64codec = new Base64(0);
  private boolean complete;
  
  public BasicScheme()
  {
    this(Consts.ASCII);
  }
  
  public BasicScheme(Charset paramCharset)
  {
    super(paramCharset);
    this.complete = false;
  }
  
  @Deprecated
  public BasicScheme(ChallengeState paramChallengeState)
  {
    super(paramChallengeState);
  }
  
  @Deprecated
  public static Header authenticate(Credentials paramCredentials, String paramString, boolean paramBoolean)
  {
    Args.notNull(paramCredentials, "Credentials");
    Args.notNull(paramString, "charset");
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCredentials.getUserPrincipal().getName());
    localStringBuilder.append(":");
    String str;
    byte[] arrayOfByte;
    CharArrayBuffer localCharArrayBuffer;
    if (paramCredentials.getPassword() == null)
    {
      str = "null";
      localStringBuilder.append(str);
      arrayOfByte = Base64.encodeBase64(EncodingUtils.getBytes(localStringBuilder.toString(), paramString), false);
      localCharArrayBuffer = new CharArrayBuffer(32);
      if (!paramBoolean) {
        break label145;
      }
      localCharArrayBuffer.append("Proxy-Authorization");
    }
    for (;;)
    {
      localCharArrayBuffer.append(": Basic ");
      localCharArrayBuffer.append(arrayOfByte, 0, arrayOfByte.length);
      return new BufferedHeader(localCharArrayBuffer);
      str = paramCredentials.getPassword();
      break;
      label145:
      localCharArrayBuffer.append("Authorization");
    }
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
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramCredentials.getUserPrincipal().getName());
    localStringBuilder.append(":");
    String str;
    byte[] arrayOfByte;
    CharArrayBuffer localCharArrayBuffer;
    if (paramCredentials.getPassword() == null)
    {
      str = "null";
      localStringBuilder.append(str);
      arrayOfByte = this.base64codec.encode(EncodingUtils.getBytes(localStringBuilder.toString(), getCredentialsCharset(paramHttpRequest)));
      localCharArrayBuffer = new CharArrayBuffer(32);
      if (!isProxy()) {
        break label155;
      }
      localCharArrayBuffer.append("Proxy-Authorization");
    }
    for (;;)
    {
      localCharArrayBuffer.append(": Basic ");
      localCharArrayBuffer.append(arrayOfByte, 0, arrayOfByte.length);
      return new BufferedHeader(localCharArrayBuffer);
      str = paramCredentials.getPassword();
      break;
      label155:
      localCharArrayBuffer.append("Authorization");
    }
  }
  
  public String getSchemeName()
  {
    return "basic";
  }
  
  public boolean isComplete()
  {
    return this.complete;
  }
  
  public boolean isConnectionBased()
  {
    return false;
  }
  
  public void processChallenge(Header paramHeader)
    throws MalformedChallengeException
  {
    super.processChallenge(paramHeader);
    this.complete = true;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.BasicScheme
 * JD-Core Version:    0.7.0.1
 */