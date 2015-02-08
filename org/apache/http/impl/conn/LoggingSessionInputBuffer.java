package org.apache.http.impl.conn;

import java.io.IOException;
import java.nio.charset.Charset;
import org.apache.http.Consts;
import org.apache.http.annotation.Immutable;
import org.apache.http.io.EofSensor;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@Immutable
public class LoggingSessionInputBuffer
  implements SessionInputBuffer, EofSensor
{
  private final String charset;
  private final EofSensor eofSensor;
  private final SessionInputBuffer in;
  private final Wire wire;
  
  public LoggingSessionInputBuffer(SessionInputBuffer paramSessionInputBuffer, Wire paramWire)
  {
    this(paramSessionInputBuffer, paramWire, null);
  }
  
  public LoggingSessionInputBuffer(SessionInputBuffer paramSessionInputBuffer, Wire paramWire, String paramString)
  {
    this.in = paramSessionInputBuffer;
    EofSensor localEofSensor;
    if ((paramSessionInputBuffer instanceof EofSensor))
    {
      localEofSensor = (EofSensor)paramSessionInputBuffer;
      this.eofSensor = localEofSensor;
      this.wire = paramWire;
      if (paramString == null) {
        break label49;
      }
    }
    for (;;)
    {
      this.charset = paramString;
      return;
      localEofSensor = null;
      break;
      label49:
      paramString = Consts.ASCII.name();
    }
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return this.in.getMetrics();
  }
  
  public boolean isDataAvailable(int paramInt)
    throws IOException
  {
    return this.in.isDataAvailable(paramInt);
  }
  
  public boolean isEof()
  {
    if (this.eofSensor != null) {
      return this.eofSensor.isEof();
    }
    return false;
  }
  
  public int read()
    throws IOException
  {
    int i = this.in.read();
    if ((this.wire.enabled()) && (i != -1)) {
      this.wire.input(i);
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    int i = this.in.read(paramArrayOfByte);
    if ((this.wire.enabled()) && (i > 0)) {
      this.wire.input(paramArrayOfByte, 0, i);
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
    if ((this.wire.enabled()) && (i > 0)) {
      this.wire.input(paramArrayOfByte, paramInt1, i);
    }
    return i;
  }
  
  public int readLine(CharArrayBuffer paramCharArrayBuffer)
    throws IOException
  {
    int i = this.in.readLine(paramCharArrayBuffer);
    if ((this.wire.enabled()) && (i >= 0))
    {
      int j = paramCharArrayBuffer.length() - i;
      String str1 = new String(paramCharArrayBuffer.buffer(), j, i);
      String str2 = str1 + "\r\n";
      this.wire.input(str2.getBytes(this.charset));
    }
    return i;
  }
  
  public String readLine()
    throws IOException
  {
    String str1 = this.in.readLine();
    if ((this.wire.enabled()) && (str1 != null))
    {
      String str2 = str1 + "\r\n";
      this.wire.input(str2.getBytes(this.charset));
    }
    return str1;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.LoggingSessionInputBuffer
 * JD-Core Version:    0.7.0.1
 */