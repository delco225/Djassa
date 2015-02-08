package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import org.apache.http.MessageConstraintException;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.config.MessageConstraints;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.HttpTransportMetrics;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.CharArrayBuffer;

@NotThreadSafe
public class SessionInputBufferImpl
  implements SessionInputBuffer, BufferInfo
{
  private final byte[] buffer;
  private int bufferlen;
  private int bufferpos;
  private CharBuffer cbuf;
  private final MessageConstraints constraints;
  private final CharsetDecoder decoder;
  private InputStream instream;
  private final ByteArrayBuffer linebuffer;
  private final HttpTransportMetricsImpl metrics;
  private final int minChunkLimit;
  
  public SessionInputBufferImpl(HttpTransportMetricsImpl paramHttpTransportMetricsImpl, int paramInt)
  {
    this(paramHttpTransportMetricsImpl, paramInt, paramInt, null, null);
  }
  
  public SessionInputBufferImpl(HttpTransportMetricsImpl paramHttpTransportMetricsImpl, int paramInt1, int paramInt2, MessageConstraints paramMessageConstraints, CharsetDecoder paramCharsetDecoder)
  {
    Args.notNull(paramHttpTransportMetricsImpl, "HTTP transport metrcis");
    Args.positive(paramInt1, "Buffer size");
    this.metrics = paramHttpTransportMetricsImpl;
    this.buffer = new byte[paramInt1];
    this.bufferpos = 0;
    this.bufferlen = 0;
    if (paramInt2 >= 0)
    {
      this.minChunkLimit = paramInt2;
      if (paramMessageConstraints == null) {
        break label86;
      }
    }
    for (;;)
    {
      this.constraints = paramMessageConstraints;
      this.linebuffer = new ByteArrayBuffer(paramInt1);
      this.decoder = paramCharsetDecoder;
      return;
      paramInt2 = 512;
      break;
      label86:
      paramMessageConstraints = MessageConstraints.DEFAULT;
    }
  }
  
  private int appendDecoded(CharArrayBuffer paramCharArrayBuffer, ByteBuffer paramByteBuffer)
    throws IOException
  {
    if (!paramByteBuffer.hasRemaining()) {
      return 0;
    }
    if (this.cbuf == null) {
      this.cbuf = CharBuffer.allocate(1024);
    }
    this.decoder.reset();
    int i = 0;
    while (paramByteBuffer.hasRemaining()) {
      i += handleDecodingResult(this.decoder.decode(paramByteBuffer, this.cbuf, true), paramCharArrayBuffer, paramByteBuffer);
    }
    int j = i + handleDecodingResult(this.decoder.flush(this.cbuf), paramCharArrayBuffer, paramByteBuffer);
    this.cbuf.clear();
    return j;
  }
  
  private int handleDecodingResult(CoderResult paramCoderResult, CharArrayBuffer paramCharArrayBuffer, ByteBuffer paramByteBuffer)
    throws IOException
  {
    if (paramCoderResult.isError()) {
      paramCoderResult.throwException();
    }
    this.cbuf.flip();
    int i = this.cbuf.remaining();
    while (this.cbuf.hasRemaining()) {
      paramCharArrayBuffer.append(this.cbuf.get());
    }
    this.cbuf.compact();
    return i;
  }
  
  private int lineFromLineBuffer(CharArrayBuffer paramCharArrayBuffer)
    throws IOException
  {
    int i = this.linebuffer.length();
    if (i > 0)
    {
      if (this.linebuffer.byteAt(i - 1) == 10) {
        i--;
      }
      if ((i > 0) && (this.linebuffer.byteAt(i - 1) == 13)) {
        i--;
      }
    }
    if (this.decoder == null) {
      paramCharArrayBuffer.append(this.linebuffer, 0, i);
    }
    for (;;)
    {
      this.linebuffer.clear();
      return i;
      i = appendDecoded(paramCharArrayBuffer, ByteBuffer.wrap(this.linebuffer.buffer(), 0, i));
    }
  }
  
  private int lineFromReadBuffer(CharArrayBuffer paramCharArrayBuffer, int paramInt)
    throws IOException
  {
    int i = paramInt;
    int j = this.bufferpos;
    this.bufferpos = (i + 1);
    if ((i > j) && (this.buffer[(i - 1)] == 13)) {
      i--;
    }
    int k = i - j;
    if (this.decoder == null)
    {
      paramCharArrayBuffer.append(this.buffer, j, k);
      return k;
    }
    return appendDecoded(paramCharArrayBuffer, ByteBuffer.wrap(this.buffer, j, k));
  }
  
  private int locateLF()
  {
    for (int i = this.bufferpos; i < this.bufferlen; i++) {
      if (this.buffer[i] == 10) {
        return i;
      }
    }
    return -1;
  }
  
  private int streamRead(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    Asserts.notNull(this.instream, "Input stream");
    return this.instream.read(paramArrayOfByte, paramInt1, paramInt2);
  }
  
  public int available()
  {
    return capacity() - length();
  }
  
  public void bind(InputStream paramInputStream)
  {
    this.instream = paramInputStream;
  }
  
  public int capacity()
  {
    return this.buffer.length;
  }
  
  public void clear()
  {
    this.bufferpos = 0;
    this.bufferlen = 0;
  }
  
  public int fillBuffer()
    throws IOException
  {
    if (this.bufferpos > 0)
    {
      int m = this.bufferlen - this.bufferpos;
      if (m > 0) {
        System.arraycopy(this.buffer, this.bufferpos, this.buffer, 0, m);
      }
      this.bufferpos = 0;
      this.bufferlen = m;
    }
    int i = this.bufferlen;
    int j = this.buffer.length - i;
    int k = streamRead(this.buffer, i, j);
    if (k == -1) {
      return -1;
    }
    this.bufferlen = (i + k);
    this.metrics.incrementBytesTransferred(k);
    return k;
  }
  
  public HttpTransportMetrics getMetrics()
  {
    return this.metrics;
  }
  
  public boolean hasBufferedData()
  {
    return this.bufferpos < this.bufferlen;
  }
  
  public boolean isBound()
  {
    return this.instream != null;
  }
  
  public boolean isDataAvailable(int paramInt)
    throws IOException
  {
    return hasBufferedData();
  }
  
  public int length()
  {
    return this.bufferlen - this.bufferpos;
  }
  
  public int read()
    throws IOException
  {
    while (!hasBufferedData()) {
      if (fillBuffer() == -1) {
        return -1;
      }
    }
    byte[] arrayOfByte = this.buffer;
    int i = this.bufferpos;
    this.bufferpos = (i + 1);
    return 0xFF & arrayOfByte[i];
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte == null) {
      return 0;
    }
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    if (paramArrayOfByte == null) {
      return 0;
    }
    if (hasBufferedData())
    {
      int k = Math.min(paramInt2, this.bufferlen - this.bufferpos);
      System.arraycopy(this.buffer, this.bufferpos, paramArrayOfByte, paramInt1, k);
      this.bufferpos = (k + this.bufferpos);
      return k;
    }
    if (paramInt2 > this.minChunkLimit)
    {
      int j = streamRead(paramArrayOfByte, paramInt1, paramInt2);
      if (j > 0) {
        this.metrics.incrementBytesTransferred(j);
      }
      return j;
    }
    while (!hasBufferedData()) {
      if (fillBuffer() == -1) {
        return -1;
      }
    }
    int i = Math.min(paramInt2, this.bufferlen - this.bufferpos);
    System.arraycopy(this.buffer, this.bufferpos, paramArrayOfByte, paramInt1, i);
    this.bufferpos = (i + this.bufferpos);
    return i;
  }
  
  public int readLine(CharArrayBuffer paramCharArrayBuffer)
    throws IOException
  {
    int i = -1;
    Args.notNull(paramCharArrayBuffer, "Char array buffer");
    int j = 0;
    int k = 1;
    if (k != 0)
    {
      m = locateLF();
      if (m != i) {
        if (this.linebuffer.isEmpty()) {
          i = lineFromReadBuffer(paramCharArrayBuffer, m);
        }
      }
    }
    while ((j == i) && (this.linebuffer.isEmpty()))
    {
      int m;
      return i;
      k = 0;
      int i2 = m + 1 - this.bufferpos;
      this.linebuffer.append(this.buffer, this.bufferpos, i2);
      this.bufferpos = (m + 1);
      for (;;)
      {
        int n = this.constraints.getMaxLineLength();
        if ((n <= 0) || (this.linebuffer.length() < n)) {
          break;
        }
        throw new MessageConstraintException("Maximum line length limit exceeded");
        if (hasBufferedData())
        {
          int i1 = this.bufferlen - this.bufferpos;
          this.linebuffer.append(this.buffer, this.bufferpos, i1);
          this.bufferpos = this.bufferlen;
        }
        j = fillBuffer();
        if (j == i) {
          k = 0;
        }
      }
    }
    return lineFromLineBuffer(paramCharArrayBuffer);
  }
  
  public String readLine()
    throws IOException
  {
    CharArrayBuffer localCharArrayBuffer = new CharArrayBuffer(64);
    if (readLine(localCharArrayBuffer) != -1) {
      return localCharArrayBuffer.toString();
    }
    return null;
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.io.SessionInputBufferImpl
 * JD-Core Version:    0.7.0.1
 */