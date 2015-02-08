package org.apache.http.message;

import java.util.NoSuchElementException;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.ParseException;
import org.apache.http.TokenIterator;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.Args;

@NotThreadSafe
public class BasicTokenIterator
  implements TokenIterator
{
  public static final String HTTP_SEPARATORS = " ,;=()<>@:\\\"/[]?{}\t";
  protected String currentHeader;
  protected String currentToken;
  protected final HeaderIterator headerIt;
  protected int searchPos;
  
  public BasicTokenIterator(HeaderIterator paramHeaderIterator)
  {
    this.headerIt = ((HeaderIterator)Args.notNull(paramHeaderIterator, "Header iterator"));
    this.searchPos = findNext(-1);
  }
  
  protected String createToken(String paramString, int paramInt1, int paramInt2)
  {
    return paramString.substring(paramInt1, paramInt2);
  }
  
  protected int findNext(int paramInt)
    throws ParseException
  {
    if (paramInt < 0)
    {
      if (!this.headerIt.hasNext()) {
        return -1;
      }
      this.currentHeader = this.headerIt.nextHeader().getValue();
    }
    int j;
    for (int i = 0;; i = findTokenSeparator(paramInt))
    {
      j = findTokenStart(i);
      if (j >= 0) {
        break;
      }
      this.currentToken = null;
      return -1;
    }
    int k = findTokenEnd(j);
    this.currentToken = createToken(this.currentHeader, j, k);
    return k;
  }
  
  protected int findTokenEnd(int paramInt)
  {
    Args.notNegative(paramInt, "Search position");
    int i = this.currentHeader.length();
    for (int j = paramInt + 1; (j < i) && (isTokenChar(this.currentHeader.charAt(j))); j++) {}
    return j;
  }
  
  protected int findTokenSeparator(int paramInt)
  {
    int i = Args.notNegative(paramInt, "Search position");
    int j = 0;
    int k = this.currentHeader.length();
    while ((j == 0) && (i < k))
    {
      char c = this.currentHeader.charAt(i);
      if (isTokenSeparator(c))
      {
        j = 1;
      }
      else if (isWhitespace(c))
      {
        i++;
      }
      else
      {
        if (isTokenChar(c)) {
          throw new ParseException("Tokens without separator (pos " + i + "): " + this.currentHeader);
        }
        throw new ParseException("Invalid character after token (pos " + i + "): " + this.currentHeader);
      }
    }
    return i;
  }
  
  protected int findTokenStart(int paramInt)
  {
    int i = Args.notNegative(paramInt, "Search position");
    int j = 0;
    while ((j == 0) && (this.currentHeader != null))
    {
      int k = this.currentHeader.length();
      while ((j == 0) && (i < k))
      {
        char c = this.currentHeader.charAt(i);
        if ((isTokenSeparator(c)) || (isWhitespace(c))) {
          i++;
        } else if (isTokenChar(this.currentHeader.charAt(i))) {
          j = 1;
        } else {
          throw new ParseException("Invalid character before token (pos " + i + "): " + this.currentHeader);
        }
      }
      if (j == 0) {
        if (this.headerIt.hasNext())
        {
          this.currentHeader = this.headerIt.nextHeader().getValue();
          i = 0;
        }
        else
        {
          this.currentHeader = null;
        }
      }
    }
    if (j != 0) {
      return i;
    }
    return -1;
  }
  
  public boolean hasNext()
  {
    return this.currentToken != null;
  }
  
  protected boolean isHttpSeparator(char paramChar)
  {
    return " ,;=()<>@:\\\"/[]?{}\t".indexOf(paramChar) >= 0;
  }
  
  protected boolean isTokenChar(char paramChar)
  {
    if (Character.isLetterOrDigit(paramChar)) {}
    do
    {
      return true;
      if (Character.isISOControl(paramChar)) {
        return false;
      }
    } while (!isHttpSeparator(paramChar));
    return false;
  }
  
  protected boolean isTokenSeparator(char paramChar)
  {
    return paramChar == ',';
  }
  
  protected boolean isWhitespace(char paramChar)
  {
    return (paramChar == '\t') || (Character.isSpaceChar(paramChar));
  }
  
  public final Object next()
    throws NoSuchElementException, ParseException
  {
    return nextToken();
  }
  
  public String nextToken()
    throws NoSuchElementException, ParseException
  {
    if (this.currentToken == null) {
      throw new NoSuchElementException("Iteration already finished.");
    }
    String str = this.currentToken;
    this.searchPos = findNext(this.searchPos);
    return str;
  }
  
  public final void remove()
    throws UnsupportedOperationException
  {
    throw new UnsupportedOperationException("Removing tokens is not supported.");
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.message.BasicTokenIterator
 * JD-Core Version:    0.7.0.1
 */