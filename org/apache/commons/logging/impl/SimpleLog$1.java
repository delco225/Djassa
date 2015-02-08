package org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

final class SimpleLog$1
  implements PrivilegedAction
{
  private final String val$name;
  
  SimpleLog$1(String paramString)
  {
    this.val$name = paramString;
  }
  
  public Object run()
  {
    ClassLoader localClassLoader = SimpleLog.access$000();
    if (localClassLoader != null) {
      return localClassLoader.getResourceAsStream(this.val$name);
    }
    return ClassLoader.getSystemResourceAsStream(this.val$name);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.SimpleLog.1
 * JD-Core Version:    0.7.0.1
 */