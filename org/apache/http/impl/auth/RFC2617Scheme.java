package org.apache.http.impl.auth;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.apache.http.Consts;
import org.apache.http.HeaderElement;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.auth.ChallengeState;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.message.BasicHeaderValueParser;
import org.apache.http.message.HeaderValueParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class RFC2617Scheme
  extends AuthSchemeBase
{
  private final Charset credentialsCharset;
  private final Map<String, String> params = new HashMap();
  
  public RFC2617Scheme()
  {
    this(Consts.ASCII);
  }
  
  public RFC2617Scheme(Charset paramCharset)
  {
    if (paramCharset != null) {}
    for (;;)
    {
      this.credentialsCharset = paramCharset;
      return;
      paramCharset = Consts.ASCII;
    }
  }
  
  @Deprecated
  public RFC2617Scheme(ChallengeState paramChallengeState)
  {
    super(paramChallengeState);
    this.credentialsCharset = Consts.ASCII;
  }
  
  String getCredentialsCharset(HttpRequest paramHttpRequest)
  {
    String str = (String)paramHttpRequest.getParams().getParameter("http.auth.credential-charset");
    if (str == null) {
      str = getCredentialsCharset().name();
    }
    return str;
  }
  
  public Charset getCredentialsCharset()
  {
    return this.credentialsCharset;
  }
  
  public String getParameter(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return (String)this.params.get(paramString.toLowerCase(Locale.ENGLISH));
  }
  
  protected Map<String, String> getParameters()
  {
    return this.params;
  }
  
  public String getRealm()
  {
    return getParameter("realm");
  }
  
  protected void parseChallenge(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2)
    throws MalformedChallengeException
  {
    HeaderElement[] arrayOfHeaderElement = BasicHeaderValueParser.INSTANCE.parseElements(paramCharArrayBuffer, new ParserCursor(paramInt1, paramCharArrayBuffer.length()));
    if (arrayOfHeaderElement.length == 0) {
      throw new MalformedChallengeException("Authentication challenge is empty");
    }
    this.params.clear();
    int i = arrayOfHeaderElement.length;
    for (int j = 0; j < i; j++)
    {
      HeaderElement localHeaderElement = arrayOfHeaderElement[j];
      this.params.put(localHeaderElement.getName(), localHeaderElement.getValue());
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.RFC2617Scheme
 * JD-Core Version:    0.7.0.1
 */