package org.apache.http.protocol;

import java.util.LinkedList;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;

public class HttpProcessorBuilder
{
  private ChainBuilder<HttpRequestInterceptor> requestChainBuilder;
  private ChainBuilder<HttpResponseInterceptor> responseChainBuilder;
  
  public static HttpProcessorBuilder create()
  {
    return new HttpProcessorBuilder();
  }
  
  private ChainBuilder<HttpRequestInterceptor> getRequestChainBuilder()
  {
    if (this.requestChainBuilder == null) {
      this.requestChainBuilder = new ChainBuilder();
    }
    return this.requestChainBuilder;
  }
  
  private ChainBuilder<HttpResponseInterceptor> getResponseChainBuilder()
  {
    if (this.responseChainBuilder == null) {
      this.responseChainBuilder = new ChainBuilder();
    }
    return this.responseChainBuilder;
  }
  
  public HttpProcessorBuilder add(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    return addLast(paramHttpRequestInterceptor);
  }
  
  public HttpProcessorBuilder add(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    return addLast(paramHttpResponseInterceptor);
  }
  
  public HttpProcessorBuilder addAll(HttpRequestInterceptor... paramVarArgs)
  {
    return addAllLast(paramVarArgs);
  }
  
  public HttpProcessorBuilder addAll(HttpResponseInterceptor... paramVarArgs)
  {
    return addAllLast(paramVarArgs);
  }
  
  public HttpProcessorBuilder addAllFirst(HttpRequestInterceptor... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return this;
    }
    getRequestChainBuilder().addAllFirst(paramVarArgs);
    return this;
  }
  
  public HttpProcessorBuilder addAllFirst(HttpResponseInterceptor... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return this;
    }
    getResponseChainBuilder().addAllFirst(paramVarArgs);
    return this;
  }
  
  public HttpProcessorBuilder addAllLast(HttpRequestInterceptor... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return this;
    }
    getRequestChainBuilder().addAllLast(paramVarArgs);
    return this;
  }
  
  public HttpProcessorBuilder addAllLast(HttpResponseInterceptor... paramVarArgs)
  {
    if (paramVarArgs == null) {
      return this;
    }
    getResponseChainBuilder().addAllLast(paramVarArgs);
    return this;
  }
  
  public HttpProcessorBuilder addFirst(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    if (paramHttpRequestInterceptor == null) {
      return this;
    }
    getRequestChainBuilder().addFirst(paramHttpRequestInterceptor);
    return this;
  }
  
  public HttpProcessorBuilder addFirst(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    if (paramHttpResponseInterceptor == null) {
      return this;
    }
    getResponseChainBuilder().addFirst(paramHttpResponseInterceptor);
    return this;
  }
  
  public HttpProcessorBuilder addLast(HttpRequestInterceptor paramHttpRequestInterceptor)
  {
    if (paramHttpRequestInterceptor == null) {
      return this;
    }
    getRequestChainBuilder().addLast(paramHttpRequestInterceptor);
    return this;
  }
  
  public HttpProcessorBuilder addLast(HttpResponseInterceptor paramHttpResponseInterceptor)
  {
    if (paramHttpResponseInterceptor == null) {
      return this;
    }
    getResponseChainBuilder().addLast(paramHttpResponseInterceptor);
    return this;
  }
  
  public HttpProcessor build()
  {
    if (this.requestChainBuilder != null) {}
    for (LinkedList localLinkedList1 = this.requestChainBuilder.build();; localLinkedList1 = null)
    {
      ChainBuilder localChainBuilder = this.responseChainBuilder;
      LinkedList localLinkedList2 = null;
      if (localChainBuilder != null) {
        localLinkedList2 = this.responseChainBuilder.build();
      }
      return new ImmutableHttpProcessor(localLinkedList1, localLinkedList2);
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.protocol.HttpProcessorBuilder
 * JD-Core Version:    0.7.0.1
 */