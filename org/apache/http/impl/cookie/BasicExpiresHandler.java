package org.apache.http.impl.cookie;

import java.util.Date;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.cookie.SetCookie;
import org.apache.http.util.Args;

@Immutable
public class BasicExpiresHandler
  extends AbstractCookieAttributeHandler
{
  private final String[] datepatterns;
  
  public BasicExpiresHandler(String[] paramArrayOfString)
  {
    Args.notNull(paramArrayOfString, "Array of date patterns");
    this.datepatterns = paramArrayOfString;
  }
  
  public void parse(SetCookie paramSetCookie, String paramString)
    throws MalformedCookieException
  {
    Args.notNull(paramSetCookie, "Cookie");
    if (paramString == null) {
      throw new MalformedCookieException("Missing value for expires attribute");
    }
    Date localDate = DateUtils.parseDate(paramString, this.datepatterns);
    if (localDate == null) {
      throw new MalformedCookieException("Unable to parse expires attribute: " + paramString);
    }
    paramSetCookie.setExpiryDate(localDate);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.cookie.BasicExpiresHandler
 * JD-Core Version:    0.7.0.1
 */