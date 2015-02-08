package org.apache.http.client.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Immutable
public class URIUtils
{
  @Deprecated
  public static URI createURI(String paramString1, String paramString2, int paramInt, String paramString3, String paramString4, String paramString5)
    throws URISyntaxException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramString2 != null)
    {
      if (paramString1 != null)
      {
        localStringBuilder.append(paramString1);
        localStringBuilder.append("://");
      }
      localStringBuilder.append(paramString2);
      if (paramInt > 0)
      {
        localStringBuilder.append(':');
        localStringBuilder.append(paramInt);
      }
    }
    if ((paramString3 == null) || (!paramString3.startsWith("/"))) {
      localStringBuilder.append('/');
    }
    if (paramString3 != null) {
      localStringBuilder.append(paramString3);
    }
    if (paramString4 != null)
    {
      localStringBuilder.append('?');
      localStringBuilder.append(paramString4);
    }
    if (paramString5 != null)
    {
      localStringBuilder.append('#');
      localStringBuilder.append(paramString5);
    }
    return new URI(localStringBuilder.toString());
  }
  
  public static HttpHost extractHost(URI paramURI)
  {
    if (paramURI == null) {}
    int i;
    String str1;
    label129:
    label162:
    do
    {
      do
      {
        return null;
      } while (!paramURI.isAbsolute());
      i = paramURI.getPort();
      str1 = paramURI.getHost();
      if (str1 == null)
      {
        str1 = paramURI.getAuthority();
        if (str1 != null)
        {
          int j = str1.indexOf('@');
          if (j >= 0)
          {
            if (str1.length() <= j + 1) {
              break label129;
            }
            str1 = str1.substring(j + 1);
          }
          while (str1 != null)
          {
            int k = str1.indexOf(':');
            int m;
            int i2;
            if (k >= 0)
            {
              m = k + 1;
              int n = 0;
              int i1 = m;
              for (;;)
              {
                if ((i1 < str1.length()) && (Character.isDigit(str1.charAt(i1))))
                {
                  n++;
                  i1++;
                  continue;
                  str1 = null;
                  break;
                }
              }
              if (n > 0) {
                i2 = m + n;
              }
            }
            try
            {
              int i3 = Integer.parseInt(str1.substring(m, i2));
              i = i3;
            }
            catch (NumberFormatException localNumberFormatException)
            {
              String str2;
              break label162;
            }
            str1 = str1.substring(0, k);
          }
        }
      }
      str2 = paramURI.getScheme();
    } while (str1 == null);
    return new HttpHost(str1, i, str2);
  }
  
  private static URI normalizeSyntax(URI paramURI)
  {
    URI localURI1;
    if ((paramURI.isOpaque()) || (paramURI.getAuthority() == null)) {
      localURI1 = paramURI;
    }
    for (;;)
    {
      return localURI1;
      Args.check(paramURI.isAbsolute(), "Base URI must be absolute");
      String str1;
      Stack localStack;
      int j;
      label60:
      String str3;
      if (paramURI.getPath() == null)
      {
        str1 = "";
        String[] arrayOfString = str1.split("/");
        localStack = new Stack();
        int i = arrayOfString.length;
        j = 0;
        if (j >= i) {
          break label143;
        }
        str3 = arrayOfString[j];
        if ((str3.length() != 0) && (!".".equals(str3))) {
          break label105;
        }
      }
      for (;;)
      {
        j++;
        break label60;
        str1 = paramURI.getPath();
        break;
        label105:
        if ("..".equals(str3))
        {
          if (!localStack.isEmpty()) {
            localStack.pop();
          }
        }
        else {
          localStack.push(str3);
        }
      }
      label143:
      StringBuilder localStringBuilder1 = new StringBuilder();
      Iterator localIterator = localStack.iterator();
      while (localIterator.hasNext())
      {
        String str2 = (String)localIterator.next();
        localStringBuilder1.append('/').append(str2);
      }
      if (str1.lastIndexOf('/') == -1 + str1.length()) {
        localStringBuilder1.append('/');
      }
      try
      {
        localURI1 = new URI(paramURI.getScheme().toLowerCase(), paramURI.getAuthority().toLowerCase(), localStringBuilder1.toString(), null, null);
        if ((paramURI.getQuery() == null) && (paramURI.getFragment() == null)) {
          continue;
        }
        StringBuilder localStringBuilder2 = new StringBuilder(localURI1.toASCIIString());
        if (paramURI.getQuery() != null) {
          localStringBuilder2.append('?').append(paramURI.getRawQuery());
        }
        if (paramURI.getFragment() != null) {
          localStringBuilder2.append('#').append(paramURI.getRawFragment());
        }
        URI localURI2 = URI.create(localStringBuilder2.toString());
        return localURI2;
      }
      catch (URISyntaxException localURISyntaxException)
      {
        throw new IllegalArgumentException(localURISyntaxException);
      }
    }
  }
  
  public static URI resolve(URI paramURI, String paramString)
  {
    return resolve(paramURI, URI.create(paramString));
  }
  
  public static URI resolve(URI paramURI1, URI paramURI2)
  {
    Args.notNull(paramURI1, "Base URI");
    Args.notNull(paramURI2, "Reference URI");
    URI localURI1 = paramURI2;
    String str1 = localURI1.toString();
    if (str1.startsWith("?")) {
      return resolveReferenceStartingWithQueryString(paramURI1, localURI1);
    }
    if (str1.length() == 0) {}
    for (int i = 1;; i = 0)
    {
      if (i != 0) {
        localURI1 = URI.create("#");
      }
      URI localURI2 = paramURI1.resolve(localURI1);
      if (i != 0)
      {
        String str2 = localURI2.toString();
        localURI2 = URI.create(str2.substring(0, str2.indexOf('#')));
      }
      return normalizeSyntax(localURI2);
    }
  }
  
  public static URI resolve(URI paramURI, HttpHost paramHttpHost, List<URI> paramList)
    throws URISyntaxException
  {
    Args.notNull(paramURI, "Request URI");
    URIBuilder localURIBuilder;
    if ((paramList == null) || (paramList.isEmpty())) {
      localURIBuilder = new URIBuilder(paramURI);
    }
    for (;;)
    {
      if (localURIBuilder.getFragment() == null) {
        localURIBuilder.setFragment(paramURI.getFragment());
      }
      if ((paramHttpHost != null) && (!localURIBuilder.isAbsolute()))
      {
        localURIBuilder.setScheme(paramHttpHost.getSchemeName());
        localURIBuilder.setHost(paramHttpHost.getHostName());
        localURIBuilder.setPort(paramHttpHost.getPort());
      }
      return localURIBuilder.build();
      localURIBuilder = new URIBuilder((URI)paramList.get(-1 + paramList.size()));
      String str = localURIBuilder.getFragment();
      for (int i = -1 + paramList.size(); (str == null) && (i >= 0); i--) {
        str = ((URI)paramList.get(i)).getFragment();
      }
      localURIBuilder.setFragment(str);
    }
  }
  
  private static URI resolveReferenceStartingWithQueryString(URI paramURI1, URI paramURI2)
  {
    String str = paramURI1.toString();
    if (str.indexOf('?') > -1) {
      str = str.substring(0, str.indexOf('?'));
    }
    return URI.create(str + paramURI2.toString());
  }
  
  public static URI rewriteURI(URI paramURI)
    throws URISyntaxException
  {
    Args.notNull(paramURI, "URI");
    if (paramURI.isOpaque()) {
      return paramURI;
    }
    URIBuilder localURIBuilder = new URIBuilder(paramURI);
    if (localURIBuilder.getUserInfo() != null) {
      localURIBuilder.setUserInfo(null);
    }
    if (TextUtils.isEmpty(localURIBuilder.getPath())) {
      localURIBuilder.setPath("/");
    }
    if (localURIBuilder.getHost() != null) {
      localURIBuilder.setHost(localURIBuilder.getHost().toLowerCase(Locale.ENGLISH));
    }
    localURIBuilder.setFragment(null);
    return localURIBuilder.build();
  }
  
  public static URI rewriteURI(URI paramURI, HttpHost paramHttpHost)
    throws URISyntaxException
  {
    return rewriteURI(paramURI, paramHttpHost, false);
  }
  
  public static URI rewriteURI(URI paramURI, HttpHost paramHttpHost, boolean paramBoolean)
    throws URISyntaxException
  {
    Args.notNull(paramURI, "URI");
    if (paramURI.isOpaque()) {
      return paramURI;
    }
    URIBuilder localURIBuilder = new URIBuilder(paramURI);
    if (paramHttpHost != null)
    {
      localURIBuilder.setScheme(paramHttpHost.getSchemeName());
      localURIBuilder.setHost(paramHttpHost.getHostName());
      localURIBuilder.setPort(paramHttpHost.getPort());
    }
    for (;;)
    {
      if (paramBoolean) {
        localURIBuilder.setFragment(null);
      }
      if (TextUtils.isEmpty(localURIBuilder.getPath())) {
        localURIBuilder.setPath("/");
      }
      return localURIBuilder.build();
      localURIBuilder.setScheme(null);
      localURIBuilder.setHost(null);
      localURIBuilder.setPort(-1);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.client.utils.URIUtils
 * JD-Core Version:    0.7.0.1
 */