package org.apache.commons.io.input;

import java.io.InputStream;

public class CloseShieldInputStream
  extends ProxyInputStream
{
  public CloseShieldInputStream(InputStream paramInputStream)
  {
    super(paramInputStream);
  }
  
  public void close()
  {
    this.in = new ClosedInputStream();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.CloseShieldInputStream
 * JD-Core Version:    0.7.0.1
 */