package org.apache.http.protocol;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.annotation.ThreadSafe;

@ThreadSafe
public final class ImmutableHttpProcessor
  implements HttpProcessor
{
  private final HttpRequestInterceptor[] requestInterceptors;
  private final HttpResponseInterceptor[] responseInterceptors;
  
  public ImmutableHttpProcessor(List<HttpRequestInterceptor> paramList, List<HttpResponseInterceptor> paramList1)
  {
    if (paramList != null) {}
    for (this.requestInterceptors = ((HttpRequestInterceptor[])paramList.toArray(new HttpRequestInterceptor[paramList.size()])); paramList1 != null; this.requestInterceptors = new HttpRequestInterceptor[0])
    {
      this.responseInterceptors = ((HttpResponseInterceptor[])paramList1.toArray(new HttpResponseInterceptor[paramList1.size()]));
      return;
    }
    this.responseInterceptors = new HttpResponseInterceptor[0];
  }
  
  @Deprecated
  public ImmutableHttpProcessor(HttpRequestInterceptorList paramHttpRequestInterceptorList, HttpResponseInterceptorList paramHttpResponseInterceptorList)
  {
    if (paramHttpRequestInterceptorList != null)
    {
      int k = paramHttpRequestInterceptorList.getRequestInterceptorCount();
      this.requestInterceptors = new HttpRequestInterceptor[k];
      for (int m = 0; m < k; m++) {
        this.requestInterceptors[m] = paramHttpRequestInterceptorList.getRequestInterceptor(m);
      }
    }
    this.requestInterceptors = new HttpRequestInterceptor[0];
    if (paramHttpResponseInterceptorList != null)
    {
      int i = paramHttpResponseInterceptorList.getResponseInterceptorCount();
      this.responseInterceptors = new HttpResponseInterceptor[i];
      for (int j = 0; j < i; j++) {
        this.responseInterceptors[j] = paramHttpResponseInterceptorList.getResponseInterceptor(j);
      }
    }
    this.responseInterceptors = new HttpResponseInterceptor[0];
  }
  
  public ImmutableHttpProcessor(HttpRequestInterceptor... paramVarArgs)
  {
    this(paramVarArgs, null);
  }
  
  public ImmutableHttpProcessor(HttpRequestInterceptor[] paramArrayOfHttpRequestInterceptor, HttpResponseInterceptor[] paramArrayOfHttpResponseInterceptor)
  {
    if (paramArrayOfHttpRequestInterceptor != null)
    {
      int j = paramArrayOfHttpRequestInterceptor.length;
      this.requestInterceptors = new HttpRequestInterceptor[j];
      System.arraycopy(paramArrayOfHttpRequestInterceptor, 0, this.requestInterceptors, 0, j);
    }
    while (paramArrayOfHttpResponseInterceptor != null)
    {
      int i = paramArrayOfHttpResponseInterceptor.length;
      this.responseInterceptors = new HttpResponseInterceptor[i];
      System.arraycopy(paramArrayOfHttpResponseInterceptor, 0, this.responseInterceptors, 0, i);
      return;
      this.requestInterceptors = new HttpRequestInterceptor[0];
    }
    this.responseInterceptors = new HttpResponseInterceptor[0];
  }
  
  public ImmutableHttpProcessor(HttpResponseInterceptor... paramVarArgs)
  {
    this(null, paramVarArgs);
  }
  
  public void process(HttpRequest paramHttpRequest, HttpContext paramHttpContext)
    throws IOException, HttpException
  {
    HttpRequestInterceptor[] arrayOfHttpRequestInterceptor = this.requestInterceptors;
    int i = arrayOfHttpRequestInterceptor.length;
    for (int j = 0; j < i; j++) {
      arrayOfHttpRequestInterceptor[j].process(paramHttpRequest, paramHttpContext);
    }
  }
  
  public void process(HttpResponse paramHttpResponse, HttpContext paramHttpContext)
    throws IOException, HttpException
  {
    HttpResponseInterceptor[] arrayOfHttpResponseInterceptor = this.responseInterceptors;
    int i = arrayOfHttpResponseInterceptor.length;
    for (int j = 0; j < i; j++) {
      arrayOfHttpResponseInterceptor[j].process(paramHttpResponse, paramHttpContext);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.ImmutableHttpProcessor
 * JD-Core Version:    0.7.0.1
 */