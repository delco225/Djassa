package org.apache.http.impl.auth;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Locale;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.util.EncodingUtils;

@NotThreadSafe
final class NTLMEngineImpl
  implements NTLMEngine
{
  static final String DEFAULT_CHARSET = "ASCII";
  protected static final int FLAG_DOMAIN_PRESENT = 4096;
  protected static final int FLAG_REQUEST_128BIT_KEY_EXCH = 536870912;
  protected static final int FLAG_REQUEST_56BIT_ENCRYPTION = -2147483648;
  protected static final int FLAG_REQUEST_ALWAYS_SIGN = 32768;
  protected static final int FLAG_REQUEST_EXPLICIT_KEY_EXCH = 1073741824;
  protected static final int FLAG_REQUEST_LAN_MANAGER_KEY = 128;
  protected static final int FLAG_REQUEST_NTLM2_SESSION = 524288;
  protected static final int FLAG_REQUEST_NTLMv1 = 512;
  protected static final int FLAG_REQUEST_SEAL = 32;
  protected static final int FLAG_REQUEST_SIGN = 16;
  protected static final int FLAG_REQUEST_TARGET = 4;
  protected static final int FLAG_REQUEST_UNICODE_ENCODING = 1;
  protected static final int FLAG_REQUEST_VERSION = 33554432;
  protected static final int FLAG_TARGETINFO_PRESENT = 8388608;
  protected static final int FLAG_WORKSTATION_PRESENT = 8192;
  private static final SecureRandom RND_GEN;
  private static final byte[] SIGNATURE;
  private String credentialCharset = "ASCII";
  
  static
  {
    try
    {
      SecureRandom localSecureRandom2 = SecureRandom.getInstance("SHA1PRNG");
      localSecureRandom1 = localSecureRandom2;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        byte[] arrayOfByte;
        SecureRandom localSecureRandom1 = null;
      }
    }
    RND_GEN = localSecureRandom1;
    arrayOfByte = EncodingUtils.getBytes("NTLMSSP", "ASCII");
    SIGNATURE = new byte[1 + arrayOfByte.length];
    System.arraycopy(arrayOfByte, 0, SIGNATURE, 0, arrayOfByte.length);
    SIGNATURE[arrayOfByte.length] = 0;
  }
  
  static int F(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt3 & (paramInt1 ^ 0xFFFFFFFF);
  }
  
  static int G(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt1 & paramInt2 | paramInt1 & paramInt3 | paramInt2 & paramInt3;
  }
  
  static int H(int paramInt1, int paramInt2, int paramInt3)
  {
    return paramInt3 ^ paramInt1 ^ paramInt2;
  }
  
  static byte[] RC4(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws NTLMEngineException
  {
    try
    {
      Cipher localCipher = Cipher.getInstance("RC4");
      localCipher.init(1, new SecretKeySpec(paramArrayOfByte2, "RC4"));
      byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte1);
      return arrayOfByte;
    }
    catch (Exception localException)
    {
      throw new NTLMEngineException(localException.getMessage(), localException);
    }
  }
  
  private static String convertDomain(String paramString)
  {
    return stripDotSuffix(paramString);
  }
  
  private static String convertHost(String paramString)
  {
    return stripDotSuffix(paramString);
  }
  
  private static byte[] createBlob(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
  {
    byte[] arrayOfByte1 = { 1, 1, 0, 0 };
    byte[] arrayOfByte2 = { 0, 0, 0, 0 };
    byte[] arrayOfByte3 = { 0, 0, 0, 0 };
    byte[] arrayOfByte4 = { 0, 0, 0, 0 };
    byte[] arrayOfByte5 = new byte[8 + (arrayOfByte1.length + arrayOfByte2.length + paramArrayOfByte3.length) + arrayOfByte3.length + paramArrayOfByte2.length + arrayOfByte4.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte5, 0, arrayOfByte1.length);
    int i = 0 + arrayOfByte1.length;
    System.arraycopy(arrayOfByte2, 0, arrayOfByte5, i, arrayOfByte2.length);
    int j = i + arrayOfByte2.length;
    System.arraycopy(paramArrayOfByte3, 0, arrayOfByte5, j, paramArrayOfByte3.length);
    int k = j + paramArrayOfByte3.length;
    System.arraycopy(paramArrayOfByte1, 0, arrayOfByte5, k, 8);
    int m = k + 8;
    System.arraycopy(arrayOfByte3, 0, arrayOfByte5, m, arrayOfByte3.length);
    int n = m + arrayOfByte3.length;
    System.arraycopy(paramArrayOfByte2, 0, arrayOfByte5, n, paramArrayOfByte2.length);
    int i1 = n + paramArrayOfByte2.length;
    System.arraycopy(arrayOfByte4, 0, arrayOfByte5, i1, arrayOfByte4.length);
    (i1 + arrayOfByte4.length);
    return arrayOfByte5;
  }
  
  private static Key createDESKey(byte[] paramArrayOfByte, int paramInt)
  {
    byte[] arrayOfByte1 = new byte[7];
    System.arraycopy(paramArrayOfByte, paramInt, arrayOfByte1, 0, 7);
    byte[] arrayOfByte2 = new byte[8];
    arrayOfByte2[0] = arrayOfByte1[0];
    arrayOfByte2[1] = ((byte)(arrayOfByte1[0] << 7 | (0xFF & arrayOfByte1[1]) >>> 1));
    arrayOfByte2[2] = ((byte)(arrayOfByte1[1] << 6 | (0xFF & arrayOfByte1[2]) >>> 2));
    arrayOfByte2[3] = ((byte)(arrayOfByte1[2] << 5 | (0xFF & arrayOfByte1[3]) >>> 3));
    arrayOfByte2[4] = ((byte)(arrayOfByte1[3] << 4 | (0xFF & arrayOfByte1[4]) >>> 4));
    arrayOfByte2[5] = ((byte)(arrayOfByte1[4] << 3 | (0xFF & arrayOfByte1[5]) >>> 5));
    arrayOfByte2[6] = ((byte)(arrayOfByte1[5] << 2 | (0xFF & arrayOfByte1[6]) >>> 6));
    arrayOfByte2[7] = ((byte)(arrayOfByte1[6] << 1));
    oddParity(arrayOfByte2);
    return new SecretKeySpec(arrayOfByte2, "DES");
  }
  
  static byte[] hmacMD5(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws NTLMEngineException
  {
    HMACMD5 localHMACMD5 = new HMACMD5(paramArrayOfByte2);
    localHMACMD5.update(paramArrayOfByte1);
    return localHMACMD5.getOutput();
  }
  
  private static byte[] lmHash(String paramString)
    throws NTLMEngineException
  {
    try
    {
      byte[] arrayOfByte1 = paramString.toUpperCase(Locale.US).getBytes("US-ASCII");
      int i = Math.min(arrayOfByte1.length, 14);
      byte[] arrayOfByte2 = new byte[14];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, i);
      Key localKey1 = createDESKey(arrayOfByte2, 0);
      Key localKey2 = createDESKey(arrayOfByte2, 7);
      byte[] arrayOfByte3 = "KGS!@#$%".getBytes("US-ASCII");
      Cipher localCipher = Cipher.getInstance("DES/ECB/NoPadding");
      localCipher.init(1, localKey1);
      byte[] arrayOfByte4 = localCipher.doFinal(arrayOfByte3);
      localCipher.init(1, localKey2);
      byte[] arrayOfByte5 = localCipher.doFinal(arrayOfByte3);
      byte[] arrayOfByte6 = new byte[16];
      System.arraycopy(arrayOfByte4, 0, arrayOfByte6, 0, 8);
      System.arraycopy(arrayOfByte5, 0, arrayOfByte6, 8, 8);
      return arrayOfByte6;
    }
    catch (Exception localException)
    {
      throw new NTLMEngineException(localException.getMessage(), localException);
    }
  }
  
  private static byte[] lmResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
    throws NTLMEngineException
  {
    try
    {
      byte[] arrayOfByte1 = new byte[21];
      System.arraycopy(paramArrayOfByte1, 0, arrayOfByte1, 0, 16);
      Key localKey1 = createDESKey(arrayOfByte1, 0);
      Key localKey2 = createDESKey(arrayOfByte1, 7);
      Key localKey3 = createDESKey(arrayOfByte1, 14);
      Cipher localCipher = Cipher.getInstance("DES/ECB/NoPadding");
      localCipher.init(1, localKey1);
      byte[] arrayOfByte2 = localCipher.doFinal(paramArrayOfByte2);
      localCipher.init(1, localKey2);
      byte[] arrayOfByte3 = localCipher.doFinal(paramArrayOfByte2);
      localCipher.init(1, localKey3);
      byte[] arrayOfByte4 = localCipher.doFinal(paramArrayOfByte2);
      byte[] arrayOfByte5 = new byte[24];
      System.arraycopy(arrayOfByte2, 0, arrayOfByte5, 0, 8);
      System.arraycopy(arrayOfByte3, 0, arrayOfByte5, 8, 8);
      System.arraycopy(arrayOfByte4, 0, arrayOfByte5, 16, 8);
      return arrayOfByte5;
    }
    catch (Exception localException)
    {
      throw new NTLMEngineException(localException.getMessage(), localException);
    }
  }
  
  private static byte[] lmv2Hash(String paramString1, String paramString2, byte[] paramArrayOfByte)
    throws NTLMEngineException
  {
    try
    {
      HMACMD5 localHMACMD5 = new HMACMD5(paramArrayOfByte);
      localHMACMD5.update(paramString2.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
      if (paramString1 != null) {
        localHMACMD5.update(paramString1.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
      }
      byte[] arrayOfByte = localHMACMD5.getOutput();
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new NTLMEngineException("Unicode not supported! " + localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
    }
  }
  
  private static byte[] lmv2Response(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    throws NTLMEngineException
  {
    HMACMD5 localHMACMD5 = new HMACMD5(paramArrayOfByte1);
    localHMACMD5.update(paramArrayOfByte2);
    localHMACMD5.update(paramArrayOfByte3);
    byte[] arrayOfByte1 = localHMACMD5.getOutput();
    byte[] arrayOfByte2 = new byte[arrayOfByte1.length + paramArrayOfByte3.length];
    System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, arrayOfByte1.length);
    System.arraycopy(paramArrayOfByte3, 0, arrayOfByte2, arrayOfByte1.length, paramArrayOfByte3.length);
    return arrayOfByte2;
  }
  
  private static byte[] makeRandomChallenge()
    throws NTLMEngineException
  {
    if (RND_GEN == null) {
      throw new NTLMEngineException("Random generator not available");
    }
    byte[] arrayOfByte = new byte[8];
    synchronized (RND_GEN)
    {
      RND_GEN.nextBytes(arrayOfByte);
      return arrayOfByte;
    }
  }
  
  private static byte[] makeSecondaryKey()
    throws NTLMEngineException
  {
    if (RND_GEN == null) {
      throw new NTLMEngineException("Random generator not available");
    }
    byte[] arrayOfByte = new byte[16];
    synchronized (RND_GEN)
    {
      RND_GEN.nextBytes(arrayOfByte);
      return arrayOfByte;
    }
  }
  
  static byte[] ntlm2SessionResponse(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
    throws NTLMEngineException
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte2);
      localMessageDigest.update(paramArrayOfByte3);
      byte[] arrayOfByte1 = localMessageDigest.digest();
      byte[] arrayOfByte2 = new byte[8];
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, 0, 8);
      byte[] arrayOfByte3 = lmResponse(paramArrayOfByte1, arrayOfByte2);
      return arrayOfByte3;
    }
    catch (Exception localException)
    {
      if ((localException instanceof NTLMEngineException)) {
        throw ((NTLMEngineException)localException);
      }
      throw new NTLMEngineException(localException.getMessage(), localException);
    }
  }
  
  private static byte[] ntlmHash(String paramString)
    throws NTLMEngineException
  {
    try
    {
      byte[] arrayOfByte1 = paramString.getBytes("UnicodeLittleUnmarked");
      MD4 localMD4 = new MD4();
      localMD4.update(arrayOfByte1);
      byte[] arrayOfByte2 = localMD4.getOutput();
      return arrayOfByte2;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new NTLMEngineException("Unicode not supported: " + localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
    }
  }
  
  private static byte[] ntlmv2Hash(String paramString1, String paramString2, byte[] paramArrayOfByte)
    throws NTLMEngineException
  {
    try
    {
      HMACMD5 localHMACMD5 = new HMACMD5(paramArrayOfByte);
      localHMACMD5.update(paramString2.toUpperCase(Locale.US).getBytes("UnicodeLittleUnmarked"));
      if (paramString1 != null) {
        localHMACMD5.update(paramString1.getBytes("UnicodeLittleUnmarked"));
      }
      byte[] arrayOfByte = localHMACMD5.getOutput();
      return arrayOfByte;
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      throw new NTLMEngineException("Unicode not supported! " + localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
    }
  }
  
  private static void oddParity(byte[] paramArrayOfByte)
  {
    int i = 0;
    if (i < paramArrayOfByte.length)
    {
      int j = paramArrayOfByte[i];
      int k;
      if ((0x1 & (j >>> 7 ^ j >>> 6 ^ j >>> 5 ^ j >>> 4 ^ j >>> 3 ^ j >>> 2 ^ j >>> 1)) == 0)
      {
        k = 1;
        label48:
        if (k == 0) {
          break label72;
        }
        paramArrayOfByte[i] = ((byte)(0x1 | paramArrayOfByte[i]));
      }
      for (;;)
      {
        i++;
        break;
        k = 0;
        break label48;
        label72:
        paramArrayOfByte[i] = ((byte)(0xFFFFFFFE & paramArrayOfByte[i]));
      }
    }
  }
  
  private static byte[] readSecurityBuffer(byte[] paramArrayOfByte, int paramInt)
    throws NTLMEngineException
  {
    int i = readUShort(paramArrayOfByte, paramInt);
    int j = readULong(paramArrayOfByte, paramInt + 4);
    if (paramArrayOfByte.length < j + i) {
      throw new NTLMEngineException("NTLM authentication - buffer too small for data item");
    }
    byte[] arrayOfByte = new byte[i];
    System.arraycopy(paramArrayOfByte, j, arrayOfByte, 0, i);
    return arrayOfByte;
  }
  
  private static int readULong(byte[] paramArrayOfByte, int paramInt)
    throws NTLMEngineException
  {
    if (paramArrayOfByte.length < paramInt + 4) {
      throw new NTLMEngineException("NTLM authentication - buffer too small for DWORD");
    }
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8 | (0xFF & paramArrayOfByte[(paramInt + 2)]) << 16 | (0xFF & paramArrayOfByte[(paramInt + 3)]) << 24;
  }
  
  private static int readUShort(byte[] paramArrayOfByte, int paramInt)
    throws NTLMEngineException
  {
    if (paramArrayOfByte.length < paramInt + 2) {
      throw new NTLMEngineException("NTLM authentication - buffer too small for WORD");
    }
    return 0xFF & paramArrayOfByte[paramInt] | (0xFF & paramArrayOfByte[(paramInt + 1)]) << 8;
  }
  
  static int rotintlft(int paramInt1, int paramInt2)
  {
    return paramInt1 << paramInt2 | paramInt1 >>> 32 - paramInt2;
  }
  
  private static String stripDotSuffix(String paramString)
  {
    if (paramString == null) {
      paramString = null;
    }
    int i;
    do
    {
      return paramString;
      i = paramString.indexOf(".");
    } while (i == -1);
    return paramString.substring(0, i);
  }
  
  static void writeULong(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    paramArrayOfByte[paramInt2] = ((byte)(paramInt1 & 0xFF));
    paramArrayOfByte[(paramInt2 + 1)] = ((byte)(0xFF & paramInt1 >> 8));
    paramArrayOfByte[(paramInt2 + 2)] = ((byte)(0xFF & paramInt1 >> 16));
    paramArrayOfByte[(paramInt2 + 3)] = ((byte)(0xFF & paramInt1 >> 24));
  }
  
  public String generateType1Msg(String paramString1, String paramString2)
    throws NTLMEngineException
  {
    return getType1Message(paramString2, paramString1);
  }
  
  public String generateType3Msg(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws NTLMEngineException
  {
    Type2Message localType2Message = new Type2Message(paramString5);
    return getType3Message(paramString1, paramString2, paramString4, paramString3, localType2Message.getChallenge(), localType2Message.getFlags(), localType2Message.getTarget(), localType2Message.getTargetInfo());
  }
  
  String getCredentialCharset()
  {
    return this.credentialCharset;
  }
  
  final String getResponseFor(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws NTLMEngineException
  {
    if ((paramString1 == null) || (paramString1.trim().equals(""))) {
      return getType1Message(paramString4, paramString5);
    }
    Type2Message localType2Message = new Type2Message(paramString1);
    return getType3Message(paramString2, paramString3, paramString4, paramString5, localType2Message.getChallenge(), localType2Message.getFlags(), localType2Message.getTarget(), localType2Message.getTargetInfo());
  }
  
  String getType1Message(String paramString1, String paramString2)
    throws NTLMEngineException
  {
    return new Type1Message(paramString2, paramString1).getResponse();
  }
  
  String getType3Message(String paramString1, String paramString2, String paramString3, String paramString4, byte[] paramArrayOfByte1, int paramInt, String paramString5, byte[] paramArrayOfByte2)
    throws NTLMEngineException
  {
    return new Type3Message(paramString4, paramString3, paramString1, paramString2, paramArrayOfByte1, paramInt, paramString5, paramArrayOfByte2).getResponse();
  }
  
  void setCredentialCharset(String paramString)
  {
    this.credentialCharset = paramString;
  }
  
  protected static class CipherGen
  {
    protected final byte[] challenge;
    protected byte[] clientChallenge;
    protected byte[] clientChallenge2;
    protected final String domain;
    protected byte[] lanManagerSessionKey = null;
    protected byte[] lm2SessionResponse = null;
    protected byte[] lmHash = null;
    protected byte[] lmResponse = null;
    protected byte[] lmUserSessionKey = null;
    protected byte[] lmv2Hash = null;
    protected byte[] lmv2Response = null;
    protected byte[] ntlm2SessionResponse = null;
    protected byte[] ntlm2SessionResponseUserSessionKey = null;
    protected byte[] ntlmHash = null;
    protected byte[] ntlmResponse = null;
    protected byte[] ntlmUserSessionKey = null;
    protected byte[] ntlmv2Blob = null;
    protected byte[] ntlmv2Hash = null;
    protected byte[] ntlmv2Response = null;
    protected byte[] ntlmv2UserSessionKey = null;
    protected final String password;
    protected byte[] secondaryKey;
    protected final String target;
    protected final byte[] targetInformation;
    protected byte[] timestamp;
    protected final String user;
    
    public CipherGen(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte1, String paramString4, byte[] paramArrayOfByte2)
    {
      this(paramString1, paramString2, paramString3, paramArrayOfByte1, paramString4, paramArrayOfByte2, null, null, null, null);
    }
    
    public CipherGen(String paramString1, String paramString2, String paramString3, byte[] paramArrayOfByte1, String paramString4, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, byte[] paramArrayOfByte4, byte[] paramArrayOfByte5, byte[] paramArrayOfByte6)
    {
      this.domain = paramString1;
      this.target = paramString4;
      this.user = paramString2;
      this.password = paramString3;
      this.challenge = paramArrayOfByte1;
      this.targetInformation = paramArrayOfByte2;
      this.clientChallenge = paramArrayOfByte3;
      this.clientChallenge2 = paramArrayOfByte4;
      this.secondaryKey = paramArrayOfByte5;
      this.timestamp = paramArrayOfByte6;
    }
    
    public byte[] getClientChallenge()
      throws NTLMEngineException
    {
      if (this.clientChallenge == null) {
        this.clientChallenge = NTLMEngineImpl.access$000();
      }
      return this.clientChallenge;
    }
    
    public byte[] getClientChallenge2()
      throws NTLMEngineException
    {
      if (this.clientChallenge2 == null) {
        this.clientChallenge2 = NTLMEngineImpl.access$000();
      }
      return this.clientChallenge2;
    }
    
    public byte[] getLM2SessionResponse()
      throws NTLMEngineException
    {
      if (this.lm2SessionResponse == null)
      {
        byte[] arrayOfByte = getClientChallenge();
        this.lm2SessionResponse = new byte[24];
        System.arraycopy(arrayOfByte, 0, this.lm2SessionResponse, 0, arrayOfByte.length);
        Arrays.fill(this.lm2SessionResponse, arrayOfByte.length, this.lm2SessionResponse.length, (byte)0);
      }
      return this.lm2SessionResponse;
    }
    
    public byte[] getLMHash()
      throws NTLMEngineException
    {
      if (this.lmHash == null) {
        this.lmHash = NTLMEngineImpl.lmHash(this.password);
      }
      return this.lmHash;
    }
    
    public byte[] getLMResponse()
      throws NTLMEngineException
    {
      if (this.lmResponse == null) {
        this.lmResponse = NTLMEngineImpl.lmResponse(getLMHash(), this.challenge);
      }
      return this.lmResponse;
    }
    
    public byte[] getLMUserSessionKey()
      throws NTLMEngineException
    {
      if (this.lmUserSessionKey == null)
      {
        byte[] arrayOfByte = getLMHash();
        this.lmUserSessionKey = new byte[16];
        System.arraycopy(arrayOfByte, 0, this.lmUserSessionKey, 0, 8);
        Arrays.fill(this.lmUserSessionKey, 8, 16, (byte)0);
      }
      return this.lmUserSessionKey;
    }
    
    public byte[] getLMv2Hash()
      throws NTLMEngineException
    {
      if (this.lmv2Hash == null) {
        this.lmv2Hash = NTLMEngineImpl.lmv2Hash(this.domain, this.user, getNTLMHash());
      }
      return this.lmv2Hash;
    }
    
    public byte[] getLMv2Response()
      throws NTLMEngineException
    {
      if (this.lmv2Response == null) {
        this.lmv2Response = NTLMEngineImpl.lmv2Response(getLMv2Hash(), this.challenge, getClientChallenge());
      }
      return this.lmv2Response;
    }
    
    public byte[] getLanManagerSessionKey()
      throws NTLMEngineException
    {
      byte[] arrayOfByte1;
      byte[] arrayOfByte2;
      if (this.lanManagerSessionKey == null)
      {
        arrayOfByte1 = getLMHash();
        arrayOfByte2 = getLMResponse();
      }
      try
      {
        byte[] arrayOfByte3 = new byte[14];
        System.arraycopy(arrayOfByte1, 0, arrayOfByte3, 0, 8);
        Arrays.fill(arrayOfByte3, 8, arrayOfByte3.length, (byte)-67);
        Key localKey1 = NTLMEngineImpl.createDESKey(arrayOfByte3, 0);
        Key localKey2 = NTLMEngineImpl.createDESKey(arrayOfByte3, 7);
        byte[] arrayOfByte4 = new byte[8];
        System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 0, arrayOfByte4.length);
        Cipher localCipher1 = Cipher.getInstance("DES/ECB/NoPadding");
        localCipher1.init(1, localKey1);
        byte[] arrayOfByte5 = localCipher1.doFinal(arrayOfByte4);
        Cipher localCipher2 = Cipher.getInstance("DES/ECB/NoPadding");
        localCipher2.init(1, localKey2);
        byte[] arrayOfByte6 = localCipher2.doFinal(arrayOfByte4);
        this.lanManagerSessionKey = new byte[16];
        System.arraycopy(arrayOfByte5, 0, this.lanManagerSessionKey, 0, arrayOfByte5.length);
        System.arraycopy(arrayOfByte6, 0, this.lanManagerSessionKey, arrayOfByte5.length, arrayOfByte6.length);
        return this.lanManagerSessionKey;
      }
      catch (Exception localException)
      {
        throw new NTLMEngineException(localException.getMessage(), localException);
      }
    }
    
    public byte[] getNTLM2SessionResponse()
      throws NTLMEngineException
    {
      if (this.ntlm2SessionResponse == null) {
        this.ntlm2SessionResponse = NTLMEngineImpl.ntlm2SessionResponse(getNTLMHash(), this.challenge, getClientChallenge());
      }
      return this.ntlm2SessionResponse;
    }
    
    public byte[] getNTLM2SessionResponseUserSessionKey()
      throws NTLMEngineException
    {
      if (this.ntlm2SessionResponseUserSessionKey == null)
      {
        byte[] arrayOfByte1 = getNTLMUserSessionKey();
        byte[] arrayOfByte2 = getLM2SessionResponse();
        byte[] arrayOfByte3 = new byte[this.challenge.length + arrayOfByte2.length];
        System.arraycopy(this.challenge, 0, arrayOfByte3, 0, this.challenge.length);
        System.arraycopy(arrayOfByte2, 0, arrayOfByte3, this.challenge.length, arrayOfByte2.length);
        this.ntlm2SessionResponseUserSessionKey = NTLMEngineImpl.hmacMD5(arrayOfByte3, arrayOfByte1);
      }
      return this.ntlm2SessionResponseUserSessionKey;
    }
    
    public byte[] getNTLMHash()
      throws NTLMEngineException
    {
      if (this.ntlmHash == null) {
        this.ntlmHash = NTLMEngineImpl.ntlmHash(this.password);
      }
      return this.ntlmHash;
    }
    
    public byte[] getNTLMResponse()
      throws NTLMEngineException
    {
      if (this.ntlmResponse == null) {
        this.ntlmResponse = NTLMEngineImpl.lmResponse(getNTLMHash(), this.challenge);
      }
      return this.ntlmResponse;
    }
    
    public byte[] getNTLMUserSessionKey()
      throws NTLMEngineException
    {
      if (this.ntlmUserSessionKey == null)
      {
        byte[] arrayOfByte = getNTLMHash();
        NTLMEngineImpl.MD4 localMD4 = new NTLMEngineImpl.MD4();
        localMD4.update(arrayOfByte);
        this.ntlmUserSessionKey = localMD4.getOutput();
      }
      return this.ntlmUserSessionKey;
    }
    
    public byte[] getNTLMv2Blob()
      throws NTLMEngineException
    {
      if (this.ntlmv2Blob == null) {
        this.ntlmv2Blob = NTLMEngineImpl.createBlob(getClientChallenge2(), this.targetInformation, getTimestamp());
      }
      return this.ntlmv2Blob;
    }
    
    public byte[] getNTLMv2Hash()
      throws NTLMEngineException
    {
      if (this.ntlmv2Hash == null) {
        this.ntlmv2Hash = NTLMEngineImpl.ntlmv2Hash(this.domain, this.user, getNTLMHash());
      }
      return this.ntlmv2Hash;
    }
    
    public byte[] getNTLMv2Response()
      throws NTLMEngineException
    {
      if (this.ntlmv2Response == null) {
        this.ntlmv2Response = NTLMEngineImpl.lmv2Response(getNTLMv2Hash(), this.challenge, getNTLMv2Blob());
      }
      return this.ntlmv2Response;
    }
    
    public byte[] getNTLMv2UserSessionKey()
      throws NTLMEngineException
    {
      if (this.ntlmv2UserSessionKey == null)
      {
        byte[] arrayOfByte1 = getNTLMv2Hash();
        byte[] arrayOfByte2 = new byte[16];
        System.arraycopy(getNTLMv2Response(), 0, arrayOfByte2, 0, 16);
        this.ntlmv2UserSessionKey = NTLMEngineImpl.hmacMD5(arrayOfByte2, arrayOfByte1);
      }
      return this.ntlmv2UserSessionKey;
    }
    
    public byte[] getSecondaryKey()
      throws NTLMEngineException
    {
      if (this.secondaryKey == null) {
        this.secondaryKey = NTLMEngineImpl.access$100();
      }
      return this.secondaryKey;
    }
    
    public byte[] getTimestamp()
    {
      if (this.timestamp == null)
      {
        long l = 10000L * (11644473600000L + System.currentTimeMillis());
        this.timestamp = new byte[8];
        for (int i = 0; i < 8; i++)
        {
          this.timestamp[i] = ((byte)(int)l);
          l >>>= 8;
        }
      }
      return this.timestamp;
    }
  }
  
  static class HMACMD5
  {
    protected byte[] ipad;
    protected MessageDigest md5;
    protected byte[] opad;
    
    HMACMD5(byte[] paramArrayOfByte)
      throws NTLMEngineException
    {
      byte[] arrayOfByte = paramArrayOfByte;
      int j;
      try
      {
        this.md5 = MessageDigest.getInstance("MD5");
        this.ipad = new byte[64];
        this.opad = new byte[64];
        int i = arrayOfByte.length;
        if (i > 64)
        {
          this.md5.update(arrayOfByte);
          arrayOfByte = this.md5.digest();
          i = arrayOfByte.length;
        }
        for (j = 0; j < i; j++)
        {
          this.ipad[j] = ((byte)(0x36 ^ arrayOfByte[j]));
          this.opad[j] = ((byte)(0x5C ^ arrayOfByte[j]));
        }
        if (j >= 64) {
          break label171;
        }
      }
      catch (Exception localException)
      {
        throw new NTLMEngineException("Error getting md5 message digest implementation: " + localException.getMessage(), localException);
      }
      for (;;)
      {
        this.ipad[j] = 54;
        this.opad[j] = 92;
        j++;
      }
      label171:
      this.md5.reset();
      this.md5.update(this.ipad);
    }
    
    byte[] getOutput()
    {
      byte[] arrayOfByte = this.md5.digest();
      this.md5.update(this.opad);
      return this.md5.digest(arrayOfByte);
    }
    
    void update(byte[] paramArrayOfByte)
    {
      this.md5.update(paramArrayOfByte);
    }
    
    void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    {
      this.md5.update(paramArrayOfByte, paramInt1, paramInt2);
    }
  }
  
  static class MD4
  {
    protected int A = 1732584193;
    protected int B = -271733879;
    protected int C = -1732584194;
    protected int D = 271733878;
    protected long count = 0L;
    protected byte[] dataBuffer = new byte[64];
    
    byte[] getOutput()
    {
      int i = (int)(0x3F & this.count);
      if (i < 56) {}
      byte[] arrayOfByte1;
      for (int j = 56 - i;; j = 120 - i)
      {
        arrayOfByte1 = new byte[j + 8];
        arrayOfByte1[0] = -128;
        for (int k = 0; k < 8; k++) {
          arrayOfByte1[(j + k)] = ((byte)(int)(8L * this.count >>> k * 8));
        }
      }
      update(arrayOfByte1);
      byte[] arrayOfByte2 = new byte[16];
      NTLMEngineImpl.writeULong(arrayOfByte2, this.A, 0);
      NTLMEngineImpl.writeULong(arrayOfByte2, this.B, 4);
      NTLMEngineImpl.writeULong(arrayOfByte2, this.C, 8);
      NTLMEngineImpl.writeULong(arrayOfByte2, this.D, 12);
      return arrayOfByte2;
    }
    
    protected void processBuffer()
    {
      int[] arrayOfInt = new int[16];
      for (int i = 0; i < 16; i++) {
        arrayOfInt[i] = ((0xFF & this.dataBuffer[(i * 4)]) + ((0xFF & this.dataBuffer[(1 + i * 4)]) << 8) + ((0xFF & this.dataBuffer[(2 + i * 4)]) << 16) + ((0xFF & this.dataBuffer[(3 + i * 4)]) << 24));
      }
      int j = this.A;
      int k = this.B;
      int m = this.C;
      int n = this.D;
      round1(arrayOfInt);
      round2(arrayOfInt);
      round3(arrayOfInt);
      this.A = (j + this.A);
      this.B = (k + this.B);
      this.C = (m + this.C);
      this.D = (n + this.D);
    }
    
    protected void round1(int[] paramArrayOfInt)
    {
      this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + paramArrayOfInt[0], 3);
      this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + paramArrayOfInt[1], 7);
      this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + paramArrayOfInt[2], 11);
      this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + paramArrayOfInt[3], 19);
      this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + paramArrayOfInt[4], 3);
      this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + paramArrayOfInt[5], 7);
      this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + paramArrayOfInt[6], 11);
      this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + paramArrayOfInt[7], 19);
      this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + paramArrayOfInt[8], 3);
      this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + paramArrayOfInt[9], 7);
      this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + paramArrayOfInt[10], 11);
      this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + paramArrayOfInt[11], 19);
      this.A = NTLMEngineImpl.rotintlft(this.A + NTLMEngineImpl.F(this.B, this.C, this.D) + paramArrayOfInt[12], 3);
      this.D = NTLMEngineImpl.rotintlft(this.D + NTLMEngineImpl.F(this.A, this.B, this.C) + paramArrayOfInt[13], 7);
      this.C = NTLMEngineImpl.rotintlft(this.C + NTLMEngineImpl.F(this.D, this.A, this.B) + paramArrayOfInt[14], 11);
      this.B = NTLMEngineImpl.rotintlft(this.B + NTLMEngineImpl.F(this.C, this.D, this.A) + paramArrayOfInt[15], 19);
    }
    
    protected void round2(int[] paramArrayOfInt)
    {
      this.A = NTLMEngineImpl.rotintlft(1518500249 + (this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + paramArrayOfInt[0]), 3);
      this.D = NTLMEngineImpl.rotintlft(1518500249 + (this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + paramArrayOfInt[4]), 5);
      this.C = NTLMEngineImpl.rotintlft(1518500249 + (this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + paramArrayOfInt[8]), 9);
      this.B = NTLMEngineImpl.rotintlft(1518500249 + (this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + paramArrayOfInt[12]), 13);
      this.A = NTLMEngineImpl.rotintlft(1518500249 + (this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + paramArrayOfInt[1]), 3);
      this.D = NTLMEngineImpl.rotintlft(1518500249 + (this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + paramArrayOfInt[5]), 5);
      this.C = NTLMEngineImpl.rotintlft(1518500249 + (this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + paramArrayOfInt[9]), 9);
      this.B = NTLMEngineImpl.rotintlft(1518500249 + (this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + paramArrayOfInt[13]), 13);
      this.A = NTLMEngineImpl.rotintlft(1518500249 + (this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + paramArrayOfInt[2]), 3);
      this.D = NTLMEngineImpl.rotintlft(1518500249 + (this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + paramArrayOfInt[6]), 5);
      this.C = NTLMEngineImpl.rotintlft(1518500249 + (this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + paramArrayOfInt[10]), 9);
      this.B = NTLMEngineImpl.rotintlft(1518500249 + (this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + paramArrayOfInt[14]), 13);
      this.A = NTLMEngineImpl.rotintlft(1518500249 + (this.A + NTLMEngineImpl.G(this.B, this.C, this.D) + paramArrayOfInt[3]), 3);
      this.D = NTLMEngineImpl.rotintlft(1518500249 + (this.D + NTLMEngineImpl.G(this.A, this.B, this.C) + paramArrayOfInt[7]), 5);
      this.C = NTLMEngineImpl.rotintlft(1518500249 + (this.C + NTLMEngineImpl.G(this.D, this.A, this.B) + paramArrayOfInt[11]), 9);
      this.B = NTLMEngineImpl.rotintlft(1518500249 + (this.B + NTLMEngineImpl.G(this.C, this.D, this.A) + paramArrayOfInt[15]), 13);
    }
    
    protected void round3(int[] paramArrayOfInt)
    {
      this.A = NTLMEngineImpl.rotintlft(1859775393 + (this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + paramArrayOfInt[0]), 3);
      this.D = NTLMEngineImpl.rotintlft(1859775393 + (this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + paramArrayOfInt[8]), 9);
      this.C = NTLMEngineImpl.rotintlft(1859775393 + (this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + paramArrayOfInt[4]), 11);
      this.B = NTLMEngineImpl.rotintlft(1859775393 + (this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + paramArrayOfInt[12]), 15);
      this.A = NTLMEngineImpl.rotintlft(1859775393 + (this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + paramArrayOfInt[2]), 3);
      this.D = NTLMEngineImpl.rotintlft(1859775393 + (this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + paramArrayOfInt[10]), 9);
      this.C = NTLMEngineImpl.rotintlft(1859775393 + (this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + paramArrayOfInt[6]), 11);
      this.B = NTLMEngineImpl.rotintlft(1859775393 + (this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + paramArrayOfInt[14]), 15);
      this.A = NTLMEngineImpl.rotintlft(1859775393 + (this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + paramArrayOfInt[1]), 3);
      this.D = NTLMEngineImpl.rotintlft(1859775393 + (this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + paramArrayOfInt[9]), 9);
      this.C = NTLMEngineImpl.rotintlft(1859775393 + (this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + paramArrayOfInt[5]), 11);
      this.B = NTLMEngineImpl.rotintlft(1859775393 + (this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + paramArrayOfInt[13]), 15);
      this.A = NTLMEngineImpl.rotintlft(1859775393 + (this.A + NTLMEngineImpl.H(this.B, this.C, this.D) + paramArrayOfInt[3]), 3);
      this.D = NTLMEngineImpl.rotintlft(1859775393 + (this.D + NTLMEngineImpl.H(this.A, this.B, this.C) + paramArrayOfInt[11]), 9);
      this.C = NTLMEngineImpl.rotintlft(1859775393 + (this.C + NTLMEngineImpl.H(this.D, this.A, this.B) + paramArrayOfInt[7]), 11);
      this.B = NTLMEngineImpl.rotintlft(1859775393 + (this.B + NTLMEngineImpl.H(this.C, this.D, this.A) + paramArrayOfInt[15]), 15);
    }
    
    void update(byte[] paramArrayOfByte)
    {
      int i = (int)(0x3F & this.count);
      int j = 0;
      while (i + (paramArrayOfByte.length - j) >= this.dataBuffer.length)
      {
        int m = this.dataBuffer.length - i;
        System.arraycopy(paramArrayOfByte, j, this.dataBuffer, i, m);
        this.count += m;
        j += m;
        processBuffer();
        i = 0;
      }
      if (j < paramArrayOfByte.length)
      {
        int k = paramArrayOfByte.length - j;
        System.arraycopy(paramArrayOfByte, j, this.dataBuffer, i, k);
        this.count += k;
        (i + k);
      }
    }
  }
  
  static class NTLMMessage
  {
    private int currentOutputPosition = 0;
    private byte[] messageContents = null;
    
    NTLMMessage() {}
    
    NTLMMessage(String paramString, int paramInt)
      throws NTLMEngineException
    {
      this.messageContents = Base64.decodeBase64(EncodingUtils.getBytes(paramString, "ASCII"));
      if (this.messageContents.length < NTLMEngineImpl.SIGNATURE.length) {
        throw new NTLMEngineException("NTLM message decoding error - packet too short");
      }
      for (int i = 0; i < NTLMEngineImpl.SIGNATURE.length; i++) {
        if (this.messageContents[i] != NTLMEngineImpl.SIGNATURE[i]) {
          throw new NTLMEngineException("NTLM message expected - instead got unrecognized bytes");
        }
      }
      int j = readULong(NTLMEngineImpl.SIGNATURE.length);
      if (j != paramInt) {
        throw new NTLMEngineException("NTLM type " + Integer.toString(paramInt) + " message expected - instead got type " + Integer.toString(j));
      }
      this.currentOutputPosition = this.messageContents.length;
    }
    
    protected void addByte(byte paramByte)
    {
      this.messageContents[this.currentOutputPosition] = paramByte;
      this.currentOutputPosition = (1 + this.currentOutputPosition);
    }
    
    protected void addBytes(byte[] paramArrayOfByte)
    {
      if (paramArrayOfByte == null) {}
      for (;;)
      {
        return;
        int i = paramArrayOfByte.length;
        for (int j = 0; j < i; j++)
        {
          int k = paramArrayOfByte[j];
          this.messageContents[this.currentOutputPosition] = k;
          this.currentOutputPosition = (1 + this.currentOutputPosition);
        }
      }
    }
    
    protected void addULong(int paramInt)
    {
      addByte((byte)(paramInt & 0xFF));
      addByte((byte)(0xFF & paramInt >> 8));
      addByte((byte)(0xFF & paramInt >> 16));
      addByte((byte)(0xFF & paramInt >> 24));
    }
    
    protected void addUShort(int paramInt)
    {
      addByte((byte)(paramInt & 0xFF));
      addByte((byte)(0xFF & paramInt >> 8));
    }
    
    protected int getMessageLength()
    {
      return this.currentOutputPosition;
    }
    
    protected int getPreambleLength()
    {
      return 4 + NTLMEngineImpl.SIGNATURE.length;
    }
    
    String getResponse()
    {
      byte[] arrayOfByte2;
      if (this.messageContents.length > this.currentOutputPosition)
      {
        arrayOfByte2 = new byte[this.currentOutputPosition];
        System.arraycopy(this.messageContents, 0, arrayOfByte2, 0, this.currentOutputPosition);
      }
      for (byte[] arrayOfByte1 = arrayOfByte2;; arrayOfByte1 = this.messageContents) {
        return EncodingUtils.getAsciiString(Base64.encodeBase64(arrayOfByte1));
      }
    }
    
    protected void prepareResponse(int paramInt1, int paramInt2)
    {
      this.messageContents = new byte[paramInt1];
      this.currentOutputPosition = 0;
      addBytes(NTLMEngineImpl.SIGNATURE);
      addULong(paramInt2);
    }
    
    protected byte readByte(int paramInt)
      throws NTLMEngineException
    {
      if (this.messageContents.length < paramInt + 1) {
        throw new NTLMEngineException("NTLM: Message too short");
      }
      return this.messageContents[paramInt];
    }
    
    protected void readBytes(byte[] paramArrayOfByte, int paramInt)
      throws NTLMEngineException
    {
      if (this.messageContents.length < paramInt + paramArrayOfByte.length) {
        throw new NTLMEngineException("NTLM: Message too short");
      }
      System.arraycopy(this.messageContents, paramInt, paramArrayOfByte, 0, paramArrayOfByte.length);
    }
    
    protected byte[] readSecurityBuffer(int paramInt)
      throws NTLMEngineException
    {
      return NTLMEngineImpl.readSecurityBuffer(this.messageContents, paramInt);
    }
    
    protected int readULong(int paramInt)
      throws NTLMEngineException
    {
      return NTLMEngineImpl.readULong(this.messageContents, paramInt);
    }
    
    protected int readUShort(int paramInt)
      throws NTLMEngineException
    {
      return NTLMEngineImpl.readUShort(this.messageContents, paramInt);
    }
  }
  
  static class Type1Message
    extends NTLMEngineImpl.NTLMMessage
  {
    protected byte[] domainBytes;
    protected byte[] hostBytes;
    
    /* Error */
    Type1Message(String paramString1, String paramString2)
      throws NTLMEngineException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial 16	org/apache/http/impl/auth/NTLMEngineImpl$NTLMMessage:<init>	()V
      //   4: aload_2
      //   5: invokestatic 22	org/apache/http/impl/auth/NTLMEngineImpl:access$1400	(Ljava/lang/String;)Ljava/lang/String;
      //   8: astore 4
      //   10: aload_1
      //   11: invokestatic 25	org/apache/http/impl/auth/NTLMEngineImpl:access$1500	(Ljava/lang/String;)Ljava/lang/String;
      //   14: astore 5
      //   16: aload 4
      //   18: ifnull +48 -> 66
      //   21: aload 4
      //   23: ldc 27
      //   25: invokevirtual 33	java/lang/String:getBytes	(Ljava/lang/String;)[B
      //   28: astore 6
      //   30: aload_0
      //   31: aload 6
      //   33: putfield 35	org/apache/http/impl/auth/NTLMEngineImpl$Type1Message:hostBytes	[B
      //   36: aconst_null
      //   37: astore 7
      //   39: aload 5
      //   41: ifnull +18 -> 59
      //   44: aload 5
      //   46: getstatic 41	java/util/Locale:US	Ljava/util/Locale;
      //   49: invokevirtual 45	java/lang/String:toUpperCase	(Ljava/util/Locale;)Ljava/lang/String;
      //   52: ldc 27
      //   54: invokevirtual 33	java/lang/String:getBytes	(Ljava/lang/String;)[B
      //   57: astore 7
      //   59: aload_0
      //   60: aload 7
      //   62: putfield 47	org/apache/http/impl/auth/NTLMEngineImpl$Type1Message:domainBytes	[B
      //   65: return
      //   66: aconst_null
      //   67: astore 6
      //   69: goto -39 -> 30
      //   72: astore_3
      //   73: new 11	org/apache/http/impl/auth/NTLMEngineException
      //   76: dup
      //   77: new 49	java/lang/StringBuilder
      //   80: dup
      //   81: invokespecial 50	java/lang/StringBuilder:<init>	()V
      //   84: ldc 52
      //   86: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   89: aload_3
      //   90: invokevirtual 60	java/io/UnsupportedEncodingException:getMessage	()Ljava/lang/String;
      //   93: invokevirtual 56	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   96: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   99: aload_3
      //   100: invokespecial 66	org/apache/http/impl/auth/NTLMEngineException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   103: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	104	0	this	Type1Message
      //   0	104	1	paramString1	String
      //   0	104	2	paramString2	String
      //   72	28	3	localUnsupportedEncodingException	UnsupportedEncodingException
      //   8	14	4	str1	String
      //   14	31	5	str2	String
      //   28	40	6	arrayOfByte1	byte[]
      //   37	24	7	arrayOfByte2	byte[]
      // Exception table:
      //   from	to	target	type
      //   4	16	72	java/io/UnsupportedEncodingException
      //   21	30	72	java/io/UnsupportedEncodingException
      //   30	36	72	java/io/UnsupportedEncodingException
      //   44	59	72	java/io/UnsupportedEncodingException
      //   59	65	72	java/io/UnsupportedEncodingException
    }
    
    String getResponse()
    {
      prepareResponse(40, 1);
      addULong(-1576500735);
      addUShort(0);
      addUShort(0);
      addULong(40);
      addUShort(0);
      addUShort(0);
      addULong(40);
      addUShort(261);
      addULong(2600);
      addUShort(3840);
      return super.getResponse();
    }
  }
  
  static class Type2Message
    extends NTLMEngineImpl.NTLMMessage
  {
    protected byte[] challenge = new byte[8];
    protected int flags;
    protected String target;
    protected byte[] targetInfo;
    
    Type2Message(String paramString)
      throws NTLMEngineException
    {
      super(2);
      readBytes(this.challenge, 24);
      this.flags = readULong(20);
      if ((0x1 & this.flags) == 0) {
        throw new NTLMEngineException("NTLM type 2 message has flags that make no sense: " + Integer.toString(this.flags));
      }
      this.target = null;
      byte[] arrayOfByte2;
      if (getMessageLength() >= 20)
      {
        arrayOfByte2 = readSecurityBuffer(12);
        if (arrayOfByte2.length == 0) {}
      }
      try
      {
        this.target = new String(arrayOfByte2, "UnicodeLittleUnmarked");
        this.targetInfo = null;
        if (getMessageLength() >= 48)
        {
          byte[] arrayOfByte1 = readSecurityBuffer(40);
          if (arrayOfByte1.length != 0) {
            this.targetInfo = arrayOfByte1;
          }
        }
        return;
      }
      catch (UnsupportedEncodingException localUnsupportedEncodingException)
      {
        throw new NTLMEngineException(localUnsupportedEncodingException.getMessage(), localUnsupportedEncodingException);
      }
    }
    
    byte[] getChallenge()
    {
      return this.challenge;
    }
    
    int getFlags()
    {
      return this.flags;
    }
    
    String getTarget()
    {
      return this.target;
    }
    
    byte[] getTargetInfo()
    {
      return this.targetInfo;
    }
  }
  
  static class Type3Message
    extends NTLMEngineImpl.NTLMMessage
  {
    protected byte[] domainBytes;
    protected byte[] hostBytes;
    protected byte[] lmResp;
    protected byte[] ntResp;
    protected byte[] sessionKey;
    protected int type2Flags;
    protected byte[] userBytes;
    
    /* Error */
    Type3Message(String paramString1, String paramString2, String paramString3, String paramString4, byte[] paramArrayOfByte1, int paramInt, String paramString5, byte[] paramArrayOfByte2)
      throws NTLMEngineException
    {
      // Byte code:
      //   0: aload_0
      //   1: invokespecial 22	org/apache/http/impl/auth/NTLMEngineImpl$NTLMMessage:<init>	()V
      //   4: aload_0
      //   5: iload 6
      //   7: putfield 24	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:type2Flags	I
      //   10: aload_2
      //   11: invokestatic 30	org/apache/http/impl/auth/NTLMEngineImpl:access$1400	(Ljava/lang/String;)Ljava/lang/String;
      //   14: astore 9
      //   16: aload_1
      //   17: invokestatic 33	org/apache/http/impl/auth/NTLMEngineImpl:access$1500	(Ljava/lang/String;)Ljava/lang/String;
      //   20: astore 10
      //   22: new 35	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen
      //   25: dup
      //   26: aload 10
      //   28: aload_3
      //   29: aload 4
      //   31: aload 5
      //   33: aload 7
      //   35: aload 8
      //   37: invokespecial 38	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:<init>	(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;[B)V
      //   40: astore 11
      //   42: ldc 39
      //   44: iload 6
      //   46: iand
      //   47: ifeq +148 -> 195
      //   50: aload 8
      //   52: ifnull +143 -> 195
      //   55: aload 7
      //   57: ifnull +138 -> 195
      //   60: aload_0
      //   61: aload 11
      //   63: invokevirtual 43	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLMv2Response	()[B
      //   66: putfield 45	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:ntResp	[B
      //   69: aload_0
      //   70: aload 11
      //   72: invokevirtual 48	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLMv2Response	()[B
      //   75: putfield 50	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:lmResp	[B
      //   78: iload 6
      //   80: sipush 128
      //   83: iand
      //   84: ifeq +101 -> 185
      //   87: aload 11
      //   89: invokevirtual 53	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLanManagerSessionKey	()[B
      //   92: astore 18
      //   94: aload 18
      //   96: astore 13
      //   98: iload 6
      //   100: bipush 16
      //   102: iand
      //   103: ifeq +254 -> 357
      //   106: ldc 54
      //   108: iload 6
      //   110: iand
      //   111: ifeq +237 -> 348
      //   114: aload_0
      //   115: aload 11
      //   117: invokevirtual 57	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getSecondaryKey	()[B
      //   120: aload 13
      //   122: invokestatic 61	org/apache/http/impl/auth/NTLMEngineImpl:RC4	([B[B)[B
      //   125: putfield 63	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:sessionKey	[B
      //   128: aload 9
      //   130: ifnull +235 -> 365
      //   133: aload 9
      //   135: ldc 65
      //   137: invokevirtual 71	java/lang/String:getBytes	(Ljava/lang/String;)[B
      //   140: astore 14
      //   142: aload_0
      //   143: aload 14
      //   145: putfield 73	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:hostBytes	[B
      //   148: aload 10
      //   150: ifnull +221 -> 371
      //   153: aload 10
      //   155: getstatic 79	java/util/Locale:US	Ljava/util/Locale;
      //   158: invokevirtual 83	java/lang/String:toUpperCase	(Ljava/util/Locale;)Ljava/lang/String;
      //   161: ldc 65
      //   163: invokevirtual 71	java/lang/String:getBytes	(Ljava/lang/String;)[B
      //   166: astore 16
      //   168: aload_0
      //   169: aload 16
      //   171: putfield 85	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:domainBytes	[B
      //   174: aload_0
      //   175: aload_3
      //   176: ldc 65
      //   178: invokevirtual 71	java/lang/String:getBytes	(Ljava/lang/String;)[B
      //   181: putfield 87	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:userBytes	[B
      //   184: return
      //   185: aload 11
      //   187: invokevirtual 90	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLMv2UserSessionKey	()[B
      //   190: astore 13
      //   192: goto -94 -> 98
      //   195: ldc 91
      //   197: iload 6
      //   199: iand
      //   200: ifeq +50 -> 250
      //   203: aload_0
      //   204: aload 11
      //   206: invokevirtual 94	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLM2SessionResponse	()[B
      //   209: putfield 45	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:ntResp	[B
      //   212: aload_0
      //   213: aload 11
      //   215: invokevirtual 97	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLM2SessionResponse	()[B
      //   218: putfield 50	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:lmResp	[B
      //   221: iload 6
      //   223: sipush 128
      //   226: iand
      //   227: ifeq +13 -> 240
      //   230: aload 11
      //   232: invokevirtual 53	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLanManagerSessionKey	()[B
      //   235: astore 13
      //   237: goto -139 -> 98
      //   240: aload 11
      //   242: invokevirtual 100	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLM2SessionResponseUserSessionKey	()[B
      //   245: astore 13
      //   247: goto -149 -> 98
      //   250: aload_0
      //   251: aload 11
      //   253: invokevirtual 103	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLMResponse	()[B
      //   256: putfield 45	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:ntResp	[B
      //   259: aload_0
      //   260: aload 11
      //   262: invokevirtual 106	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLMResponse	()[B
      //   265: putfield 50	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:lmResp	[B
      //   268: iload 6
      //   270: sipush 128
      //   273: iand
      //   274: ifeq +13 -> 287
      //   277: aload 11
      //   279: invokevirtual 53	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLanManagerSessionKey	()[B
      //   282: astore 13
      //   284: goto -186 -> 98
      //   287: aload 11
      //   289: invokevirtual 109	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getNTLMUserSessionKey	()[B
      //   292: astore 17
      //   294: aload 17
      //   296: astore 13
      //   298: goto -200 -> 98
      //   301: astore 12
      //   303: aload_0
      //   304: iconst_0
      //   305: newarray byte
      //   307: putfield 45	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:ntResp	[B
      //   310: aload_0
      //   311: aload 11
      //   313: invokevirtual 106	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLMResponse	()[B
      //   316: putfield 50	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:lmResp	[B
      //   319: iload 6
      //   321: sipush 128
      //   324: iand
      //   325: ifeq +13 -> 338
      //   328: aload 11
      //   330: invokevirtual 53	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLanManagerSessionKey	()[B
      //   333: astore 13
      //   335: goto -237 -> 98
      //   338: aload 11
      //   340: invokevirtual 112	org/apache/http/impl/auth/NTLMEngineImpl$CipherGen:getLMUserSessionKey	()[B
      //   343: astore 13
      //   345: goto -247 -> 98
      //   348: aload_0
      //   349: aload 13
      //   351: putfield 63	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:sessionKey	[B
      //   354: goto -226 -> 128
      //   357: aload_0
      //   358: aconst_null
      //   359: putfield 63	org/apache/http/impl/auth/NTLMEngineImpl$Type3Message:sessionKey	[B
      //   362: goto -234 -> 128
      //   365: aconst_null
      //   366: astore 14
      //   368: goto -226 -> 142
      //   371: aconst_null
      //   372: astore 16
      //   374: goto -206 -> 168
      //   377: astore 15
      //   379: new 17	org/apache/http/impl/auth/NTLMEngineException
      //   382: dup
      //   383: new 114	java/lang/StringBuilder
      //   386: dup
      //   387: invokespecial 115	java/lang/StringBuilder:<init>	()V
      //   390: ldc 117
      //   392: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   395: aload 15
      //   397: invokevirtual 125	java/io/UnsupportedEncodingException:getMessage	()Ljava/lang/String;
      //   400: invokevirtual 121	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   403: invokevirtual 128	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   406: aload 15
      //   408: invokespecial 131	org/apache/http/impl/auth/NTLMEngineException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
      //   411: athrow
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	412	0	this	Type3Message
      //   0	412	1	paramString1	String
      //   0	412	2	paramString2	String
      //   0	412	3	paramString3	String
      //   0	412	4	paramString4	String
      //   0	412	5	paramArrayOfByte1	byte[]
      //   0	412	6	paramInt	int
      //   0	412	7	paramString5	String
      //   0	412	8	paramArrayOfByte2	byte[]
      //   14	120	9	str1	String
      //   20	134	10	str2	String
      //   40	299	11	localCipherGen	NTLMEngineImpl.CipherGen
      //   301	1	12	localNTLMEngineException	NTLMEngineException
      //   96	254	13	localObject	Object
      //   140	227	14	arrayOfByte1	byte[]
      //   377	30	15	localUnsupportedEncodingException	UnsupportedEncodingException
      //   166	207	16	arrayOfByte2	byte[]
      //   292	3	17	arrayOfByte3	byte[]
      //   92	3	18	arrayOfByte4	byte[]
      // Exception table:
      //   from	to	target	type
      //   60	78	301	org/apache/http/impl/auth/NTLMEngineException
      //   87	94	301	org/apache/http/impl/auth/NTLMEngineException
      //   185	192	301	org/apache/http/impl/auth/NTLMEngineException
      //   203	221	301	org/apache/http/impl/auth/NTLMEngineException
      //   230	237	301	org/apache/http/impl/auth/NTLMEngineException
      //   240	247	301	org/apache/http/impl/auth/NTLMEngineException
      //   250	268	301	org/apache/http/impl/auth/NTLMEngineException
      //   277	284	301	org/apache/http/impl/auth/NTLMEngineException
      //   287	294	301	org/apache/http/impl/auth/NTLMEngineException
      //   133	142	377	java/io/UnsupportedEncodingException
      //   142	148	377	java/io/UnsupportedEncodingException
      //   153	168	377	java/io/UnsupportedEncodingException
      //   168	184	377	java/io/UnsupportedEncodingException
    }
    
    String getResponse()
    {
      int i = this.ntResp.length;
      int j = this.lmResp.length;
      int k;
      int m;
      label39:
      int n;
      if (this.domainBytes != null)
      {
        k = this.domainBytes.length;
        if (this.hostBytes == null) {
          break label392;
        }
        m = this.hostBytes.length;
        n = this.userBytes.length;
        if (this.sessionKey == null) {
          break label398;
        }
      }
      label392:
      label398:
      for (int i1 = this.sessionKey.length;; i1 = 0)
      {
        int i2 = j + 72;
        int i3 = i2 + i;
        int i4 = i3 + k;
        int i5 = i4 + n;
        int i6 = i5 + m;
        prepareResponse(i6 + i1, 3);
        addUShort(j);
        addUShort(j);
        addULong(72);
        addUShort(i);
        addUShort(i);
        addULong(i2);
        addUShort(k);
        addUShort(k);
        addULong(i3);
        addUShort(n);
        addUShort(n);
        addULong(i4);
        addUShort(m);
        addUShort(m);
        addULong(i5);
        addUShort(i1);
        addUShort(i1);
        addULong(i6);
        addULong(0x2000000 | 0x80 & this.type2Flags | 0x200 & this.type2Flags | 0x80000 & this.type2Flags | 0x8000 & this.type2Flags | 0x20 & this.type2Flags | 0x10 & this.type2Flags | 0x20000000 & this.type2Flags | 0x80000000 & this.type2Flags | 0x40000000 & this.type2Flags | 0x800000 & this.type2Flags | 0x1 & this.type2Flags | 0x4 & this.type2Flags);
        addUShort(261);
        addULong(2600);
        addUShort(3840);
        addBytes(this.lmResp);
        addBytes(this.ntResp);
        addBytes(this.domainBytes);
        addBytes(this.userBytes);
        addBytes(this.hostBytes);
        if (this.sessionKey != null) {
          addBytes(this.sessionKey);
        }
        return super.getResponse();
        k = 0;
        break;
        m = 0;
        break label39;
      }
    }
  }
}


/* Location:           C:\Users\delco\Desktop\projet_S4\test\src\dex2jar-0.0.7.7-SNAPSHOT\classes_dex2jar.jar
 * Qualified Name:     org.apache.http.impl.auth.NTLMEngineImpl
 * JD-Core Version:    0.7.0.1
 */