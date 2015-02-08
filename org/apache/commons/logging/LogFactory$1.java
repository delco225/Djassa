package org.apache.commons.logging;

import java.security.PrivilegedAction;

final class LogFactory$1
  implements PrivilegedAction
{
  public Object run()
  {
    return LogFactory.directGetContextClassLoader();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.logging.LogFactory.1
 * JD-Core Version:    0.7.0.1
 */