package org.apache.http.impl.entity;

import java.io.IOException;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.io.ChunkedInputStream;
import org.apache.http.impl.io.ContentLengthInputStream;
import org.apache.http.impl.io.IdentityInputStream;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

@Deprecated
@Immutable
public class EntityDeserializer
{
  private final ContentLengthStrategy lenStrategy;
  
  public EntityDeserializer(ContentLengthStrategy paramContentLengthStrategy)
  {
    this.lenStrategy = ((ContentLengthStrategy)Args.notNull(paramContentLengthStrategy, "Content length strategy"));
  }
  
  public HttpEntity deserialize(SessionInputBuffer paramSessionInputBuffer, HttpMessage paramHttpMessage)
    throws HttpException, IOException
  {
    Args.notNull(paramSessionInputBuffer, "Session input buffer");
    Args.notNull(paramHttpMessage, "HTTP message");
    return doDeserialize(paramSessionInputBuffer, paramHttpMessage);
  }
  
  protected BasicHttpEntity doDeserialize(SessionInputBuffer paramSessionInputBuffer, HttpMessage paramHttpMessage)
    throws HttpException, IOException
  {
    BasicHttpEntity localBasicHttpEntity = new BasicHttpEntity();
    long l = this.lenStrategy.determineLength(paramHttpMessage);
    if (l == -2L)
    {
      localBasicHttpEntity.setChunked(true);
      localBasicHttpEntity.setContentLength(-1L);
      localBasicHttpEntity.setContent(new ChunkedInputStream(paramSessionInputBuffer));
    }
    for (;;)
    {
      Header localHeader1 = paramHttpMessage.getFirstHeader("Content-Type");
      if (localHeader1 != null) {
        localBasicHttpEntity.setContentType(localHeader1);
      }
      Header localHeader2 = paramHttpMessage.getFirstHeader("Content-Encoding");
      if (localHeader2 != null) {
        localBasicHttpEntity.setContentEncoding(localHeader2);
      }
      return localBasicHttpEntity;
      if (l == -1L)
      {
        localBasicHttpEntity.setChunked(false);
        localBasicHttpEntity.setContentLength(-1L);
        localBasicHttpEntity.setContent(new IdentityInputStream(paramSessionInputBuffer));
      }
      else
      {
        localBasicHttpEntity.setChunked(false);
        localBasicHttpEntity.setContentLength(l);
        localBasicHttpEntity.setContent(new ContentLengthInputStream(paramSessionInputBuffer, l));
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.entity.EntityDeserializer
 * JD-Core Version:    0.7.0.1
 */