package org.apache.http.impl.execchain;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.conn.EofSensorInputStream;
import org.apache.http.conn.EofSensorWatcher;
import org.apache.http.entity.HttpEntityWrapper;

@NotThreadSafe
class ResponseEntityWrapper
  extends HttpEntityWrapper
  implements EofSensorWatcher
{
  private final ConnectionHolder connReleaseTrigger;
  
  public ResponseEntityWrapper(HttpEntity paramHttpEntity, ConnectionHolder paramConnectionHolder)
  {
    super(paramHttpEntity);
    this.connReleaseTrigger = paramConnectionHolder;
  }
  
  private void cleanup()
  {
    if (this.connReleaseTrigger != null) {
      this.connReleaseTrigger.abortConnection();
    }
  }
  
  @Deprecated
  public void consumeContent()
    throws IOException
  {
    releaseConnection();
  }
  
  public boolean eofDetected(InputStream paramInputStream)
    throws IOException
  {
    try
    {
      paramInputStream.close();
      releaseConnection();
      return false;
    }
    finally
    {
      cleanup();
    }
  }
  
  public InputStream getContent()
    throws IOException
  {
    return new EofSensorInputStream(this.wrappedEntity.getContent(), this);
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  
  public void releaseConnection()
    throws IOException
  {
    if (this.connReleaseTrigger != null) {}
    try
    {
      if (this.connReleaseTrigger.isReusable()) {
        this.connReleaseTrigger.releaseConnection();
      }
      return;
    }
    finally
    {
      cleanup();
    }
  }
  
  public boolean streamAbort(InputStream paramInputStream)
    throws IOException
  {
    cleanup();
    return false;
  }
  
  /* Error */
  public boolean streamClosed(InputStream paramInputStream)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 16	org/apache/http/impl/execchain/ResponseEntityWrapper:connReleaseTrigger	Lorg/apache/http/impl/execchain/ConnectionHolder;
    //   4: ifnull +33 -> 37
    //   7: aload_0
    //   8: getfield 16	org/apache/http/impl/execchain/ResponseEntityWrapper:connReleaseTrigger	Lorg/apache/http/impl/execchain/ConnectionHolder;
    //   11: invokevirtual 67	org/apache/http/impl/execchain/ConnectionHolder:isReleased	()Z
    //   14: istore 5
    //   16: iload 5
    //   18: ifne +19 -> 37
    //   21: iconst_1
    //   22: istore_3
    //   23: aload_1
    //   24: invokevirtual 37	java/io/InputStream:close	()V
    //   27: aload_0
    //   28: invokevirtual 30	org/apache/http/impl/execchain/ResponseEntityWrapper:releaseConnection	()V
    //   31: aload_0
    //   32: invokespecial 39	org/apache/http/impl/execchain/ResponseEntityWrapper:cleanup	()V
    //   35: iconst_0
    //   36: ireturn
    //   37: iconst_0
    //   38: istore_3
    //   39: goto -16 -> 23
    //   42: astore 4
    //   44: iload_3
    //   45: ifeq -14 -> 31
    //   48: aload 4
    //   50: athrow
    //   51: astore_2
    //   52: aload_0
    //   53: invokespecial 39	org/apache/http/impl/execchain/ResponseEntityWrapper:cleanup	()V
    //   56: aload_2
    //   57: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	58	0	this	ResponseEntityWrapper
    //   0	58	1	paramInputStream	InputStream
    //   51	6	2	localObject	Object
    //   22	23	3	i	int
    //   42	7	4	localSocketException	java.net.SocketException
    //   14	3	5	bool	boolean
    // Exception table:
    //   from	to	target	type
    //   23	31	42	java/net/SocketException
    //   0	16	51	finally
    //   23	31	51	finally
    //   48	51	51	finally
  }
  
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    try
    {
      this.wrappedEntity.writeTo(paramOutputStream);
      releaseConnection();
      return;
    }
    finally
    {
      cleanup();
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.execchain.ResponseEntityWrapper
 * JD-Core Version:    0.7.0.1
 */