package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Asserts;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@Immutable
public abstract class AbstractAuthenticationHandler
  implements AuthenticationHandler
{
  private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList(new String[] { "negotiate", "NTLM", "Digest", "Basic" }));
  private final Log log = LogFactory.getLog(getClass());
  
  protected List<String> getAuthPreferences()
  {
    return DEFAULT_SCHEME_PRIORITY;
  }
  
  protected List<String> getAuthPreferences(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
  {
    return getAuthPreferences();
  }
  
  protected Map<String, Header> parseChallenges(Header[] paramArrayOfHeader)
    throws MalformedChallengeException
  {
    HashMap localHashMap = new HashMap(paramArrayOfHeader.length);
    int i = paramArrayOfHeader.length;
    for (int j = 0; j < i; j++)
    {
      Header localHeader = paramArrayOfHeader[j];
      CharArrayBuffer localCharArrayBuffer;
      int k;
      if ((localHeader instanceof FormattedHeader))
      {
        localCharArrayBuffer = ((FormattedHeader)localHeader).getBuffer();
        k = ((FormattedHeader)localHeader).getValuePos();
      }
      while ((k < localCharArrayBuffer.length()) && (HTTP.isWhitespace(localCharArrayBuffer.charAt(k))))
      {
        k++;
        continue;
        String str = localHeader.getValue();
        if (str == null) {
          throw new MalformedChallengeException("Header value is null");
        }
        localCharArrayBuffer = new CharArrayBuffer(str.length());
        localCharArrayBuffer.append(str);
        k = 0;
      }
      int m = k;
      while ((k < localCharArrayBuffer.length()) && (!HTTP.isWhitespace(localCharArrayBuffer.charAt(k)))) {
        k++;
      }
      localHashMap.put(localCharArrayBuffer.substring(m, k).toLowerCase(Locale.US), localHeader);
    }
    return localHashMap;
  }
  
  public AuthScheme selectScheme(Map<String, Header> paramMap, HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws AuthenticationException
  {
    AuthSchemeRegistry localAuthSchemeRegistry = (AuthSchemeRegistry)paramHttpContext.getAttribute("http.authscheme-registry");
    Asserts.notNull(localAuthSchemeRegistry, "AuthScheme registry");
    List localList = getAuthPreferences(paramHttpResponse, paramHttpContext);
    if (localList == null) {
      localList = DEFAULT_SCHEME_PRIORITY;
    }
    if (this.log.isDebugEnabled()) {
      this.log.debug("Authentication schemes in the order of preference: " + localList);
    }
    Iterator localIterator = localList.iterator();
    Object localObject;
    for (;;)
    {
      boolean bool = localIterator.hasNext();
      localObject = null;
      String str;
      if (bool)
      {
        str = (String)localIterator.next();
        if ((Header)paramMap.get(str.toLowerCase(Locale.ENGLISH)) == null) {
          break label280;
        }
        if (this.log.isDebugEnabled()) {
          this.log.debug(str + " authentication scheme selected");
        }
      }
      try
      {
        AuthScheme localAuthScheme = localAuthSchemeRegistry.getAuthScheme(str, paramHttpResponse.getParams());
        localObject = localAuthScheme;
        if (localObject != null) {
          break;
        }
        throw new AuthenticationException("Unable to respond to any of these challenges: " + paramMap);
      }
      catch (IllegalStateException localIllegalStateException) {}
      if (this.log.isWarnEnabled())
      {
        this.log.warn("Authentication scheme " + str + " not supported");
        continue;
        label280:
        if (this.log.isDebugEnabled()) {
          this.log.debug("Challenge for " + str + " authentication scheme not available");
        }
      }
    }
    return localObject;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.AbstractAuthenticationHandler
 * JD-Core Version:    0.7.0.1
 */