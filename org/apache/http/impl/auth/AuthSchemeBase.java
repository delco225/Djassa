package org.apache.http.impl.auth;

import java.util.Locale;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.ContextAwareAuthScheme;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AuthSchemeBase
  implements ContextAwareAuthScheme
{
  private ChallengeState challengeState;
  
  public AuthSchemeBase() {}
  
  @Deprecated
  public AuthSchemeBase(ChallengeState paramChallengeState)
  {
    this.challengeState = paramChallengeState;
  }
  
  public Header authenticate(Credentials paramCredentials, HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    return authenticate(paramCredentials, paramHttpRequest);
  }
  
  public ChallengeState getChallengeState()
  {
    return this.challengeState;
  }
  
  public boolean isProxy()
  {
    return (this.challengeState != null) && (this.challengeState == ChallengeState.PROXY);
  }
  
  protected abstract void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2)
    throws MalformedChallengeException;
  
  public void processChallenge(Header paramHeader)
    throws MalformedChallengeException
  {
    Args.notNull(paramHeader, "Header");
    String str1 = paramHeader.getName();
    CharArrayBuffer localCharArrayBuffer;
    int i;
    if (str1.equalsIgnoreCase("WWW-Authenticate"))
    {
      this.challengeState = ChallengeState.TARGET;
      if (!(paramHeader instanceof FormattedHeader)) {
        break label134;
      }
      localCharArrayBuffer = ((FormattedHeader)paramHeader).getBuffer();
      i = ((FormattedHeader)paramHeader).getValuePos();
    }
    for (;;)
    {
      if ((i >= localCharArrayBuffer.length()) || (!HTTP.isWhitespace(localCharArrayBuffer.charAt(i)))) {
        break label184;
      }
      i++;
      continue;
      if (str1.equalsIgnoreCase("Proxy-Authenticate"))
      {
        this.challengeState = ChallengeState.PROXY;
        break;
      }
      throw new MalformedChallengeException("Unexpected header name: " + str1);
      label134:
      String str2 = paramHeader.getValue();
      if (str2 == null) {
        throw new MalformedChallengeException("Header value is null");
      }
      localCharArrayBuffer = new CharArrayBuffer(str2.length());
      localCharArrayBuffer.append(str2);
      i = 0;
    }
    label184:
    int j = i;
    while ((i < localCharArrayBuffer.length()) && (!HTTP.isWhitespace(localCharArrayBuffer.charAt(i)))) {
      i++;
    }
    String str3 = localCharArrayBuffer.substring(j, i);
    if (!str3.equalsIgnoreCase(getSchemeName())) {
      throw new MalformedChallengeException("Invalid scheme identifier: " + str3);
    }
    parseChallenge(localCharArrayBuffer, i, localCharArrayBuffer.length());
  }
  
  public String toString()
  {
    String str = getSchemeName();
    if (str != null) {
      return str.toUpperCase(Locale.US);
    }
    return super.toString();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.AuthSchemeBase
 * JD-Core Version:    0.7.0.1
 */