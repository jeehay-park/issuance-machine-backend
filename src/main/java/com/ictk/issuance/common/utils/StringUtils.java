package com.ictk.issuance.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class StringUtils {

    public enum CONFIG {
        trim, format, replace, substring, convert, split, keyvalue, branch, branchresult, result,
        rearrange, keep, copy, ignore, newstring, geoip, regex, makekeyvalue, meta, operator,
        metacase, concat, contains, remove, normalize, length, json, xml, tonumber, evaluate, none;

        public static CONFIG toCONFIG(String str) {
            try {
                return valueOf(str);
            } catch (Exception e) {
                return none;
            }
        }
    }

    public static final int REPLACE_ALL = 0;
    public static final int REPLACE_FIRST = 1;
    public static final int REPLACE_LAST = 2;
    public static final int REPLACE_RANGE = 3;

    public static final int SUBSTRING_INCLUDE = 0;
    public static final int SUBSTRING_EXCLUDE = 1;
    public static final int SUBSTRING_CHOOSE = 2;

    public static final int REMOVE_INCLUDE = 0;
    public static final int REMOVE_EXCLUDE = 1;

    public static final int EVALOPT_COMPUTE_NUMBERS = 0;
    public static final int EVALOPT_COMPARE_NUMBERS = 1;
    public static final int EVALOPT_CHECK_STRINGS = 2;

    public static final String UTF_8 = "UTF-8";
    public static final String C_FRAMEWORK_FREESIA_LIBEXT_PATH = "/home/freesia/c_framework/freesia/libext/";
    public static final String STRING = "string";
    public static final String NUMBER = "number";
    public static final String NULL = "null";
    public static final String IGNORE = "ignore";
    public static final String $ORG = "$ORG";
    public static final String TEMP__CHANGE = "temp__change";

    public static final String $VAL = "$VAL";

    public static final String PADDING_LEFT = "LEFT";
    public static final String PADDING_RIGHT = "RIGHT";

    private static Logger logger = LogManager.getLogger(StringUtils.class);


    /** FREESIA Normalizer의 trim{...} configuration에 대응하는 함수이다. */
    /**
     * trim 옵션 : 이 flag를 설정하면 문자열의 맨 처음과 끝부분에 추가되어 있는 whitespace [ \t\n\x0B\f\r]를 제거한다.
     * squeeze 옵션 : 이 flag를 설정하면 문자열내의 공백(' ')이나 탭(\t)이 여러개 중복되어 나올경우 하나로만 인식한다.
     */
    public static String trim(boolean trim, boolean squeeze, String s) {
        if (s == null)
            return null;
        if (s.length() <= 0)
            return s;
        if (squeeze) {
            char[] arr = s.toCharArray();
            int next = 1; // refers to the next character in the result.

            for (int i = 1; i < arr.length; i++) {
                arr[next] = arr[i];
                if (arr[next] != ' ' && arr[next] != '\t')
                    next++;
                else if (arr[next - 1] != ' ' && arr[next - 1] != '\t')
                    next++;
            }
            if (trim)
                return new String(arr, 0, next).trim();
            else
                return new String(arr, 0, next);
        } else {
            if (trim)
                return s.trim();
            else
                return s;
        }
    }


    /**
     * getDiffFormats함수에서 구한 문자열비교 포맷(format)과 getDiffCharacters 함수에서 구한 추가 문자열을 가지고서
     * 입력되는 문자열을 원하는 변환문자열로 변환하는 함수이다.
     * EX : format("20131107 16:03:50.234Z", "ssssassasssssrssrssrasssr", "//_") --> 2013/11/07 160350_234
     *    : format("20131210 09:10:21.113Z", "ssssassasssssrssrssrasssr", "//_") --> 2013/12/10 091021_113
     */
    public static String format(String str, /* String strlength, */String fmt, String chstr) throws Exception {
        StringBuffer outb = new StringBuffer();
        int stri = 0;
        int chstri = 0;

        for (int i = 0; i < fmt.length(); i++) {
            if (fmt.charAt(i) == 's') {
                outb.append(str.charAt(stri));
                stri++;
            } else if (fmt.charAt(i) == 'a') {
                outb.append(chstr.charAt(chstri));
                chstri++;
            } else if (fmt.charAt(i) == 'r') {
                stri++;
            }
        }
        return outb.toString();
    }

    /**
     * 원 문자열내에 발생하는 구문자열(old string:os)을 신문자열(new string)으로 변경하여 리턴하는 함수이다.
     * 다음과 같은 변경옵션이 있다.
     *   REPLACE_ALL: 원문자열내에서 모든 구문자열을 신문자열로 변경,
     *   REPLACE_FIRST: 원문자열내에서 최초 발생하는 구문자열만을 신문자열로 변경,
     *   REPLACE_LAST : 원문자열내에서 마지막으로 발생하는 구문자열만을 신문자열로 변경.
     * EX : 원문자열 --> 192.168.100.130||FW||Lowest||20131001110020||Allow||Log||124.57.85.7 -> 178.100.144.118||Allow
     *    : replace(NormalizerUtils.REPLACE_ALL, str, "||", "@")	--> 192.168.100.130@FW@Lowest@20131001110020@Allow@Log@124.57.85.7 -> 178.100.144.118@Allow
     *    : replace(NormalizerUtils.REPLACE_FIRST, str, "||", "@")	--> 192.168.100.130@FW||Lowest||20131001110020||Allow||Log||124.57.85.7 -> 178.100.144.118||Allow
     *    : replace(NormalizerUtils.REPLACE_LAST, str, "||", "@")	--> 192.168.100.130||FW||Lowest||20131001110020||Allow||Log||124.57.85.7 -> 178.100.144.118@Allow
     */
    public static String replace(String source, String os, String ns) {
        return replace(REPLACE_ALL, source, os, ns);
    }
    public static String replace(int option, String source, String os, String ns) {
        if (source == null) {
            return null;
        }
        int i = 0;
        if (option == REPLACE_LAST)
            i = source.lastIndexOf(os);
        else
            i = source.indexOf(os, i);
        // if ((i = source.indexOf(os, i)) >= 0) {
        if (i >= 0) {
            char[] sourceArray = source.toCharArray();
            char[] nsArray = ns.toCharArray();
            int oLength = os.length();
            StringBuffer buf = new StringBuffer(sourceArray.length);
            buf.append(sourceArray, 0, i).append(nsArray);
            i += oLength;
            int j = i;

            if (option == REPLACE_FIRST || option == REPLACE_LAST) {
                buf.append(sourceArray, j, sourceArray.length - j);
            } else {
                // Replace all remaining instances of oldString with newString.
                while ((i = source.indexOf(os, i)) > 0) {
                    buf.append(sourceArray, j, i - j).append(nsArray);
                    i += oLength;
                    j = i;
                }
                buf.append(sourceArray, j, sourceArray.length - j);
            }
            source = buf.toString();
            buf.setLength(0);
        }
        return source;
    }

    /**
     * 원 문자열내에서 접두사(문자열)과 접미사(문자열)내의 문자열을 리턴하는 함수이다.
     * 접두사(문자열)와 접미사(문자열)를 포함하는 지 여부에 따라 다음과 같은 옵션이 있다.
     *   SUBSTRING_INCLUDE : 접두사(문자열)와 접미사(문자열)를 포함하여 결과를 리턴함.
     *   SUBSTRING_EXCLUDE : 접두사(문자열)와 접미사(문자열)를 제외한후에 결과를 리턴함.
     * 문자열내에 접두사(문자열)혹은 접미사(문자열)의 발생이 여러 건이 경우에 최초발생 혹은 최종발생유무도 고려한다.
     * EX : 원문자열 --> FREESIA{{..cfgfirst..cfgfirst..}}FREESIA{{~~cfgmedium~~cfgmedium~~}}FREESIA{{||cfglast||cfglast||}}
     * CASE1 : substring(NormalizerUtils.SUBSTRING_INCLUDE, true, true, cfgstr, "FREESIA{", "}")
     *         해석 : 접두사접미사 포함, 접두사 최초발생, 접미사 최초발생
     *         --> FREESIA{{..cfgfirst..cfgfirst..}
     *       : substring(NormalizerUtils.SUBSTRING_INCLUDE, true, true, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함, 접두사 최초발생, 접미사 최초발생
     *         --> FREESIA{{..cfgfirst..cfgfirst..}}
     * CASE2 : substring(NormalizerUtils.SUBSTRING_INCLUDE, true, false, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함, 접두사 최초발생, 접미사 최종발생
     *         --> FREESIA{{..cfgfirst..cfgfirst..}}FREESIA{{~~cfgmedium~~cfgmedium~~}}FREESIA{{||cfglast||cfglast||}}
     * CASE3 : substring(NormalizerUtils.SUBSTRING_INCLUDE, false, true, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함, 접두사 최종발생, 접미사 최초발생
     *         --> FREESIA{{||cfglast||cfglast||}}
     * CASE4 : substring(NormalizerUtils.SUBSTRING_INCLUDE, false, false, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함, 접두사 최종발생, 접미사 최종발생
     *         --> FREESIA{{||cfglast||cfglast||}}
     * CASE5 : substring(NormalizerUtils.SUBSTRING_EXCLUDE, true, true, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함안함, 접두사 최초발생, 접미사 최초발생
     *         --> ..cfgfirst..cfgfirst..
     * CASE6 : substring(NormalizerUtils.SUBSTRING_EXCLUDE, true, false, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함안함, 접두사 최초발생, 접미사 최종발생
     *         --> ..cfgfirst..cfgfirst..}}FREESIA{{~~cfgmedium~~cfgmedium~~}}FREESIA{{||cfglast||cfglast||
     * CASE7 : substring(NormalizerUtils.SUBSTRING_EXCLUDE, false, true, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함안함, 접두사 최종발생, 접미사 최초발생
     *         --> ||cfglast||cfglast||
     * CASE8 : substring(NormalizerUtils.SUBSTRING_EXCLUDE, false, false, cfgstr, "FREESIA{{", "}}")
     *         해석 : 접두사접미사 포함안함, 접두사 최종발생, 접미사 최종발생
     *         --> ||cfglast||cfglast||
     */
    public static String substring(int option, boolean isprefixfirst, boolean issuffixfirst, String org, String prefix, String suffix) {
        String rtn = null;
        int begin = -1;
        int end = -1;
        int inchk = -1;
        try {
            if (option == SUBSTRING_INCLUDE) {
                if (isprefixfirst)
                    begin = org.indexOf(prefix);
                else
                    begin = org.lastIndexOf(prefix);
                if (issuffixfirst)
                    end = org.indexOf(suffix, begin + prefix.length()) + suffix.length();
                else
                    end = org.lastIndexOf(suffix) + suffix.length();
                if (begin >= 0 && end > 0 && end > begin) {
                    rtn = org.substring(begin, end);
                }
            } else {
                inchk = org.indexOf(prefix);
                if (isprefixfirst) {
                    begin = org.indexOf(prefix) + prefix.length();
                    // begin = org.indexOf(prefix);
                } else {
                    begin = org.lastIndexOf(prefix) + prefix.length();
                    // begin = org.lastIndexOf(prefix);
                }
                if (issuffixfirst)
                    end = org.indexOf(suffix, begin);
                else
                    end = org.lastIndexOf(suffix);

                // begin += prefix.length();
                if (inchk >= 0 && end > 0 && end > begin) {
                    rtn = org.substring(begin, end);
                } else if (inchk >= 0 && end > 0 && end == begin) {
                    rtn = "";
                }
            }
            // System.out.println("begin:"+begin+" end:"+end);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rtn;
    }

    // SUBSTRING_CHOOSE : nth번째 prefix출현 시점에서 suffix 출현시점 사이의 문자열을 반환.
    // String line = "<186>Nov 09 2014 23:59:58: %ASA-2-106006: Deny inbound UDP from 172.43.121.138/49154 to 172.23.121.19/161 on interface outside"
    // substring(SUBSTRING_CHOOSE, 2, line, "-", ":")
    // 2번째 발생한 '-'와 이후의 ':'사이의 106006을 반환.
    public static String substring(int option, int nth, String org, String prefix, String suffix) {
        String rtn = null;
        int begin = -1;
        int end = -1;
        try {
            if (option == SUBSTRING_INCLUDE) {
                for (int i = 0; i < nth; i++) {
                    if (i == 0)
                        begin = org.indexOf(prefix, 0);
                    else
                        begin = org.indexOf(prefix, end);
                    if (begin < 0) {
                        // System.out.println("begin:"+begin);
                        return rtn;
                    }
                    end = org.indexOf(suffix, begin + prefix.length());
                    // System.out.println("begin:"+begin+",end:"+end);
                    if (begin < 0 || end < 0)
                        return rtn;
                }
                end += suffix.length();
                if (begin >= 0 && end > 0 && end > begin) {
                    rtn = org.substring(begin, end);
                }
            } else if (option == SUBSTRING_EXCLUDE) {
                for (int i = 0; i < nth; i++) {
                    if (i == 0)
                        begin = org.indexOf(prefix, 0);
                    else
                        begin = org.indexOf(prefix, end);
                    if (begin < 0) {
                        // System.out.println("begin:"+begin);
                        return rtn;
                    }
                    end = org.indexOf(suffix, begin + prefix.length());
                    // System.out.println("begin:"+begin+",end:"+end);
                    if (begin < 0 || end < 0)
                        return rtn;
                }
                begin += prefix.length();
                if (begin >= 0 && end > 0 && end > begin) {
                    rtn = org.substring(begin, end);
                }
            } else {
                List<String> rtnList = Lists.newArrayList(Splitter.on(prefix).limit(nth + 1).split(org));
                rtn = Lists.newArrayList(Splitter.on(suffix).limit(2).split(rtnList.get(nth))).get(0);
                return rtn;
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rtn;
    }

    public static List<String> substring(int option, String org, String prefix, String suffix) {
        int i = 0;
        List<String> rtn = null;
        int begin = -1;
        int end = -1;
        try {
            if (option == SUBSTRING_INCLUDE) {
                while (true) {
                    if (i == 0)
                        begin = org.indexOf(prefix, 0);
                    else
                        begin = org.indexOf(prefix, end);
                    if (begin < 0) {
                        // System.out.println("begin:"+begin);
                        break;
                    }
                    end = org.indexOf(suffix, begin + prefix.length());
                    // System.out.println("begin:"+begin+",end:"+end);
                    if (begin < 0 || end < 0)
                        break;
                    end += suffix.length();
                    if (begin >= 0 && end > 0 && end > begin) {
                        if (rtn == null)
                            rtn = new ArrayList<String>();
                        rtn.add(org.substring(begin, end));
                    }
                    i++;
                }
            } else {
                while (true) {
                    if (i == 0)
                        begin = org.indexOf(prefix, 0);
                    else
                        begin = org.indexOf(prefix, end + 1);
                    if (begin < 0) {
                        // System.out.println("begin:"+begin);
                        break;
                    }
                    end = org.indexOf(suffix, begin + prefix.length());
                    // System.out.println("begin:"+begin+",end:"+end);
                    if (begin < 0 || end < 0)
                        break;
                    begin += prefix.length();
                    if (begin >= 0 && end > 0 && end > begin) {
                        if (rtn == null)
                            rtn = new ArrayList<String>();
                        rtn.add(org.substring(begin, end));
                    }
                    i++;
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return rtn;
    }

    /**
     * 문자열을 바이트로 변경한 후에 변경한 바이트의 시작위치, 종료위치까지의 서브바이트를 계산하여 이를 다시 문자열로 변환하는 함수이다.
     * 바이트수 계산을 위하여 캐릭터셋 인자가 필요함. (기본값은 UTF-8)
     * - 한글바이트수 참고
     *   UTF-8 : 3바이트
     *   EUC-KR : 2바이트
     *   KSC5601 : 2바이트
     *   MS949 : 2바이트
     */
    public static String substringbyte(int from, int to, String charset, String line) {
        String substr = null;
        if(charset==null)
            charset=UTF_8;
        try {
            if(to>=from && line!=null) {
                byte[] inbytes = line.getBytes(charset);
                byte[] subbytes = Arrays.copyOfRange(inbytes, from, to);

                Charset incs = Charset.forName(charset);
                ByteBuffer inbuff = ByteBuffer.wrap(subbytes);
                CharBuffer inchBuf = incs.decode(inbuff);
                substr = inchBuf.toString();
            }
        } catch(Exception e) {
            logger.error("error ***** "+e.getMessage(), e);
        }
        return substr;
    }

    /**
     * 코드성 데이터는 MAP으로 작성하여 convert시에 performance를 고려하도록 한다.
     * ('if else'구문이나 'switch구문'은 코드성데이터(Key-Value쌍)가 많아지면 worst case의 경우 KEY값 비교시 성능이 떨어질 수 있다.
     * 하지만 MAP으로 관리하면 코드가 많아진다고 하더라도 동일한 응답결과를 보장할 수 있다.
     * 대소문자를 구분할 경우에는 일반적인 HashMap을 사용하도록 하고,
     * 대소문자를 구분할 필요가 없는 경우에는 TreeMap을 아래 예제처럼 사용하도록 한다.
     * // convert test : Case sensitive(대소문자 구분)
     * Map<String,String> hmap = new HashMap<String,String>();
     * hmap.put("TCP", "1");
     * hmap.put("UDP", "2");
     * hmap.put("ICMP", "3");
     *
     * EX : convert(hmap, "TCP") --> 1
     *    : convert(hmap, "UDP") --> 2
     *    : convert(hmap, "ICMP") --> 3
     *
     * // convert test : Case insensitive(대소문자 구분X)
     * Map<String,String> tmap = new TreeMap<String,String>(String.CASE_INSENSITIVE_ORDER);
     * tmap.put("TCP", "1");
     * tmap.put("UDP", "2");
     * tmap.put("ICMP", "3");
     *
     * EX : convert(tmap, "Tcp") --> 1
     *      convert(tmap, "tcp") --> 1
     *      convert(tmap, "tcP") --> 1
     *      convert(tmap, "udP") --> 2
     *      convert(tmap, "IcmP") --> 3
     */
    public static String convert(Map<String, String> map, String key) {
        return (String) map.get(key);
    }

    public static String convert(Map<String, String> map, String key, String defaultval) {
        String val = (String) map.get(key);
        if (val != null)
            return val;
        // logger.info(key + " is not mapped to anything");
        return defaultval;
    }

    /**
     * 문자열을 특정 구분자(delimeter)로 쪼개어(split) 필드토큰 리스트로 작성하는 함수는
     * 구글의 guava 라이브러리를 사용하도록 한다. (performance 이슈때문임.)
     * split이후 각 필드별로 trim을 할지여부와 split의 개수를 제한하는 기능을 제공한다.
     * EX : 원문자열 --> 192.168.100.130,FW, Lowest, 20131001110020, Allow,Log,124.57.85.7 -> 178.100.144.118 , Allow ,124.57.85.7, 10, 178.100.144.118, 21, TCP, 21/TCP
     *    : split(true, 0, ',', msgstr)
     *    : split(false, 0, ',', msgstr)
     *    : split(true, 3, ',', msgstr)
     *    : split(false, 3, ',', msgstr)
     */
    public static List<String> split(boolean resulttrim, int limit, char delimeter, String line) {
        List<String> rtn = null;
        if (resulttrim) {
            if (limit > 0)
                rtn = Lists.newArrayList(Splitter.on(delimeter).limit(limit).trimResults().split(line));
            else
                rtn = Lists.newArrayList(Splitter.on(delimeter).trimResults().split(line));
        } else {
            if (limit > 0)
                rtn = Lists.newArrayList(Splitter.on(delimeter).limit(limit).split(line));
            else
                rtn = Lists.newArrayList(Splitter.on(delimeter).split(line));
        }
        return rtn;
    }

    public static List<String> split(boolean resulttrim, int limit, String delimeter, String line) {
        List<String> rtn = null;
        if (resulttrim) {
            if (limit > 0)
                rtn = Lists.newArrayList(Splitter.on(delimeter).limit(limit).trimResults().split(line));
            else
                rtn = Lists.newArrayList(Splitter.on(delimeter).trimResults().split(line));
        } else {
            if (limit > 0)
                rtn = Lists.newArrayList(Splitter.on(delimeter).limit(limit).split(line));
            else
                rtn = Lists.newArrayList(Splitter.on(delimeter).split(line));
        }
        return rtn;
    }

    public static List<String> splitOnPattern(boolean resulttrim, int limit, String pattern, String line) {
        List<String> rtn = null;
        if(resulttrim) {
            if(limit>0)
                rtn = Lists.newArrayList( Splitter.onPattern(pattern).limit(limit).trimResults().split(line) );
            else
                rtn = Lists.newArrayList( Splitter.onPattern(pattern).trimResults().split(line) );
        } else {
            if(limit>0)
                rtn = Lists.newArrayList( Splitter.onPattern(pattern).limit(limit).split(line) );
            else
                rtn = Lists.newArrayList( Splitter.onPattern(pattern).split(line) );
        }
        return rtn;
    }

    /**
     * 문자열을 '특정 길이 문자수 배열'로 쪼개어(split) 필드토큰 리스트로 작성하는 함수는 구글의 guava 라이브러리를 변경하여 사용하도록 한다. (performance 이슈때문임.)
     * EX : 원문자열(msgstr) --> abcdefghijklmnopqrstuvwxyz가나다라마바사1234567890
     *    : split("26,7,10", msgstr)
     *        abcdefghijklmnopqrstuvwxyz
     *        가나다라마바사
     *        1234567890
     *    : split("26,7", msgstr)
     *        abcdefghijklmnopqrstuvwxyz
     *        가나다라마바사
     *        1234567890
     *    : split("26,6,100", msgstr)
     *        abcdefghijklmnopqrstuvwxyz
     *        가나다라마바
     *        사1234567890
     *    : split("26,6", msgstr)
     *        abcdefghijklmnopqrstuvwxyz
     *        가나다라마바
     *        사1234567890
     *    : split("26,6,4", msgstr)
     *        abcdefghijklmnopqrstuvwxyz
     *        가나다라마바
     *        사123
     *        4567890
     */
    public static List<String> split(String indexes, String line) {
        return split(false, indexes, line);
    }

    public static List<String> split(boolean trim, String indexes, String line) {
        List<String> indexeslist = split(true, 0, ",", indexes);
        int indexesarr[] = null;
        if (indexeslist != null) {
            indexesarr = new int[indexeslist.size()];
            for (int i = 0; i < indexeslist.size(); i++) {
                indexesarr[i] = Integer.parseInt(indexeslist.get(i));
            }
        }

        return split(trim, indexesarr, line);
    }

    public static List<String> split(int indexes[], String line) {
        return split(false, indexes, line);
    }

    public static List<String> split(boolean trim, int indexes[], String line) {
        if (indexes != null && line != null) {
            if (trim) {
                return Lists.newArrayList(Splitter.Length(indexes).trimResults().split(line));
            } else {
                return Lists.newArrayList(Splitter.Length(indexes).split(line));
            }
        }
        return null;
    }

    /**
     * 문자열을 '특정 길이 바이트수 배열'로 쪼개어(split) 이를 다시 문자열로 변환한후 필드토큰 리스트로 작성하는 함수이다.
     * 바이트수 계산을 위하여 캐릭터셋 인자가 필요함. (기본값은 UTF-8)
     * - 한글바이트수 참고
     *   UTF-8 : 3바이트
     *   EUC-KR : 2바이트
     *   KSC5601 : 2바이트
     *   MS949 : 2바이트
     */
    public static List<String> splitbyte(String indexes, String charset, String line) {
        return splitbyte(false, indexes, charset, line);
    }

    public static List<String> splitbyte(boolean trim, String indexes, String charset, String line) {
        List<String> indexeslist = split(true, 0, ",", indexes);
        int indexesarr[] = null;
        if (indexeslist != null) {
            indexesarr = new int[indexeslist.size()];
            for (int i = 0; i < indexeslist.size(); i++) {
                indexesarr[i] = Integer.parseInt(indexeslist.get(i));
            }
        }

        return splitbyte(trim, indexesarr, charset, line);
    }

    public static List<String> splitbyte(int indexes[], String charset, String line) {
        return splitbyte(false, indexes, charset, line);
    }

    public static List<String> splitbyte(boolean trim, int indexes[], String charset, String line) {
        List<String> splitList = null;
        if (charset == null)
            charset = UTF_8;
        try {
            int indexsum =0;
            for(int index:indexes) {
                indexsum = indexsum+index;
            }
            // logger.debug("indexsum:"+indexsum);
            if (indexes != null && indexes.length > 0 && line != null) {
                byte[] inbytes = null;
                byte[] linebytes = line.getBytes(charset);
                // logger.debug("line bytes size:"+linebytes.length);
                if(linebytes.length<indexsum) {
                    inbytes = new byte[indexsum];
                    // logger.debug("larger than..");
                    for(int i=0;i<linebytes.length;i++) {
                        inbytes[i] = linebytes[i];
                    }
                    for(int i=linebytes.length;i<indexsum;i++) {
                        inbytes[i] = (char)' ';
                    }
                } else
                    inbytes = line.getBytes(charset);

                // logger.debug("input byte length:"+inbytes.length);
                List<byte[]> bytessplits = splitbyte(indexes, inbytes);
                if (bytessplits != null) {
                    splitList = new ArrayList<String>();
                    Charset incs = Charset.forName(charset);
                    for (byte[] bytes : bytessplits) {
                        ByteBuffer inbuff = ByteBuffer.wrap(bytes);
                        CharBuffer inchBuf = incs.decode(inbuff);

                        if (trim)
                            splitList.add(inchBuf.toString().trim());
                        else
                            splitList.add(inchBuf.toString());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return splitList;
    }

    public static List<byte[]> splitbyte(int indexes[], byte[] in) {
        List<byte[]> byteList = null;
        if(indexes!=null && indexes.length>0 && in!=null && in.length>0) {
            int from = 0;
            int to =0;
            byteList = new ArrayList<byte[]>();
            for(int i=0; i<indexes.length; i++) {
                to += indexes[i];
                byteList.add(Arrays.copyOfRange(in, from, to));
                from = to;
            }
            if(to<in.length) {
                byteList.add(Arrays.copyOfRange(in, from, in.length));
            }
        }
        return byteList;
    }

//    public static String subbyte(boolean trim, int from, String charset, String line) {
//        String substr = null;
//        if (charset == null)
//            charset = UTF_8;
//        try {
//            byte[] inbytes = null;
//            if (line!=null)
//                inbytes = line.getBytes(charset);
//            substr = subbyte(trim, from, inbytes.length-1, charset, line);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//        }
//        return substr;
//    }

    public static String subbyte(boolean trim, int from, int to, String charset, String line) {
        String substr = null;
        if (charset == null)
            charset = UTF_8;
        try {
            byte[] inbytes = null;
            if (line!=null)
                inbytes = line.getBytes(charset);
            if(inbytes!=null && inbytes.length>0 && inbytes.length>=to && from>=0 && to>=from) {
                if(to==from)
                    return "";
                Charset incs = Charset.forName(charset);
                byte[] subbytes = subbyte(from, to, inbytes);
                if(subbytes!=null) {
                    ByteBuffer inbuff = ByteBuffer.wrap(subbytes);
                    CharBuffer inchBuf = incs.decode(inbuff);
                    if(trim)
                        substr = inchBuf.toString().trim();
                    else
                        substr = inchBuf.toString();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return substr;
    }

    public static String subbyte(boolean trim, int from, String charset, byte[] inbytes) {
        String substr = null;
        if (charset == null)
            charset = UTF_8;
        try {
            substr = subbyte(trim, from, inbytes.length-1, charset, inbytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return substr;
    }

    public static String subbyte(boolean trim, int from, int to, String charset, byte[] inbytes) {
        String substr = null;
        if (charset == null)
            charset = UTF_8;
        try {
            if(inbytes!=null && inbytes.length>0 && inbytes.length>=to && from>=0 && to>=from) {
                if(to==from)
                    return "";
                Charset incs = Charset.forName(charset);
                byte[] subbytes = subbyte(from, to, inbytes);
                if(subbytes!=null) {
                    ByteBuffer inbuff = ByteBuffer.wrap(subbytes);
                    CharBuffer inchBuf = incs.decode(inbuff);
                    if(trim)
                        substr = inchBuf.toString().trim();
                    else
                        substr = inchBuf.toString();
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return substr;
    }

    public static byte[] subbyte(int from, byte[] in) {
        byte[] subbyte = null;
        if(in!=null && in.length>0 && in.length>from) {
            subbyte = Arrays.copyOfRange(in, from, in.length);
        }
        return subbyte;
    }

    public static byte[] subbyte(int from, int to, byte[] in) {
        byte[] subbyte = null;
        if(in!=null && in.length>0 && in.length>=to && to>from) {
            subbyte = Arrays.copyOfRange(in, from, to);
        }
        return subbyte;
    }

    /**
     * getSelectedArrange 함수에서 구한 start, end 조합의 position정보를 통해
     * 입력 string 내의 표시 위치를 변경한다.
     * EX : rearrange("20131107 16:03:50.234Z", "6,7,4,5,0,3,9,10,12,13,15,16,18,20") --> 07112013160350234
     *    이후에 format("07112013160350234", "ssassassssassassassasss","// ::.") --> 07/11/2013 16:03:50.234
     *    위의 두 단계에 의해 위치가 변경된 포맷을 처리한다.
     */
    public static String rearrange(String str, int[] fmt) {
        StringBuffer outb = new StringBuffer();
        // System.out.println(fmtLength);

        for (int i = 0; i < fmt.length; i++) {
            // System.out.println("656 : "+outb.toString()+"fmt["+i+"] : "+ fmt[i]+" ----string length : "+ str.length()+"\n");
            if (fmt[i] > str.length() - 1)
                return outb.toString();
        }
        for (int i = 0; i < fmt.length; i = i + 2) {
            outb.append(str.substring(fmt[i], fmt[i + 1] + 1));
            // System.out.println(outb.toString()+"\n");
        }
        return outb.toString();
    }

    /**
     * 두 '문자열'(instr:입력문자열, convstr:변환문자열)내의 모든 문자들을 각각 비교하여 동일한 문자의 idx를 뽑아낸다.
     * (s:same 문자같음, a:append 문자추가됨, r:remove 문자삭제됨)
     * EX : getSelectedPosition("20131107 16:03:50.234Z", "2013")
     *    : 0,3 (0 : 시작 idx, 3: 끝 idx)
     */
    public static String getSelectedPosition(String instr, String selectstr) {
        StringBuffer outb = new StringBuffer();
        char[] instrChar = instr.toCharArray();
        char[] selectstrChar = selectstr.toCharArray();
        boolean stop = false;
        String Start = null;
        String End = null;
        for (int i = 0; i < instrChar.length; i++) {
            if (selectstrChar.length > (instrChar.length - i))
                break;

            if (instrChar[i] == selectstrChar[0]) {
                for (int j = 0; j < selectstrChar.length; j++) {
                    if (instrChar[i + j] != selectstrChar[j]) {
                        break;
                    }
                    if (j == selectstrChar.length - 1) {
                        Start = Integer.toString(i);
                        End = Integer.toString(i + j);
                        outb.append(Start);
                        outb.append(",");
                        outb.append(End);
                        stop = true;
                    }
                }
            }
        }
        if (stop)
            return outb.toString();
        else
            return "none";
    }

    /**
     * 문자열에서 키(key) 값(value)를 구분하여 Map 데이터를 작성하여 리턴하는 함수이다. 구글의 guava 라이브러리를 변경하여 사용한다.
     * (예외 처리등에 대하여 변경하였음. 구글에서는 key value가 쌍으로 제대로 있지않으면 에러를 리턴하고 종료하는데, 변경한 부분은 에러를 무시하고 계속처리하도록 처리함. )
     * 문자열을 특정 구분자(split delimeter)로 쪼개고(split), trim을 수행한 후 쪼개어진 부분에 대하여  키값구분자(kvseparator)를 사용하여
     * 키(key) 값(value)을 나눈후 이를 Map에 저장하는 방식이다.
     * EX : 원문자열(str) --> date=2014/07/18 11:20:17,    name=홍길동,    e-mail=hongkildong@www.company.com,    phone=010-1234-5678,    department=연구소,    comment=this is hong. nice to see you all
     *    : keyvalue(",", "=", true, str)
     *      map에 저장되는 key:value 결과 -->
     *          date:2014/07/18 11:20:17
     *          name:홍길동
     *          e-mail:hongkildong@www.company.com
     *          phone:010-1234-5678
     *          department:연구소
     *          comment:this is hong. nice to see you all
     */
    public static Map<String, String> keyvalue(String splitdelimeter, String kvseparator, boolean trim, String s) {
        Map<String, String> kvmap = null;

        s = replaceBetweenQuotations(replaceBetweenQuotations(s, splitdelimeter, "^^"), kvseparator, "vv");

        if (trim)
            kvmap = Splitter.on(splitdelimeter).trimResults().withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv").split(s);
        else
            kvmap = Splitter.on(splitdelimeter).withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv").split(s);

        return kvmap;
    }

    public static Map<String, String> keyvalue(String splitdelimeter, String kvseparator, String subss, String subse, boolean trim, String s) {
        Map<String, String> kvmap = null;

        s = replaceBetweens(replaceBetweens(s, subss, subse, splitdelimeter, "^^"), subss, subse, kvseparator, "vv");

        if (trim)
            kvmap = Splitter.on(splitdelimeter).trimResults().withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv").split(s);
        else
            kvmap = Splitter.on(splitdelimeter).withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv").split(s);

        return kvmap;
    }

    /**
     * 다음 예제에서는 원문자열의 키값쌍중에서 키가 date, name, phone인 것에 대한 값만을 구하여 Map으로 저장한다.
     * EX : 원문자열(str) --> date=2014/07/18 11:20:17,    name=홍길동,    e-mail=hongkildong@www.company.com,    phone=010-1234-5678,    department=연구소,    comment=this is hong. nice to see you all
     *    : keyvalue(",", "=", true, str, "date","name","phone")
     *      map에 저장되는 key:value 결과 -->
     *          date:2014/07/18 11:20:17
     *          name:홍길동
     *          phone:010-1234-5678
     *    : keyvalue(",", "=", true, str, "name","phone","comment","e-mail")
     *      map에 저장되는 key:value 결과 -->
     *          name:홍길동
     *          phone:010-1234-5678
     *          comment:this is hong. nice to see you all
     *          e-mail:hongkildong@www.company.com
     */
    public static Map<String, String> keyvalue(String splitdelimeter, String kvseparator, boolean trim, String s, String... keys) {
        Map<String, String> kvmap = null;

        s = replaceBetweenQuotations(replaceBetweenQuotations(s, splitdelimeter, "^^"), kvseparator, "vv");

        if (trim)
            kvmap = Splitter.on(splitdelimeter).trimResults().withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv", keys).split(s);
        else
            kvmap = Splitter.on(splitdelimeter).withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv", keys).split(s);

        return kvmap;
    }

    public static Map<String, String> keyvalue(String splitdelimeter, String kvseparator, String subss, String subse, boolean trim, String s, String... keys) {
        Map<String, String> kvmap = null;

        s = replaceBetweens(replaceBetweens(s, subss, subse, splitdelimeter, "^^"), subss, subse, kvseparator, "vv");

        if (trim)
            kvmap = Splitter.on(splitdelimeter).trimResults().withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv", keys).split(s);
        else
            kvmap = Splitter.on(splitdelimeter).withKeyValueSeparator(kvseparator, splitdelimeter, "^^", kvseparator, "vv", keys).split(s);

        return kvmap;
    }

    /**
     * 예를들어, 원문자열에 다음과 같을 때,
     * 원문자열(str) : id=firewall time="2014-07-22 13:25:06" fw=(none) pri=7 rule=53 proto=53/udp src=192.168.100.80 dst=8.8.8.8 sent=309 rcvd=0 duration=0 msg="source interface = Internal"
     * 따옴표(")로 둘러쌓인 서브문자열 --> "2014-07-22 13:25:06"과 "source interface = Internal"의 " "를 "^^"로 바꾸고 싶으면 다음처럼 하면 된다.
     *   : replaceBetweenQuotations(str, " ", "^^")
     * 결과 : id=firewall time="2014-07-22^^13:25:06" fw=(none) pri=7 rule=53 proto=53/udp src=192.168.100.80 dst=8.8.8.8 sent=309 rcvd=0 duration=0 msg="source^^interface^^=^^Internal"
     */
    public static String replaceBetweenQuotations(String source, String os, String ns) {
        if (source == null)
            return null;

        char[] sourceArray = source.toCharArray();
        char[] nsArray = ns.toCharArray();
        int oLength = os.length();
        StringBuffer buf = new StringBuffer();

        int i = 0, j = 0, s = 0, e = 0;
        s = source.indexOf("\"", s);
        e = source.indexOf("\"", s + 1);

        while (s >= 0 && e >= 0 && e > s) {
            i = source.indexOf(os, s);
            while (i > s && i < e) {
                // System.out.println("s:"+s+", i:"+i+", e:"+e);
                buf.append(sourceArray, j, i - j).append(nsArray);
                i += oLength;
                j = i;
                i = source.indexOf(os, j);
            }

            s = e + 1;
            s = source.indexOf("\"", s);
            e = source.indexOf("\"", s + 1);
        }
        buf.append(sourceArray, j, sourceArray.length - j);

        return buf.toString();
    }

    /** 문자열내부에 시작문자(열)과 종료문자(열)로 둘러쌓인 서브문자열들 내의 문자들을 다른 문자로 변경하고자 할때 사용한다. */
    /**
     * 예를들어, 원문자열에 다음과 같을 때,
     * 원문자열(str) : id=firewall time=[2014-07-22 13:25:06] fw=(none) pri=7 rule=53 proto=53/udp src=192.168.100.80 dst=8.8.8.8 sent=309 rcvd=0 duration=0 msg=[source interface = Internal]
     * 시작문자"["와 종료문자"]"로 둘러쌓인 서브문자열 --> [2014-07-22 13:25:06]과 [source interface = Internal]의 공백( )을 "^^"로 바꾸고 싶으면 다음처럼 하면 된다.
     *   : replaceBetweens(str, "[", "]", " ", "^^")
     * 결과 : id=firewall time=[2014-07-22^^13:25:06] fw=(none) pri=7 rule=53 proto=53/udp src=192.168.100.80 dst=8.8.8.8 sent=309 rcvd=0 duration=0 msg=[source^^interface^^=^^Internal]
     */
    public static String replaceBetweens(String source, String ss, String es, String os, String ns) {
        if (source == null)
            return null;

        char[] sourceArray = source.toCharArray();
        char[] nsArray = ns.toCharArray();
        int oLength = os.length();
        StringBuffer buf = new StringBuffer();

        int i = 0, j = 0, s = 0, e = 0;
        s = source.indexOf(ss, s);
        e = source.indexOf(es, s + 1);

        while (s >= 0 && e >= 0 && e > s) {
            i = source.indexOf(os, s);
            while (i > s && i < e) {
                // System.out.println("s:"+s+", i:"+i+", e:"+e);
                buf.append(sourceArray, j, i - j).append(nsArray);
                i += oLength;
                j = i;
                i = source.indexOf(os, j);
            }

            s = e + 1;
            s = source.indexOf(ss, s);
            e = source.indexOf(es, s + 1);
        }
        buf.append(sourceArray, j, sourceArray.length - j);

        return buf.toString();
    }

    /** 문자열내부에 시작문자(열)과 종료문자(열)로 둘러쌓인 서브문자열들 전체를 다른 문자로 변경하고자 할때 사용한다. */
    /**
     * 예를들어, 원문자열에 다음과 같을 때,
     * 원문자열(str) : 08:EB:74:07:FF:78||0||633||2016-03-31 22:check{0,1,2,3,4,5,6}:00||430||check{7,8,9,10}||UC1300X
     * 시작문자"check{"와 종료문자"}"로 둘러쌓인 서브문자열 --> check{0,1,2,3,4,5,6}과 check{7,8,9,10} 문자열들 모두를 "new string"로 바꾸고 싶으면 다음처럼 하면 된다.
     *   : replaceBetweens(str, "check{", "}", "new string")
     * 결과 : 08:EB:74:07:FF:78||0||633||2016-03-31 22:new string:00||430||new string||UC1300X
     */
    public static String replaceBetweens(String source, String ss, String es, String ns) {
        if(source==null || ss==null || es==null)
            return null;

        char[] sourceArray = source.toCharArray();
        char[] nsArray = ns.toCharArray();
        StringBuffer buf = new StringBuffer();

        int i=0, s=0, e=0;
        s = source.indexOf(ss, s);
        e = source.indexOf(es, s+1);

        while( s>=0 && e>=0 && e>s) {
            // System.out.println("s:"+s+", i:"+i+", e:"+e);
            buf.append(sourceArray, i, s-i).append(nsArray);
            i = e+es.length();
            s = e+1;
            s = source.indexOf(ss, s);
            e = source.indexOf(es, s+1);
        }
        buf.append (sourceArray, i, sourceArray.length - i);

        return buf.toString();
    }

    /** 문자열내부에 시작문자열과 종료문자열을 포함하는 부분을 제거한다. */
    /**
     * EX : 원문자열(str) --> rf1c2{email}f1{phone}f0
     *     removeBetween(str, "{", "}")의 결과 --> rf1c2f1f0
     */
    public static String removeBetween(String source, String ss, String es) {
        if (source == null)
            return null;

        char[] sourceArray = source.toCharArray();
        StringBuffer buf = new StringBuffer();

        int i = 0, s = 0, e = 0;
        s = source.indexOf(ss, s);
        e = source.indexOf(es, s + 1);

        while (s >= 0 && e >= 0 && e > s) {
            buf.append(sourceArray, i, s - i);
            i = e + es.length();
            s = e + es.length();
            s = source.indexOf(ss, s);
            e = source.indexOf(es, s + 1);
        }
        buf.append(sourceArray, i, sourceArray.length - i);

        return buf.toString();
    }

    public static String removeAfter(int option, String source, String cs) {
        String rmstr = null;
        if(source == null)
            return null;
        if(cs==null)
            return source;
        int cidx = source.indexOf(cs);
        if(cidx!=-1) {
            if(option == REMOVE_INCLUDE) {
                rmstr = source.substring(0, cidx);
            }
            if(option == REMOVE_EXCLUDE) {
                rmstr = source.substring(0, cidx+cs.length());
            }
        }
        return rmstr;
    }


    public static boolean isNumber(String str){
        boolean result = false;

        try {
            Double.parseDouble(str) ;
            result = true ;
        } catch(Exception e){logger.error("error ***** {}", e.getMessage());}

        return result ;
    }

    public static boolean isNumeric(char c) {
        if (c!='.' && !Character.isDigit(c))
            return false;
        return true;
    }

    public static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (c!='.' && !Character.isDigit(c)) return false;
        }
        return true;
    }

    /** masking 처리 */
    public static String masking(String maskfmt, String s) {
        List<String> fmtlist = split(false, 2, '|', maskfmt);
        if(fmtlist!=null && fmtlist.size()==2) {
            try {
                int idx = Integer.parseInt(fmtlist.get(0));
                return masking(idx, fmtlist.get(1), s);
            } catch(Exception e) {
                logger.error("error ***** {}", e.getMessage());
            }
        }
        return s;
    }

    /**
     * masking 처리
     * 원문자열 str : 12345678901234567890abcdefghijklmnopqrstuvwxys
     * masking(5, "*", str) --> 12345*****************************************
     * masking(5, "abc", str) --> 12345abcabcabcabcabcabcabcabcabcabcabcabcabcab
     */
    public static String masking(int idx, String maskstr, String s) {
        if(s==null || s.length()<=idx)
            return s;
        char[] mask = maskstr.toCharArray();
        char[] arr = s.toCharArray();
        int j=0;
        for (int i = idx; i < arr.length; i++) {
            arr[i] = mask[j];
            j++;
            if(j>=mask.length)
                j=0;
        }
        return new String(arr, 0, arr.length);
    }



    /**
     * 입력 값의 길이를 리턴한다.
     */
    public static String length(String countType, String charset, String inputValue) {
        String ret = null;

        try {
            if (inputValue == null || inputValue.equals(""))
                ret = "0";
            else {
                if (countType != null && countType.equalsIgnoreCase("char"))
                    ret = Integer.toString(inputValue.length());
                else if (countType != null && countType.equalsIgnoreCase("byte") && charset != null) {
                    ret = Integer.toString(inputValue.getBytes(charset).length);
                }
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("error ***** "+e.getMessage(), e);
        }
        return ret;
    }



    public static String toPrettyJson(Object obj) {
        String json = "";
        try {
            if(obj!=null) {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                json = ow.writeValueAsString(obj);
            }
        } catch(Exception e) {
            logger.error("error ***** "+e.getMessage(), e);
        }
        return json;
    }

    /**
     * 리스트를 정의한 구분자 문자열로 변환
     *
     * @param collection 리스트
     * @param separator 구분자
     * @return 문자열
     */
    public static String join(Collection collection, char separator) {
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (final Object object : collection) {
            if (!first) {
                sb.append(separator);
            } else {
                first = false;
            }
            sb.append(object);
        }
        return sb.toString();
    }

    public static String join(Iterator iterator, String separator) {

        // handle null, zero and one elements before building a buffer
        if (iterator == null) {
            return null;
        }
        if (!iterator.hasNext()) {
            return "";
        }
        Object first = iterator.next();
        if (!iterator.hasNext()) {
            return first == null?"":first.toString();
        }

        // two or more elements
        StringBuffer buf = new StringBuffer(256); // Java default is 16, probably too small
        if (first != null) {
            buf.append(first);
        }

        while (iterator.hasNext()) {
            if (separator != null) {
                buf.append(separator);
            }
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
        }
        return buf.toString();
    }

    public static String join(Collection collection, String separator) {
        if (collection == null) {
            return null;
        }
        return join(collection.iterator(), separator);
    }


    public static int countOccurrences(String input, String word) {
        int count = 0;
        Pattern pattern = Pattern.compile(word);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    /**
     * 문자열의 charset을 변경하는 convert함수.
     * String ms949str = convert(orgstr, "MS949");
     * String utf8str = convert(orgstr, "UTF-8");
     * String cp933str = convert(orgstr, "CP933");
     * String euckrstr = convert(orgstr, "EUC-KR");
     */
    public static String convert(String str, String encoding) throws IOException {
        ByteArrayOutputStream requestOutputStream = new ByteArrayOutputStream();
        requestOutputStream.write(str.getBytes(encoding));
        return requestOutputStream.toString(encoding);
    }

    /**
     * 상수형식의 문자열에서 특정형식({0},{1},..)의 문자를 원하는 파라미터로 변환한다.
     * formatStr : ex) "/site/collect_fwrk/{0}/{1}/idx"
     *             {0},{1},... --> 0부터 시작해야한다.
     * 사용법
     *    getFormatMessage(formatStr, "node", "sn1.BOMSOFTWARE.com")
     *    --> /site/collect_fwrk/node/sn1.BOMSOFTWARE.com/idx
     *    {0}는 "node"로 치환되고, {1}은 "sn1.BOMSOFTWARE.com"으로 치환된다.
     */
    public static String getFormatMessage(String formatStr, Object...param ) {
        String compStr = MessageFormat.format(formatStr.replace("'", "''"), param);
        //logger.debug("==============================================");
        //logger.debug(" getFormatMessage :: "+ compStr);
        //logger.debug("==============================================");
        return compStr;
    }

    public static String getFormatMessage(String formatStr, List<String> strlist) {

        String compStr = null;
        if(strlist.size()>0) {
            compStr = MessageFormat.format(formatStr, strlist.toArray());
        }

        return compStr;
    }


    public static String getDateNumberStr(String fullDateStr) {
        if(fullDateStr==null)
            return "";
        if(fullDateStr.length()<=0)
            return "";
        IntStream stream = fullDateStr.chars();
        return stream.filter((ch)-> (48 <= ch && ch <= 57))
                .mapToObj(ch -> (char)ch)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
