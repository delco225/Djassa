package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream
  extends OutputStream
{
  public static final NullOutputStream NULL_OUTPUT_STREAM = new NullOutputStream();
  
  public void write(int paramInt) {}
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {}
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {}
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.NullOutputStream
 * JD-Core Version:    0.7.0.1
 */