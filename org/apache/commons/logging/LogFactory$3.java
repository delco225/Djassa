package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$3
  implements PrivilegedAction
{
  private final ClassLoader val$loader;
  private final String val$name;
  
  LogFactory$3(ClassLoader paramClassLoader, String paramString)
  {
    this.val$loader = paramClassLoader;
    this.val$name = paramString;
  }
  
  public Object run()
  {
    if (this.val$loader != null) {
      return this.val$loader.getResourceAsStream(this.val$name);
    }
    return ClassLoader.getSystemResourceAsStream(this.val$name);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory.3
 * JD-Core Version:    0.7.0.1
 */