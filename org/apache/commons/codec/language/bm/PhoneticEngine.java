package org.apache.commons.codec.language.bm;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class PhoneticEngine
{
  private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap(NameType.class);
  private final boolean concat;
  private final Lang lang;
  private final NameType nameType;
  private final RuleType ruleType;
  
  static
  {
    NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "bar", "ben", "da", "de", "van", "von" }))));
    NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von" }))));
    NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[] { "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von" }))));
  }
  
  public PhoneticEngine(NameType paramNameType, RuleType paramRuleType, boolean paramBoolean)
  {
    if (paramRuleType == RuleType.RULES) {
      throw new IllegalArgumentException("ruleType must not be " + RuleType.RULES);
    }
    this.nameType = paramNameType;
    this.ruleType = paramRuleType;
    this.concat = paramBoolean;
    this.lang = Lang.instance(paramNameType);
  }
  
  private PhonemeBuilder applyFinalRules(PhonemeBuilder paramPhonemeBuilder, List<Rule> paramList)
  {
    if (paramList == null) {
      throw new NullPointerException("finalRules can not be null");
    }
    if (paramList.isEmpty()) {
      return paramPhonemeBuilder;
    }
    TreeSet localTreeSet = new TreeSet(Rule.Phoneme.COMPARATOR);
    Iterator localIterator = paramPhonemeBuilder.getPhonemes().iterator();
    while (localIterator.hasNext())
    {
      Rule.Phoneme localPhoneme = (Rule.Phoneme)localIterator.next();
      PhonemeBuilder localPhonemeBuilder = PhonemeBuilder.empty(localPhoneme.getLanguages());
      CharSequence localCharSequence = cacheSubSequence(localPhoneme.getPhonemeText());
      RulesApplication localRulesApplication;
      for (int i = 0; i < localCharSequence.length(); i = localRulesApplication.getI())
      {
        localRulesApplication = new RulesApplication(paramList, localCharSequence, localPhonemeBuilder, i).invoke();
        boolean bool = localRulesApplication.isFound();
        localPhonemeBuilder = localRulesApplication.getPhonemeBuilder();
        if (!bool) {
          localPhonemeBuilder = localPhonemeBuilder.append(localCharSequence.subSequence(i, i + 1));
        }
      }
      localTreeSet.addAll(localPhonemeBuilder.getPhonemes());
    }
    return new PhonemeBuilder(localTreeSet, null);
  }
  
  private static CharSequence cacheSubSequence(CharSequence paramCharSequence)
  {
    new CharSequence()
    {
      public char charAt(int paramAnonymousInt)
      {
        return this.val$cached.charAt(paramAnonymousInt);
      }
      
      public int length()
      {
        return this.val$cached.length();
      }
      
      public CharSequence subSequence(int paramAnonymousInt1, int paramAnonymousInt2)
      {
        Object localObject;
        if (paramAnonymousInt1 == paramAnonymousInt2) {
          localObject = "";
        }
        do
        {
          return localObject;
          localObject = this.val$cache[paramAnonymousInt1][(paramAnonymousInt2 - 1)];
        } while (localObject != null);
        CharSequence localCharSequence = this.val$cached.subSequence(paramAnonymousInt1, paramAnonymousInt2);
        this.val$cache[paramAnonymousInt1][(paramAnonymousInt2 - 1)] = localCharSequence;
        return localCharSequence;
      }
    };
  }
  
  private static String join(Iterable<String> paramIterable, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramIterable.iterator();
    if (localIterator.hasNext()) {
      localStringBuilder.append((String)localIterator.next());
    }
    while (localIterator.hasNext()) {
      localStringBuilder.append(paramString).append((String)localIterator.next());
    }
    return localStringBuilder.toString();
  }
  
  public String encode(String paramString)
  {
    return encode(paramString, this.lang.guessLanguages(paramString));
  }
  
  public String encode(String paramString, Languages.LanguageSet paramLanguageSet)
  {
    List localList1 = Rule.getInstance(this.nameType, RuleType.RULES, paramLanguageSet);
    List localList2 = Rule.getInstance(this.nameType, this.ruleType, "common");
    List localList3 = Rule.getInstance(this.nameType, this.ruleType, paramLanguageSet);
    String str1 = paramString.toLowerCase(Locale.ENGLISH).replace('-', ' ').trim();
    if (this.nameType == NameType.GENERIC)
    {
      if ((str1.length() >= 2) && (str1.substring(0, 2).equals("d'")))
      {
        String str7 = str1.substring(2);
        String str8 = "d" + str7;
        return "(" + encode(str7) + ")-(" + encode(str8) + ")";
      }
      Iterator localIterator3 = ((Set)NAME_PREFIXES.get(this.nameType)).iterator();
      while (localIterator3.hasNext())
      {
        String str4 = (String)localIterator3.next();
        if (str1.startsWith(str4 + " "))
        {
          String str5 = str1.substring(1 + str4.length());
          String str6 = str4 + str5;
          return "(" + encode(str5) + ")-(" + encode(str6) + ")";
        }
      }
    }
    List localList4 = Arrays.asList(str1.split("\\s+"));
    ArrayList localArrayList = new ArrayList();
    switch (2.$SwitchMap$org$apache$commons$codec$language$bm$NameType[this.nameType.ordinal()])
    {
    default: 
      throw new IllegalStateException("Unreachable case: " + this.nameType);
    case 1: 
      Iterator localIterator2 = localList4.iterator();
      while (localIterator2.hasNext())
      {
        String[] arrayOfString = ((String)localIterator2.next()).split("'");
        localArrayList.add(arrayOfString[(-1 + arrayOfString.length)]);
      }
      localArrayList.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
      if (!this.concat) {
        break;
      }
    }
    PhonemeBuilder localPhonemeBuilder;
    for (String str3 = join(localArrayList, " ");; str3 = (String)localList4.iterator().next())
    {
      localPhonemeBuilder = PhonemeBuilder.empty(paramLanguageSet);
      CharSequence localCharSequence = cacheSubSequence(str3);
      int i = 0;
      while (i < localCharSequence.length())
      {
        RulesApplication localRulesApplication1 = new RulesApplication(localList1, localCharSequence, localPhonemeBuilder, i);
        RulesApplication localRulesApplication2 = localRulesApplication1.invoke();
        i = localRulesApplication2.getI();
        localPhonemeBuilder = localRulesApplication2.getPhonemeBuilder();
      }
      localArrayList.addAll(localList4);
      localArrayList.removeAll((Collection)NAME_PREFIXES.get(this.nameType));
      break;
      localArrayList.addAll(localList4);
      break;
      if (localArrayList.size() != 1) {
        break label663;
      }
    }
    label663:
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator1 = localArrayList.iterator();
    while (localIterator1.hasNext())
    {
      String str2 = (String)localIterator1.next();
      localStringBuilder.append("-").append(encode(str2));
    }
    return localStringBuilder.substring(1);
    return applyFinalRules(applyFinalRules(localPhonemeBuilder, localList2), localList3).makeString();
  }
  
  public Lang getLang()
  {
    return this.lang;
  }
  
  public NameType getNameType()
  {
    return this.nameType;
  }
  
  public RuleType getRuleType()
  {
    return this.ruleType;
  }
  
  public boolean isConcat()
  {
    return this.concat;
  }
  
  static final class PhonemeBuilder
  {
    private final Set<Rule.Phoneme> phonemes;
    
    private PhonemeBuilder(Set<Rule.Phoneme> paramSet)
    {
      this.phonemes = paramSet;
    }
    
    public static PhonemeBuilder empty(Languages.LanguageSet paramLanguageSet)
    {
      return new PhonemeBuilder(Collections.singleton(new Rule.Phoneme("", paramLanguageSet)));
    }
    
    public PhonemeBuilder append(CharSequence paramCharSequence)
    {
      HashSet localHashSet = new HashSet();
      Iterator localIterator = this.phonemes.iterator();
      while (localIterator.hasNext()) {
        localHashSet.add(((Rule.Phoneme)localIterator.next()).append(paramCharSequence));
      }
      return new PhonemeBuilder(localHashSet);
    }
    
    public PhonemeBuilder apply(Rule.PhonemeExpr paramPhonemeExpr)
    {
      HashSet localHashSet = new HashSet();
      Iterator localIterator1 = this.phonemes.iterator();
      while (localIterator1.hasNext())
      {
        Rule.Phoneme localPhoneme1 = (Rule.Phoneme)localIterator1.next();
        Iterator localIterator2 = paramPhonemeExpr.getPhonemes().iterator();
        while (localIterator2.hasNext())
        {
          Rule.Phoneme localPhoneme2 = localPhoneme1.join((Rule.Phoneme)localIterator2.next());
          if (!localPhoneme2.getLanguages().isEmpty()) {
            localHashSet.add(localPhoneme2);
          }
        }
      }
      return new PhonemeBuilder(localHashSet);
    }
    
    public Set<Rule.Phoneme> getPhonemes()
    {
      return this.phonemes;
    }
    
    public String makeString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      Iterator localIterator = this.phonemes.iterator();
      while (localIterator.hasNext())
      {
        Rule.Phoneme localPhoneme = (Rule.Phoneme)localIterator.next();
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append("|");
        }
        localStringBuilder.append(localPhoneme.getPhonemeText());
      }
      return localStringBuilder.toString();
    }
  }
  
  private static final class RulesApplication
  {
    private final List<Rule> finalRules;
    private boolean found;
    private int i;
    private final CharSequence input;
    private PhoneticEngine.PhonemeBuilder phonemeBuilder;
    
    public RulesApplication(List<Rule> paramList, CharSequence paramCharSequence, PhoneticEngine.PhonemeBuilder paramPhonemeBuilder, int paramInt)
    {
      if (paramList == null) {
        throw new NullPointerException("The finalRules argument must not be null");
      }
      this.finalRules = paramList;
      this.phonemeBuilder = paramPhonemeBuilder;
      this.input = paramCharSequence;
      this.i = paramInt;
    }
    
    public int getI()
    {
      return this.i;
    }
    
    public PhoneticEngine.PhonemeBuilder getPhonemeBuilder()
    {
      return this.phonemeBuilder;
    }
    
    public RulesApplication invoke()
    {
      this.found = false;
      int j = 0;
      Iterator localIterator = this.finalRules.iterator();
      while (localIterator.hasNext())
      {
        Rule localRule = (Rule)localIterator.next();
        j = localRule.getPattern().length();
        if (localRule.patternAndContextMatches(this.input, this.i))
        {
          this.phonemeBuilder = this.phonemeBuilder.apply(localRule.getPhoneme());
          this.found = true;
        }
      }
      if (!this.found) {
        j = 1;
      }
      this.i = (j + this.i);
      return this;
    }
    
    public boolean isFound()
    {
      return this.found;
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.codec.language.bm.PhoneticEngine
 * JD-Core Version:    0.7.0.1
 */