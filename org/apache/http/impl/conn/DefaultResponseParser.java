package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponseFactory;
import org.apache.http.NoHttpResponseException;
import org.apache.http.ProtocolException;
import org.apache.http.StatusLine;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.impl.io.AbstractMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.LineParser;
import org.apache.http.message.ParserCursor;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@Deprecated
@ThreadSafe
public class DefaultResponseParser
  extends AbstractMessageParser<HttpMessage>
{
  private final CharArrayBuffer lineBuf;
  private final Log log = LogFactory.getLog(getClass());
  private final int maxGarbageLines;
  private final HttpResponseFactory responseFactory;
  
  public DefaultResponseParser(SessionInputBuffer paramSessionInputBuffer, LineParser paramLineParser, HttpResponseFactory paramHttpResponseFactory, HttpParams paramHttpParams)
  {
    super(paramSessionInputBuffer, paramLineParser, paramHttpParams);
    Args.notNull(paramHttpResponseFactory, "Response factory");
    this.responseFactory = paramHttpResponseFactory;
    this.lineBuf = new CharArrayBuffer(128);
    this.maxGarbageLines = getMaxGarbageLines(paramHttpParams);
  }
  
  protected int getMaxGarbageLines(HttpParams paramHttpParams)
  {
    return paramHttpParams.getIntParameter("http.connection.max-status-line-garbage", 2147483647);
  }
  
  protected HttpMessage parseHead(SessionInputBuffer paramSessionInputBuffer)
    throws IOException, HttpException
  {
    for (int i = 0;; i++)
    {
      this.lineBuf.clear();
      int j = paramSessionInputBuffer.readLine(this.lineBuf);
      if ((j == -1) && (i == 0)) {
        throw new NoHttpResponseException("The target server failed to respond");
      }
      ParserCursor localParserCursor = new ParserCursor(0, this.lineBuf.length());
      if (this.lineParser.hasProtocolVersion(this.lineBuf, localParserCursor))
      {
        StatusLine localStatusLine = this.lineParser.parseStatusLine(this.lineBuf, localParserCursor);
        return this.responseFactory.newHttpResponse(localStatusLine, null);
      }
      if ((j == -1) || (i >= this.maxGarbageLines)) {
        throw new ProtocolException("The server failed to respond with a valid HTTP response");
      }
      if (this.log.isDebugEnabled()) {
        this.log.debug("Garbage in response: " + this.lineBuf.toString());
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.conn.DefaultResponseParser
 * JD-Core Version:    0.7.0.1
 */