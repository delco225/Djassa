package org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

final class LogFactoryImpl$2
  implements PrivilegedAction
{
  private final String val$def;
  private final String val$key;
  
  LogFactoryImpl$2(String paramString1, String paramString2)
  {
    this.val$key = paramString1;
    this.val$def = paramString2;
  }
  
  public Object run()
  {
    return System.getProperty(this.val$key, this.val$def);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.LogFactoryImpl.2
 * JD-Core Version:    0.7.0.1
 */