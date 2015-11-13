package util;

import com.google.common.base.Joiner;

import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@SuppressWarnings("rawtypes") 
public class StringUtil {
	public static boolean isNotNull(String str) {
		return (!StringUtil.isNull(str));
	}
	public static boolean isNull(String str) {
		return (str == null || str.length() == 0 || "null".equals(str.toLowerCase()));
	}
	
	public static boolean isNotEmpty(Collection collection) {
		return (!StringUtil.isEmpty(collection));
	}
	public static boolean isEmpty(Collection collection) {
		return (collection == null || collection.size() == 0);
	}
	
	public static boolean isNotEmpty(Map map) {
		return (!StringUtil.isEmpty(map));
	}
	public static boolean isEmpty(Map map) {
		return (map == null || map.size() == 0);
	}
	public static String getTimePreHours(int i) {
		Calendar calendar = Calendar.getInstance();
//	　　　　/* HOUR_OF_DAY 指示一天中的小时 */
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - i);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(i +"个小时前的时间：" + df.format(calendar.getTime()));
		System.out.println("当前的时间：" + df.format(new Date()));
		return df.format(calendar.getTime());
		
	}
	public final static String getDigest(String content) {  
    	String sdesc = "";
    	content = StringUtil.trimLeft(StringUtil.guoHtml(content));
    	if (StringUtil.isNotNull(content)) {
    		if (content.length() > 200) {
    			sdesc = content.substring(0, 200);
    		} else {
    			sdesc = content;
    		}
    	}
		return sdesc;
    }
	public static String guoHtml(String s){
		if(!s.equals("")||s!=null){
			String str=s.replaceAll("<[.[^<]]*>","");
			return str;
		}else{
			return s;
		}
	}
	public static String trimLeft(String s) {
		while (StringUtil.isNotNull(s) && " ".endsWith(s.substring(0, 1))) s = s.substring(1);
		return s;
	}
	public static Long getDateDiff(String start_day, String end_day) {
		Long iminus = 0l;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = formatter.parse(start_day);
			Date date2 = formatter.parse(end_day);
			if ((date2.getTime() - date1.getTime())% (1000 * 60 * 60 * 24) == 0) 
				iminus = (date2.getTime() - date1.getTime())/(1000 * 60 * 60 * 24);
			else
				iminus = (date2.getTime() - date1.getTime())/(1000 * 60 * 60 * 24) + 1;
//			System.out.println(iminus);
		}catch(Exception e) {
			
		}
		return iminus;
	}
	public static Long getDateDiff(String start_day, Date end_day) {
		Long iminus = 0l;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Date date1 = formatter.parse(start_day);
			Date date2 = formatter.parse(formatter.format(end_day));
			if ((date2.getTime() - date1.getTime())% (1000 * 60 * 60 * 24) == 0) 
				iminus = (date2.getTime() - date1.getTime())/(1000 * 60 * 60 * 24);
			else
				iminus = (date2.getTime() - date1.getTime())/(1000 * 60 * 60 * 24) + 1;
//			System.out.println(iminus);
		}catch(Exception e) {
			
		}
		return iminus;
	}
	/** 
	* 获得指定日期的前几天 或 后几天 （正数是后，负数是前） 
	* @param specifiedDay 
	* @return 
	* @throws Exception 
	*/ 
	public static String getSpecifiedDay(Date date, int iday){ 
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance(); 
		c.setTime(date); 
		int day=c.get(Calendar.DATE); 
		c.set(Calendar.DATE,day + iday); 
		String dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
		return dayBefore; 
	}
	/** 
	* 获得指定日期的前几天 或 后几天 （正数是后，负数是） 
	* @param specifiedDay 
	* @return 
	* @throws Exception 
	*/ 
	public static String getSpecifiedDay(String specifiedDay, int iday){ 
		//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
		Calendar c = Calendar.getInstance(); 
		Date date=null; 
		String dayBefore=null;
		try { 
			date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay); 
			c.setTime(date); 
			int day=c.get(Calendar.DATE); 
			c.set(Calendar.DATE,day + iday); 
			dayBefore=new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()); 
		} catch (ParseException e) { 
			e.printStackTrace(); 
		} 
		return dayBefore; 
	} 
	public static String getStrFromList(List<String> list) {
		if (list == null || list.size() == 0) return "";
		String res = "";
		for (String str : list) {
			res += str +",";
		}
		if (res.length() > 0) res = res.substring(0, res.length() - 1);
		return res;
	}
	public static String getDateStr(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		if (date == null) {
			date = new Date();
		}
		return df.format(date);
	}
	public static List<String> addItem(List<String> list, String key) {
		if (list == null) {
			list = new ArrayList<String>();
			if (StringUtil.isNotNull(key)) list.add(key); 
		}
		if (!list.contains(key)) list.add(key);
		return list;
	}
	/**
     * 判断字符串是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符串是否不为空
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }
    public static String tagsSort(String tags, String separator) {
        if(null == tags){
            return null;
        }
        if(null == separator){
            separator = ",";
        }
        Comparator<Object> cmp = Collator.getInstance(java.util.Locale.CHINA);

        String[] temp = tags.split(separator);
        String[] clear = new String[temp.length];

        for(int i = 0; i < temp.length; i++){
            clear[i] = temp[i].toLowerCase().trim();
        }
        Arrays.sort(clear, cmp);
        return Joiner.on(separator).join(clear);
    }
}
