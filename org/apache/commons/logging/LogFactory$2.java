package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$2
  implements PrivilegedAction
{
  private final ClassLoader val$classLoader;
  private final String val$factoryClass;
  
  LogFactory$2(String paramString, ClassLoader paramClassLoader)
  {
    this.val$factoryClass = paramString;
    this.val$classLoader = paramClassLoader;
  }
  
  public Object run()
  {
    return LogFactory.createFactory(this.val$factoryClass, this.val$classLoader);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory.2
 * JD-Core Version:    0.7.0.1
 */