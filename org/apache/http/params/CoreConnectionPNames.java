package org.apache.http.params;

@Deprecated
public abstract interface CoreConnectionPNames
{
  public static final String CONNECTION_TIMEOUT = "http.connection.timeout";
  public static final String MAX_HEADER_COUNT = "http.connection.max-header-count";
  public static final String MAX_LINE_LENGTH = "http.connection.max-line-length";
  public static final String MIN_CHUNK_LIMIT = "http.connection.min-chunk-limit";
  public static final String SOCKET_BUFFER_SIZE = "http.socket.buffer-size";
  public static final String SO_KEEPALIVE = "http.socket.keepalive";
  public static final String SO_LINGER = "http.socket.linger";
  public static final String SO_REUSEADDR = "http.socket.reuseaddr";
  public static final String SO_TIMEOUT = "http.socket.timeout";
  public static final String STALE_CONNECTION_CHECK = "http.connection.stalecheck";
  public static final String TCP_NODELAY = "http.tcp.nodelay";
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.params.CoreConnectionPNames
 * JD-Core Version:    0.7.0.1
 */