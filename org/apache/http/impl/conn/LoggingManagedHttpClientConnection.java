package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@NotThreadSafe
class LoggingManagedHttpClientConnection
  extends DefaultManagedHttpClientConnection
{
  private final Log headerlog;
  private final Log log;
  private final Wire wire;
  
  public LoggingManagedHttpClientConnection(String paramString, Log paramLog1, Log paramLog2, Log paramLog3, int paramInt1, int paramInt2, CharsetDecoder paramCharsetDecoder, CharsetEncoder paramCharsetEncoder, MessageConstraints paramMessageConstraints, ContentLengthStrategy paramContentLengthStrategy1, ContentLengthStrategy paramContentLengthStrategy2, HttpMessageWriterFactory<HttpRequest> paramHttpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> paramHttpMessageParserFactory)
  {
    super(paramString, paramInt1, paramInt2, paramCharsetDecoder, paramCharsetEncoder, paramMessageConstraints, paramContentLengthStrategy1, paramContentLengthStrategy2, paramHttpMessageWriterFactory, paramHttpMessageParserFactory);
    this.log = paramLog1;
    this.headerlog = paramLog2;
    this.wire = new Wire(paramLog3, paramString);
  }
  
  public void close()
    throws IOException
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug(getId() + ": Close connection");
    }
    super.close();
  }
  
  protected InputStream getSocketInputStream(Socket paramSocket)
    throws IOException
  {
    Object localObject = super.getSocketInputStream(paramSocket);
    if (this.wire.enabled()) {
      localObject = new LoggingInputStream((InputStream)localObject, this.wire);
    }
    return localObject;
  }
  
  protected OutputStream getSocketOutputStream(Socket paramSocket)
    throws IOException
  {
    Object localObject = super.getSocketOutputStream(paramSocket);
    if (this.wire.enabled()) {
      localObject = new LoggingOutputStream((OutputStream)localObject, this.wire);
    }
    return localObject;
  }
  
  protected void onRequestSubmitted(HttpRequest paramHttpRequest)
  {
    if ((paramHttpRequest != null) && (this.headerlog.isDebugEnabled()))
    {
      this.headerlog.debug(getId() + " >> " + paramHttpRequest.getRequestLine().toString());
      for (Header localHeader : paramHttpRequest.getAllHeaders()) {
        this.headerlog.debug(getId() + " >> " + localHeader.toString());
      }
    }
  }
  
  protected void onResponseReceived(HttpResponse paramHttpResponse)
  {
    if ((paramHttpResponse != null) && (this.headerlog.isDebugEnabled()))
    {
      this.headerlog.debug(getId() + " << " + paramHttpResponse.getStatusLine().toString());
      for (Header localHeader : paramHttpResponse.getAllHeaders()) {
        this.headerlog.debug(getId() + " << " + localHeader.toString());
      }
    }
  }
  
  public void shutdown()
    throws IOException
  {
    if (this.log.isDebugEnabled()) {
      this.log.debug(getId() + ": Shutdown connection");
    }
    super.shutdown();
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.LoggingManagedHttpClientConnection
 * JD-Core Version:    0.7.0.1
 */