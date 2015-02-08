package org.apache.http.cookie;

import java.io.Serializable;
import java.util.Comparator;
import org.apache.http.annotation.Immutable;

@Immutable
public class CookiePathComparator
  implements Serializable, Comparator<Cookie>
{
  private static final long serialVersionUID = 7523645369616405818L;
  
  private String normalizePath(Cookie paramCookie)
  {
    String str = paramCookie.getPath();
    if (str == null) {
      str = "/";
    }
    if (!str.endsWith("/")) {
      str = str + '/';
    }
    return str;
  }
  
  public int compare(Cookie paramCookie1, Cookie paramCookie2)
  {
    String str1 = normalizePath(paramCookie1);
    String str2 = normalizePath(paramCookie2);
    if (str1.equals(str2)) {}
    do
    {
      return 0;
      if (str1.startsWith(str2)) {
        return -1;
      }
    } while (!str2.startsWith(str1));
    return 1;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.cookie.CookiePathComparator
 * JD-Core Version:    0.7.0.1
 */