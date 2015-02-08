package org.apache.http.impl.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.MessageConstraintException;
import org.apache.http.ParseException;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public abstract class AbstractMessageParser<T extends HttpMessage>
  implements HttpMessageParser<T>
{
  private static final int HEADERS = 1;
  private static final int HEAD_LINE;
  private final List<CharArrayBuffer> headerLines;
  protected final LineParser lineParser;
  private T message;
  private final MessageConstraints messageConstraints;
  private final SessionInputBuffer sessionBuffer;
  private int state;
  
  public AbstractMessageParser(SessionInputBuffer paramSessionInputBuffer, LineParser paramLineParser, MessageConstraints paramMessageConstraints)
  {
    this.sessionBuffer = ((SessionInputBuffer)Args.notNull(paramSessionInputBuffer, "Session input buffer"));
    if (paramLineParser != null)
    {
      this.lineParser = paramLineParser;
      if (paramMessageConstraints == null) {
        break label59;
      }
    }
    for (;;)
    {
      this.messageConstraints = paramMessageConstraints;
      this.headerLines = new ArrayList();
      this.state = 0;
      return;
      paramLineParser = BasicLineParser.INSTANCE;
      break;
      label59:
      paramMessageConstraints = MessageConstraints.DEFAULT;
    }
  }
  
  @Deprecated
  public AbstractMessageParser(SessionInputBuffer paramSessionInputBuffer, LineParser paramLineParser, HttpParams paramHttpParams)
  {
    Args.notNull(paramSessionInputBuffer, "Session input buffer");
    Args.notNull(paramHttpParams, "HTTP parameters");
    this.sessionBuffer = paramSessionInputBuffer;
    this.messageConstraints = HttpParamConfig.getMessageConstraints(paramHttpParams);
    if (paramLineParser != null) {}
    for (;;)
    {
      this.lineParser = paramLineParser;
      this.headerLines = new ArrayList();
      this.state = 0;
      return;
      paramLineParser = BasicLineParser.INSTANCE;
    }
  }
  
  public static Header[] parseHeaders(SessionInputBuffer paramSessionInputBuffer, int paramInt1, int paramInt2, LineParser paramLineParser)
    throws HttpException, IOException
  {
    ArrayList localArrayList = new ArrayList();
    if (paramLineParser != null) {}
    for (;;)
    {
      return parseHeaders(paramSessionInputBuffer, paramInt1, paramInt2, paramLineParser, localArrayList);
      paramLineParser = BasicLineParser.INSTANCE;
    }
  }
  
  public static Header[] parseHeaders(SessionInputBuffer paramSessionInputBuffer, int paramInt1, int paramInt2, LineParser paramLineParser, List<CharArrayBuffer> paramList)
    throws HttpException, IOException
  {
    Args.notNull(paramSessionInputBuffer, "Session input buffer");
    Args.notNull(paramLineParser, "Line parser");
    Args.notNull(paramList, "Header line list");
    CharArrayBuffer localCharArrayBuffer1 = null;
    CharArrayBuffer localCharArrayBuffer2 = null;
    Header[] arrayOfHeader;
    for (;;)
    {
      if (localCharArrayBuffer1 == null) {
        localCharArrayBuffer1 = new CharArrayBuffer(64);
      }
      while ((paramSessionInputBuffer.readLine(localCharArrayBuffer1) == -1) || (localCharArrayBuffer1.length() < 1))
      {
        arrayOfHeader = new Header[paramList.size()];
        int i = 0;
        while (i < paramList.size())
        {
          CharArrayBuffer localCharArrayBuffer3 = (CharArrayBuffer)paramList.get(i);
          try
          {
            arrayOfHeader[i] = paramLineParser.parseHeader(localCharArrayBuffer3);
            i++;
          }
          catch (ParseException localParseException)
          {
            int j;
            throw new ProtocolException(localParseException.getMessage());
          }
        }
        localCharArrayBuffer1.clear();
      }
      if (((localCharArrayBuffer1.charAt(0) == ' ') || (localCharArrayBuffer1.charAt(0) == '\t')) && (localCharArrayBuffer2 != null))
      {
        for (j = 0;; j++) {
          if (j < localCharArrayBuffer1.length())
          {
            int k = localCharArrayBuffer1.charAt(j);
            if ((k == 32) || (k == 9)) {}
          }
          else
          {
            if ((paramInt2 <= 0) || (1 + localCharArrayBuffer2.length() + localCharArrayBuffer1.length() - j <= paramInt2)) {
              break;
            }
            throw new MessageConstraintException("Maximum line length limit exceeded");
          }
        }
        localCharArrayBuffer2.append(' ');
        localCharArrayBuffer2.append(localCharArrayBuffer1, j, localCharArrayBuffer1.length() - j);
      }
      while ((paramInt1 > 0) && (paramList.size() >= paramInt1))
      {
        throw new MessageConstraintException("Maximum header count exceeded");
        paramList.add(localCharArrayBuffer1);
        localCharArrayBuffer2 = localCharArrayBuffer1;
        localCharArrayBuffer1 = null;
      }
    }
    return arrayOfHeader;
  }
  
  public T parse()
    throws IOException, HttpException
  {
    switch (this.state)
    {
    default: 
      throw new IllegalStateException("Inconsistent parser state");
    }
    try
    {
      this.message = parseHead(this.sessionBuffer);
      this.state = 1;
      Header[] arrayOfHeader = parseHeaders(this.sessionBuffer, this.messageConstraints.getMaxHeaderCount(), this.messageConstraints.getMaxLineLength(), this.lineParser, this.headerLines);
      this.message.setHeaders(arrayOfHeader);
      HttpMessage localHttpMessage = this.message;
      this.message = null;
      this.headerLines.clear();
      this.state = 0;
      return localHttpMessage;
    }
    catch (ParseException localParseException)
    {
      throw new ProtocolException(localParseException.getMessage(), localParseException);
    }
  }
  
  protected abstract T parseHead(SessionInputBuffer paramSessionInputBuffer)
    throws IOException, HttpException, ParseException;
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.AbstractMessageParser
 * JD-Core Version:    0.7.0.1
 */