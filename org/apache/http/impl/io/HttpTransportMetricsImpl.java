package org.apache.http.impl.io;

import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.io.HttpTransportMetrics;

@NotThreadSafe
public class HttpTransportMetricsImpl
  implements HttpTransportMetrics
{
  private long bytesTransferred = 0L;
  
  public long getBytesTransferred()
  {
    return this.bytesTransferred;
  }
  
  public void incrementBytesTransferred(long paramLong)
  {
    this.bytesTransferred = (paramLong + this.bytesTransferred);
  }
  
  public void reset()
  {
    this.bytesTransferred = 0L;
  }
  
  public void setBytesTransferred(long paramLong)
  {
    this.bytesTransferred = paramLong;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.HttpTransportMetricsImpl
 * JD-Core Version:    0.7.0.1
 */