package org.apache.http.impl.io;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Immutable;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;

@Immutable
public class DefaultHttpResponseParserFactory
  implements HttpMessageParserFactory<HttpResponse>
{
  public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
  private final LineParser lineParser;
  private final HttpResponseFactory responseFactory;
  
  public DefaultHttpResponseParserFactory()
  {
    this(null, null);
  }
  
  public DefaultHttpResponseParserFactory(LineParser paramLineParser, HttpResponseFactory paramHttpResponseFactory)
  {
    if (paramLineParser != null)
    {
      this.lineParser = paramLineParser;
      if (paramHttpResponseFactory == null) {
        break label30;
      }
    }
    for (;;)
    {
      this.responseFactory = paramHttpResponseFactory;
      return;
      paramLineParser = BasicLineParser.INSTANCE;
      break;
      label30:
      paramHttpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
    }
  }
  
  public HttpMessageParser<HttpResponse> create(SessionInputBuffer paramSessionInputBuffer, MessageConstraints paramMessageConstraints)
  {
    return new DefaultHttpResponseParser(paramSessionInputBuffer, this.lineParser, this.responseFactory, paramMessageConstraints);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.DefaultHttpResponseParserFactory
 * JD-Core Version:    0.7.0.1
 */