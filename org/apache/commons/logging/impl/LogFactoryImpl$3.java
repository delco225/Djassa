package org.apache.commons.logging.impl;

import java.security.PrivilegedAction;

class LogFactoryImpl$3
  implements PrivilegedAction
{
  private final LogFactoryImpl this$0;
  private final ClassLoader val$cl;
  
  LogFactoryImpl$3(LogFactoryImpl paramLogFactoryImpl, ClassLoader paramClassLoader)
  {
    this.this$0 = paramLogFactoryImpl;
    this.val$cl = paramClassLoader;
  }
  
  public Object run()
  {
    return this.val$cl.getParent();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.impl.LogFactoryImpl.3
 * JD-Core Version:    0.7.0.1
 */