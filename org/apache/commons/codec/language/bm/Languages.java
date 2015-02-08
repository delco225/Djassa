package org.apache.commons.codec.language.bm;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class Languages
{
  public static final String ANY = "any";
  public static final LanguageSet ANY_LANGUAGE = new LanguageSet()
  {
    public boolean contains(String paramAnonymousString)
    {
      return true;
    }
    
    public String getAny()
    {
      throw new NoSuchElementException("Can't fetch any language from the any language set.");
    }
    
    public boolean isEmpty()
    {
      return false;
    }
    
    public boolean isSingleton()
    {
      return false;
    }
    
    public Languages.LanguageSet restrictTo(Languages.LanguageSet paramAnonymousLanguageSet)
    {
      return paramAnonymousLanguageSet;
    }
    
    public String toString()
    {
      return "ANY_LANGUAGE";
    }
  };
  private static final Map<NameType, Languages> LANGUAGES = new EnumMap(NameType.class);
  public static final LanguageSet NO_LANGUAGES;
  private final Set<String> languages;
  
  static
  {
    for (NameType localNameType : NameType.values()) {
      LANGUAGES.put(localNameType, getInstance(langResourceName(localNameType)));
    }
    NO_LANGUAGES = new LanguageSet()
    {
      public boolean contains(String paramAnonymousString)
      {
        return false;
      }
      
      public String getAny()
      {
        throw new NoSuchElementException("Can't fetch any language from the empty language set.");
      }
      
      public boolean isEmpty()
      {
        return true;
      }
      
      public boolean isSingleton()
      {
        return false;
      }
      
      public Languages.LanguageSet restrictTo(Languages.LanguageSet paramAnonymousLanguageSet)
      {
        return this;
      }
      
      public String toString()
      {
        return "NO_LANGUAGES";
      }
    };
  }
  
  private Languages(Set<String> paramSet)
  {
    this.languages = paramSet;
  }
  
  public static Languages getInstance(String paramString)
  {
    HashSet localHashSet = new HashSet();
    InputStream localInputStream = Languages.class.getClassLoader().getResourceAsStream(paramString);
    if (localInputStream == null) {
      throw new IllegalArgumentException("Unable to resolve required resource: " + paramString);
    }
    Scanner localScanner = new Scanner(localInputStream, "UTF-8");
    int i = 0;
    while (localScanner.hasNextLine())
    {
      String str = localScanner.nextLine().trim();
      if (i != 0)
      {
        if (str.endsWith("*/")) {
          i = 0;
        }
      }
      else if (str.startsWith("/*")) {
        i = 1;
      } else if (str.length() > 0) {
        localHashSet.add(str);
      }
    }
    return new Languages(Collections.unmodifiableSet(localHashSet));
  }
  
  public static Languages getInstance(NameType paramNameType)
  {
    return (Languages)LANGUAGES.get(paramNameType);
  }
  
  private static String langResourceName(NameType paramNameType)
  {
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramNameType.getName();
    return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", arrayOfObject);
  }
  
  public Set<String> getLanguages()
  {
    return this.languages;
  }
  
  public static abstract class LanguageSet
  {
    public static LanguageSet from(Set<String> paramSet)
    {
      if (paramSet.isEmpty()) {
        return Languages.NO_LANGUAGES;
      }
      return new Languages.SomeLanguages(paramSet, null);
    }
    
    public abstract boolean contains(String paramString);
    
    public abstract String getAny();
    
    public abstract boolean isEmpty();
    
    public abstract boolean isSingleton();
    
    public abstract LanguageSet restrictTo(LanguageSet paramLanguageSet);
  }
  
  public static final class SomeLanguages
    extends Languages.LanguageSet
  {
    private final Set<String> languages;
    
    private SomeLanguages(Set<String> paramSet)
    {
      this.languages = Collections.unmodifiableSet(paramSet);
    }
    
    public boolean contains(String paramString)
    {
      return this.languages.contains(paramString);
    }
    
    public String getAny()
    {
      return (String)this.languages.iterator().next();
    }
    
    public Set<String> getLanguages()
    {
      return this.languages;
    }
    
    public boolean isEmpty()
    {
      return this.languages.isEmpty();
    }
    
    public boolean isSingleton()
    {
      return this.languages.size() == 1;
    }
    
    public Languages.LanguageSet restrictTo(Languages.LanguageSet paramLanguageSet)
    {
      if (paramLanguageSet == Languages.NO_LANGUAGES) {
        this = paramLanguageSet;
      }
      SomeLanguages localSomeLanguages;
      do
      {
        do
        {
          return this;
        } while (paramLanguageSet == Languages.ANY_LANGUAGE);
        localSomeLanguages = (SomeLanguages)paramLanguageSet;
      } while (localSomeLanguages.languages.containsAll(this.languages));
      HashSet localHashSet = new HashSet(this.languages);
      localHashSet.retainAll(localSomeLanguages.languages);
      return from(localHashSet);
    }
    
    public String toString()
    {
      return "Languages(" + this.languages.toString() + ")";
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.bm.Languages
 * JD-Core Version:    0.7.0.1
 */