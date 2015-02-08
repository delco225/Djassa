package org.apache.http.impl.entity;

import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
import org.apache.http.entity.ContentLengthStrategy;

@Immutable
public class DisallowIdentityContentLengthStrategy
  implements ContentLengthStrategy
{
  public static final DisallowIdentityContentLengthStrategy INSTANCE = new DisallowIdentityContentLengthStrategy(new LaxContentLengthStrategy(0));
  private final ContentLengthStrategy contentLengthStrategy;
  
  public DisallowIdentityContentLengthStrategy(ContentLengthStrategy paramContentLengthStrategy)
  {
    this.contentLengthStrategy = paramContentLengthStrategy;
  }
  
  public long determineLength(HttpMessage paramHttpMessage)
    throws HttpException
  {
    long l = this.contentLengthStrategy.determineLength(paramHttpMessage);
    if (l == -1L) {
      throw new ProtocolException("Identity transfer encoding cannot be used");
    }
    return l;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy
 * JD-Core Version:    0.7.0.1
 */