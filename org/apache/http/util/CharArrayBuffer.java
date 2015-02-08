package org.apache.http.util;

import java.io.Serializable;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.protocol.HTTP;

@NotThreadSafe
public final class CharArrayBuffer
  implements Serializable
{
  private static final long serialVersionUID = -6208952725094867135L;
  private char[] buffer;
  private int len;
  
  public CharArrayBuffer(int paramInt)
  {
    Args.notNegative(paramInt, "Buffer capacity");
    this.buffer = new char[paramInt];
  }
  
  private void expand(int paramInt)
  {
    char[] arrayOfChar = new char[Math.max(this.buffer.length << 1, paramInt)];
    System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.len);
    this.buffer = arrayOfChar;
  }
  
  public void append(char paramChar)
  {
    int i = 1 + this.len;
    if (i > this.buffer.length) {
      expand(i);
    }
    this.buffer[this.len] = paramChar;
    this.len = i;
  }
  
  public void append(Object paramObject)
  {
    append(String.valueOf(paramObject));
  }
  
  public void append(String paramString)
  {
    if (paramString != null) {}
    for (String str = paramString;; str = "null")
    {
      int i = str.length();
      int j = i + this.len;
      if (j > this.buffer.length) {
        expand(j);
      }
      str.getChars(0, i, this.buffer, this.len);
      this.len = j;
      return;
    }
  }
  
  public void append(ByteArrayBuffer paramByteArrayBuffer, int paramInt1, int paramInt2)
  {
    if (paramByteArrayBuffer == null) {
      return;
    }
    append(paramByteArrayBuffer.buffer(), paramInt1, paramInt2);
  }
  
  public void append(CharArrayBuffer paramCharArrayBuffer)
  {
    if (paramCharArrayBuffer == null) {
      return;
    }
    append(paramCharArrayBuffer.buffer, 0, paramCharArrayBuffer.len);
  }
  
  public void append(CharArrayBuffer paramCharArrayBuffer, int paramInt1, int paramInt2)
  {
    if (paramCharArrayBuffer == null) {
      return;
    }
    append(paramCharArrayBuffer.buffer, paramInt1, paramInt2);
  }
  
  public void append(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if (paramArrayOfByte == null) {}
    do
    {
      return;
      if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
        throw new IndexOutOfBoundsException("off: " + paramInt1 + " len: " + paramInt2 + " b.length: " + paramArrayOfByte.length);
      }
    } while (paramInt2 == 0);
    int i = this.len;
    int j = i + paramInt2;
    if (j > this.buffer.length) {
      expand(j);
    }
    int k = paramInt1;
    for (int m = i; m < j; m++)
    {
      this.buffer[m] = ((char)(0xFF & paramArrayOfByte[k]));
      k++;
    }
    this.len = j;
  }
  
  public void append(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (paramArrayOfChar == null) {}
    do
    {
      return;
      if ((paramInt1 < 0) || (paramInt1 > paramArrayOfChar.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length)) {
        throw new IndexOutOfBoundsException("off: " + paramInt1 + " len: " + paramInt2 + " b.length: " + paramArrayOfChar.length);
      }
    } while (paramInt2 == 0);
    int i = paramInt2 + this.len;
    if (i > this.buffer.length) {
      expand(i);
    }
    System.arraycopy(paramArrayOfChar, paramInt1, this.buffer, this.len, paramInt2);
    this.len = i;
  }
  
  public char[] buffer()
  {
    return this.buffer;
  }
  
  public int capacity()
  {
    return this.buffer.length;
  }
  
  public char charAt(int paramInt)
  {
    return this.buffer[paramInt];
  }
  
  public void clear()
  {
    this.len = 0;
  }
  
  public void ensureCapacity(int paramInt)
  {
    if (paramInt <= 0) {}
    while (paramInt <= this.buffer.length - this.len) {
      return;
    }
    expand(paramInt + this.len);
  }
  
  public int indexOf(int paramInt)
  {
    return indexOf(paramInt, 0, this.len);
  }
  
  public int indexOf(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = paramInt2;
    if (i < 0) {
      i = 0;
    }
    int j = paramInt3;
    if (j > this.len) {
      j = this.len;
    }
    if (i > j)
    {
      k = -1;
      return k;
    }
    for (int k = i;; k++)
    {
      if (k >= j) {
        break label70;
      }
      if (this.buffer[k] == paramInt1) {
        break;
      }
    }
    label70:
    return -1;
  }
  
  public boolean isEmpty()
  {
    return this.len == 0;
  }
  
  public boolean isFull()
  {
    return this.len == this.buffer.length;
  }
  
  public int length()
  {
    return this.len;
  }
  
  public void setLength(int paramInt)
  {
    if ((paramInt < 0) || (paramInt > this.buffer.length)) {
      throw new IndexOutOfBoundsException("len: " + paramInt + " < 0 or > buffer len: " + this.buffer.length);
    }
    this.len = paramInt;
  }
  
  public String substring(int paramInt1, int paramInt2)
  {
    return new String(this.buffer, paramInt1, paramInt2 - paramInt1);
  }
  
  public String substringTrimmed(int paramInt1, int paramInt2)
  {
    int i = paramInt1;
    int j = paramInt2;
    if (i < 0) {
      throw new IndexOutOfBoundsException("Negative beginIndex: " + i);
    }
    if (j > this.len) {
      throw new IndexOutOfBoundsException("endIndex: " + j + " > length: " + this.len);
    }
    if (i > j) {
      throw new IndexOutOfBoundsException("beginIndex: " + i + " > endIndex: " + j);
    }
    while ((i < j) && (HTTP.isWhitespace(this.buffer[i]))) {
      i++;
    }
    while ((j > i) && (HTTP.isWhitespace(this.buffer[(j - 1)]))) {
      j--;
    }
    return new String(this.buffer, i, j - i);
  }
  
  public char[] toCharArray()
  {
    char[] arrayOfChar = new char[this.len];
    if (this.len > 0) {
      System.arraycopy(this.buffer, 0, arrayOfChar, 0, this.len);
    }
    return arrayOfChar;
  }
  
  public String toString()
  {
    return new String(this.buffer, 0, this.len);
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.util.CharArrayBuffer
 * JD-Core Version:    0.7.0.1
 */