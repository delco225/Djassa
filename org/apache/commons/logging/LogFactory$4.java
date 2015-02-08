package org.apache.commons.logging;

import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Enumeration;

final class LogFactory$4
  implements PrivilegedAction
{
  private final ClassLoader val$loader;
  private final String val$name;
  
  LogFactory$4(ClassLoader paramClassLoader, String paramString)
  {
    this.val$loader = paramClassLoader;
    this.val$name = paramString;
  }
  
  public Object run()
  {
    try
    {
      if (this.val$loader != null) {
        return this.val$loader.getResources(this.val$name);
      }
      Enumeration localEnumeration = ClassLoader.getSystemResources(this.val$name);
      return localEnumeration;
    }
    catch (IOException localIOException)
    {
      if (LogFactory.isDiagnosticsEnabled())
      {
        LogFactory.access$000("Exception while trying to find configuration file " + this.val$name + ":" + localIOException.getMessage());
        return null;
      }
    }
    catch (NoSuchMethodError localNoSuchMethodError) {}
    return null;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory.4
 * JD-Core Version:    0.7.0.1
 */