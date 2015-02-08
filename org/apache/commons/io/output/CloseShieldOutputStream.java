package org.apache.commons.io.output;

import java.io.OutputStream;

public class CloseShieldOutputStream
  extends ProxyOutputStream
{
  public CloseShieldOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }
  
  public void close()
  {
    this.out = new ClosedOutputStream();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.CloseShieldOutputStream
 * JD-Core Version:    0.7.0.1
 */