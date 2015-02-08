package org.apache.commons.logging;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.PrivilegedAction;
import java.util.Properties;

final class LogFactory$5
  implements PrivilegedAction
{
  private final URL val$url;
  
  LogFactory$5(URL paramURL)
  {
    this.val$url = paramURL;
  }
  
  public Object run()
  {
    InputStream localInputStream = null;
    for (;;)
    {
      try
      {
        URLConnection localURLConnection = this.val$url.openConnection();
        localURLConnection.setUseCaches(false);
        localInputStream = localURLConnection.getInputStream();
        if (localInputStream != null)
        {
          Properties localProperties = new Properties();
          localProperties.load(localInputStream);
          localInputStream.close();
          if (0 != 0) {}
          try
          {
            null.close();
            return localProperties;
          }
          catch (IOException localIOException4)
          {
            if (!LogFactory.isDiagnosticsEnabled()) {
              continue;
            }
            LogFactory.access$000("Unable to close stream for URL " + this.val$url);
            return localProperties;
          }
        }
      }
      catch (IOException localIOException2)
      {
        if (!LogFactory.isDiagnosticsEnabled()) {
          continue;
        }
        LogFactory.access$000("Unable to read URL " + this.val$url);
        if (localInputStream == null) {
          continue;
        }
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException3) {}
        if (!LogFactory.isDiagnosticsEnabled()) {
          continue;
        }
        LogFactory.access$000("Unable to close stream for URL " + this.val$url);
        continue;
      }
      finally
      {
        if (localInputStream == null) {
          break label228;
        }
      }
      try
      {
        localInputStream.close();
        return null;
      }
      catch (IOException localIOException5)
      {
        if (LogFactory.isDiagnosticsEnabled()) {
          LogFactory.access$000("Unable to close stream for URL " + this.val$url);
        }
      }
    }
    try
    {
      localInputStream.close();
      label228:
      throw localObject;
    }
    catch (IOException localIOException1)
    {
      for (;;)
      {
        if (LogFactory.isDiagnosticsEnabled()) {
          LogFactory.access$000("Unable to close stream for URL " + this.val$url);
        }
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory.5
 * JD-Core Version:    0.7.0.1
 */