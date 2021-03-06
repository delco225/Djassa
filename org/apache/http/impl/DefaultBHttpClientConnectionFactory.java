package org.apache.http.impl;

import java.io.IOException;
import java.net.Socket;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class DefaultBHttpClientConnectionFactory
  implements HttpConnectionFactory<DefaultBHttpClientConnection>
{
  public static final DefaultBHttpClientConnectionFactory INSTANCE = new DefaultBHttpClientConnectionFactory();
  private final ConnectionConfig cconfig;
  private final ContentLengthStrategy incomingContentStrategy;
  private final ContentLengthStrategy outgoingContentStrategy;
  private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
  private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
  
  public DefaultBHttpClientConnectionFactory()
  {
    this(null, null, null, null, null);
  }
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig paramConnectionConfig)
  {
    this(paramConnectionConfig, null, null, null, null);
  }
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig paramConnectionConfig, ContentLengthStrategy paramContentLengthStrategy1, ContentLengthStrategy paramContentLengthStrategy2, HttpMessageWriterFactory<HttpRequest> paramHttpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> paramHttpMessageParserFactory)
  {
    if (paramConnectionConfig != null) {}
    for (;;)
    {
      this.cconfig = paramConnectionConfig;
      this.incomingContentStrategy = paramContentLengthStrategy1;
      this.outgoingContentStrategy = paramContentLengthStrategy2;
      this.requestWriterFactory = paramHttpMessageWriterFactory;
      this.responseParserFactory = paramHttpMessageParserFactory;
      return;
      paramConnectionConfig = ConnectionConfig.DEFAULT;
    }
  }
  
  public DefaultBHttpClientConnectionFactory(ConnectionConfig paramConnectionConfig, HttpMessageWriterFactory<HttpRequest> paramHttpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> paramHttpMessageParserFactory)
  {
    this(paramConnectionConfig, null, null, paramHttpMessageWriterFactory, paramHttpMessageParserFactory);
  }
  
  public DefaultBHttpClientConnection createConnection(Socket paramSocket)
    throws IOException
  {
    DefaultBHttpClientConnection localDefaultBHttpClientConnection = new DefaultBHttpClientConnection(this.cconfig.getBufferSize(), this.cconfig.getFragmentSizeHint(), ConnSupport.createDecoder(this.cconfig), ConnSupport.createEncoder(this.cconfig), this.cconfig.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
    localDefaultBHttpClientConnection.bind(paramSocket);
    return localDefaultBHttpClientConnection;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.DefaultBHttpClientConnectionFactory
 * JD-Core Version:    0.7.0.1
 */