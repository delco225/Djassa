package org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;

@Immutable
public class ManagedHttpClientConnectionFactory
  implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection>
{
  private static final AtomicLong COUNTER = new AtomicLong();
  public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
  private final Log headerlog = LogFactory.getLog("org.apache.http.headers");
  private final Log log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
  private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
  private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
  private final Log wirelog = LogFactory.getLog("org.apache.http.wire");
  
  public ManagedHttpClientConnectionFactory()
  {
    this(null, null);
  }
  
  public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> paramHttpMessageParserFactory)
  {
    this(null, paramHttpMessageParserFactory);
  }
  
  public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> paramHttpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> paramHttpMessageParserFactory)
  {
    if (paramHttpMessageWriterFactory != null)
    {
      this.requestWriterFactory = paramHttpMessageWriterFactory;
      if (paramHttpMessageParserFactory == null) {
        break label57;
      }
    }
    for (;;)
    {
      this.responseParserFactory = paramHttpMessageParserFactory;
      return;
      paramHttpMessageWriterFactory = DefaultHttpRequestWriterFactory.INSTANCE;
      break;
      label57:
      paramHttpMessageParserFactory = DefaultHttpResponseParserFactory.INSTANCE;
    }
  }
  
  public ManagedHttpClientConnection create(HttpRoute paramHttpRoute, ConnectionConfig paramConnectionConfig)
  {
    ConnectionConfig localConnectionConfig;
    Charset localCharset;
    CodingErrorAction localCodingErrorAction1;
    if (paramConnectionConfig != null)
    {
      localConnectionConfig = paramConnectionConfig;
      localCharset = localConnectionConfig.getCharset();
      if (localConnectionConfig.getMalformedInputAction() == null) {
        break label175;
      }
      localCodingErrorAction1 = localConnectionConfig.getMalformedInputAction();
      label25:
      if (localConnectionConfig.getUnmappableInputAction() == null) {
        break label183;
      }
    }
    label175:
    label183:
    for (CodingErrorAction localCodingErrorAction2 = localConnectionConfig.getUnmappableInputAction();; localCodingErrorAction2 = CodingErrorAction.REPORT)
    {
      CharsetDecoder localCharsetDecoder = null;
      CharsetEncoder localCharsetEncoder = null;
      if (localCharset != null)
      {
        localCharsetDecoder = localCharset.newDecoder();
        localCharsetDecoder.onMalformedInput(localCodingErrorAction1);
        localCharsetDecoder.onUnmappableCharacter(localCodingErrorAction2);
        localCharsetEncoder = localCharset.newEncoder();
        localCharsetEncoder.onMalformedInput(localCodingErrorAction1);
        localCharsetEncoder.onUnmappableCharacter(localCodingErrorAction2);
      }
      return new LoggingManagedHttpClientConnection("http-outgoing-" + Long.toString(COUNTER.getAndIncrement()), this.log, this.headerlog, this.wirelog, localConnectionConfig.getBufferSize(), localConnectionConfig.getFragmentSizeHint(), localCharsetDecoder, localCharsetEncoder, localConnectionConfig.getMessageConstraints(), null, null, this.requestWriterFactory, this.responseParserFactory);
      localConnectionConfig = ConnectionConfig.DEFAULT;
      break;
      localCodingErrorAction1 = CodingErrorAction.REPORT;
      break label25;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.ManagedHttpClientConnectionFactory
 * JD-Core Version:    0.7.0.1
 */