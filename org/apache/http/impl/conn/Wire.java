package org.apache.http.impl.conn;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.logging.Log;
import org.apache.http.annotation.Immutable;
import org.apache.http.util.Args;

@Immutable
public class Wire
{
  private final String id;
  private final Log log;
  
  public Wire(Log paramLog)
  {
    this(paramLog, "");
  }
  
  public Wire(Log paramLog, String paramString)
  {
    this.log = paramLog;
    this.id = paramString;
  }
  
  private void wire(String paramString, InputStream paramInputStream)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;)
    {
      int i = paramInputStream.read();
      if (i == -1) {
        break;
      }
      if (i == 13)
      {
        localStringBuilder.append("[\\r]");
      }
      else if (i == 10)
      {
        localStringBuilder.append("[\\n]\"");
        localStringBuilder.insert(0, "\"");
        localStringBuilder.insert(0, paramString);
        this.log.debug(this.id + " " + localStringBuilder.toString());
        localStringBuilder.setLength(0);
      }
      else if ((i < 32) || (i > 127))
      {
        localStringBuilder.append("[0x");
        localStringBuilder.append(Integer.toHexString(i));
        localStringBuilder.append("]");
      }
      else
      {
        localStringBuilder.append((char)i);
      }
    }
    if (localStringBuilder.length() > 0)
    {
      localStringBuilder.append('"');
      localStringBuilder.insert(0, '"');
      localStringBuilder.insert(0, paramString);
      this.log.debug(this.id + " " + localStringBuilder.toString());
    }
  }
  
  public boolean enabled()
  {
    return this.log.isDebugEnabled();
  }
  
  public void input(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = ((byte)paramInt);
    input(arrayOfByte);
  }
  
  public void input(InputStream paramInputStream)
    throws IOException
  {
    Args.notNull(paramInputStream, "Input");
    wire("<< ", paramInputStream);
  }
  
  public void input(String paramString)
    throws IOException
  {
    Args.notNull(paramString, "Input");
    input(paramString.getBytes());
  }
  
  public void input(byte[] paramArrayOfByte)
    throws IOException
  {
    Args.notNull(paramArrayOfByte, "Input");
    wire("<< ", new ByteArrayInputStream(paramArrayOfByte));
  }
  
  public void input(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    Args.notNull(paramArrayOfByte, "Input");
    wire("<< ", new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2));
  }
  
  public void output(int paramInt)
    throws IOException
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = ((byte)paramInt);
    output(arrayOfByte);
  }
  
  public void output(InputStream paramInputStream)
    throws IOException
  {
    Args.notNull(paramInputStream, "Output");
    wire(">> ", paramInputStream);
  }
  
  public void output(String paramString)
    throws IOException
  {
    Args.notNull(paramString, "Output");
    output(paramString.getBytes());
  }
  
  public void output(byte[] paramArrayOfByte)
    throws IOException
  {
    Args.notNull(paramArrayOfByte, "Output");
    wire(">> ", new ByteArrayInputStream(paramArrayOfByte));
  }
  
  public void output(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    Args.notNull(paramArrayOfByte, "Output");
    wire(">> ", new ByteArrayInputStream(paramArrayOfByte, paramInt1, paramInt2));
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.Wire
 * JD-Core Version:    0.7.0.1
 */