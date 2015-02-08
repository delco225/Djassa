package org.apache.http.impl.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.ProtocolException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.entity.HttpEntityWrapper;

@Deprecated
@NotThreadSafe
public class EntityEnclosingRequestWrapper
  extends RequestWrapper
  implements HttpEntityEnclosingRequest
{
  private boolean consumed;
  private HttpEntity entity;
  
  public EntityEnclosingRequestWrapper(HttpEntityEnclosingRequest paramHttpEntityEnclosingRequest)
    throws ProtocolException
  {
    super(paramHttpEntityEnclosingRequest);
    setEntity(paramHttpEntityEnclosingRequest.getEntity());
  }
  
  public boolean expectContinue()
  {
    Header localHeader = getFirstHeader("Expect");
    return (localHeader != null) && ("100-continue".equalsIgnoreCase(localHeader.getValue()));
  }
  
  public HttpEntity getEntity()
  {
    return this.entity;
  }
  
  public boolean isRepeatable()
  {
    return (this.entity == null) || (this.entity.isRepeatable()) || (!this.consumed);
  }
  
  public void setEntity(HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity != null) {}
    for (EntityWrapper localEntityWrapper = new EntityWrapper(paramHttpEntity);; localEntityWrapper = null)
    {
      this.entity = localEntityWrapper;
      this.consumed = false;
      return;
    }
  }
  
  class EntityWrapper
    extends HttpEntityWrapper
  {
    EntityWrapper(HttpEntity paramHttpEntity)
    {
      super();
    }
    
    public void consumeContent()
      throws IOException
    {
      EntityEnclosingRequestWrapper.access$002(EntityEnclosingRequestWrapper.this, true);
      super.consumeContent();
    }
    
    public InputStream getContent()
      throws IOException
    {
      EntityEnclosingRequestWrapper.access$002(EntityEnclosingRequestWrapper.this, true);
      return super.getContent();
    }
    
    public void writeTo(OutputStream paramOutputStream)
      throws IOException
    {
      EntityEnclosingRequestWrapper.access$002(EntityEnclosingRequestWrapper.this, true);
      super.writeTo(paramOutputStream);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.client.EntityEnclosingRequestWrapper
 * JD-Core Version:    0.7.0.1
 */