package com.example.demo.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class StringDateUtils extends org.apache.commons.lang.StringUtils {

	private static Logger logger = LoggerFactory.getLogger(StringDateUtils.class);
	//安装目录
	//@Value("#{ @environment['app.collect.untarpath'] ?: T(java.lang.System).getProperty('com.example.demo.user.dir')}")
	@Value("#{T(java.lang.System).getProperty('user.dir')}")
	private String installDir;

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String format_senconde = "yyyy-MM-dd HH:mm:ss";
	private static Calendar startDate = Calendar.getInstance();
	private static Calendar endDate = Calendar.getInstance();
	private static DateFormat df = DateFormat.getDateInstance();
	private static Date earlydate = new Date();
	private static Date latedate = new Date();

	/**
	 * 判断操作系统类型
	 * @param
	 * @return
	 */
	public static boolean isWindow(){
		boolean flag = false;
		String os = System.getProperty("os.name");
		if(os.toLowerCase().startsWith("window")){
			flag = true;
		}
		return flag;
	}
	/**
	 * 获取安装路径
	 * @param
	 * @return
	 */
	public static String ffaInstallPath(){
		return System.getProperty("com.example.demo.user.dir");
	}
	/**
	 * 获取安装路径
	 * @param
	 * @return
	 */
	public static String ffaInstallOs(){
		return System.getProperty("os.name");
	}
	/**
	 * 得到全球唯一标示
	 * @return
	 */
	public static String getUUID(){
		String returnStr = "";
		returnStr = UUID.randomUUID().toString().replaceAll("-", "");
		return returnStr;
	}

	/**
	 * 判断字符串为空
	 * @param value
	 * @return
	 */
	public static boolean isBlank(String value){
		boolean flag = false;
		if(value == null || value.trim().equals("null") || value.trim().equals("")){
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断字符串不为空
	 * @param value
	 * @return
	 */
	public static boolean isNotBlank(String value){
		boolean flag = false;
		if(!isBlank(value)){
			flag = true;
		}
		return flag;
	}

	/**
	 * 对象转Json
	 * @param data
	 * @return
	 */
	public static String objToJson(Object data){

		ObjectMapper mapper = new ObjectMapper();
		String returnValue = "";
		try {
			returnValue = mapper.writeValueAsString(data);
//			logger.info("json up = "+returnValue);
		} catch (JsonProcessingException e) {
			logger.error("json转换失败"+e.getMessage(),e);
		}
		return returnValue;
	}

	/**
	 * List<Integer>数组排序
	 * @param list
	 * @return
	 */
	public List<Integer> listOrderBy(List<Integer> list){
		Collections.sort(list);
		return list;
	}


	/**
	 * 比较两个字符串日期的先后顺序
	 * @param stime
	 * @param etime
	 * @return
	 */
	public static boolean compareStrDate(String stime,String etime) {
		boolean flag = false;
		try {
			Date sDate = strToDate(stime, format_senconde);
			Date eDate = strToDate(etime, format_senconde);
			if(sDate.before(eDate)) {
				flag = true;
			}
		} catch (Exception e) {
			logger.error("字符串两日期比较异常"+e.getMessage(),e);
		}
		return flag;
	}

	/**
	 * 计算两个时间相差多少个年
	 *
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int yearsBetween(String start, String end) throws ParseException {
		startDate.setTime(sdf.parse(start));
		endDate.setTime(sdf.parse(end));
		return (endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR));
	}
	/**
	 * 计算两个时间相差多少个月
	 *
	 * @param start
	 *            <String>
	 * @param end
	 *            <String>
	 * @return int
	 * @throws ParseException
	 */
	public static int getMonthSpace(String start, String end) throws ParseException {
		int result =  0;
		try {
			startDate.setTime(sdf.parse(start));
			endDate.setTime(sdf.parse(end));
			result = yearsBetween(start, end) * 12 + endDate.get(Calendar.MONTH) - startDate.get(Calendar.MONTH);
		} catch (Exception e) {
			logger.error(""+e.getMessage(),e);
		}
		return result == 0 ? 1 : Math.abs(result);

	}
	/**
	 * 计算两个时间相差多少个天
	 *
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String start, String end) throws ParseException {
		// 得到两个日期相差多少天
		return hoursBetween(start, end) / 24;
	}
	/**
	 * 计算两个时间相差多少小时
	 *
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int hoursBetween(String start, String end) throws ParseException {
		// 得到两个日期相差多少小时
		return minutesBetween(start, end) / 60;
	}

	/**
	 * 计算两个时间相差多少分
	 *
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int minutesBetween(String start, String end) throws ParseException {
		// 得到两个日期相差多少分
		return secondesBetween(start, end) / 60;
	}

	/**
	 * 计算两个时间相差多少秒
	 *
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	 */
	public static int secondesBetween(String start, String end) throws ParseException {
		earlydate = df.parse(start);
		latedate = df.parse(end);
		startDate.setTime(earlydate);
		endDate.setTime(latedate);
		// 设置时间为0时
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		endDate.set(Calendar.HOUR_OF_DAY, 0);
		endDate.set(Calendar.MINUTE, 0);
		endDate.set(Calendar.SECOND, 0);
		// 得到两个日期相差多少秒
		return ((int) (endDate.getTime().getTime() / 1000) - (int) (startDate.getTime().getTime() / 1000));
	}

	/**
	 * 字符串转日期
	 * @param strDate
	 * @param pattern
	 * @return
	 */
	public static Date strToDate(String strDate,String pattern) {
		SimpleDateFormat formatter = new SimpleDateFormat(pattern);
		Date strtodate = null;
		try {
			strtodate = formatter.parse(strDate);
		} catch (ParseException e) {
			logger.error("日期获取异常"+e.getMessage(),e);
		}
		return strtodate;
	}
	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String dateToStr(Date date,String pattern) {
		String dateString = "";
		if (date!=null) {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			dateString = formatter.format(date);
		}
		return dateString;
	}
	/**
	 * 日期推算
	 * 正数 向前推   负数向后推
	 * @param
	 * @param
	 * @return
	 */
	public static String dateToCalculate(String dateStr,int hour) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(strToDate(dateStr, format_senconde));
			calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY)-hour);
			return dateToStr(calendar.getTime(), format_senconde);
		} catch (Exception e) {
			logger.error(dateStr+"日期推算异常"+e.getMessage(),e);
		}
		return null;
	}

	/**
	 * String类型date转Long类型
	 * @param dateStr
	 * @return
	 */
	public static Long strngDateToLong(String dateStr){
		Long returnVal = null;

		if(isBlank(dateStr)){
			return returnVal;
		}
		try {
			Date date = sdf.parse(dateStr);
			if(date != null){
				returnVal = date.getTime();
			}
		} catch (Exception e) {
			logger.error("日期转long "+e.getMessage(),e);
		}

		return returnVal;
	}
	/**
	 * Date转Long类型
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date){
		long returnVal = 0l;

		if(date == null){
			return returnVal;
		}
		try {
			returnVal = date.getTime();
		} catch (Exception e) {
			logger.error("日期转long "+e.getMessage(),e);
		}
		return returnVal;
	}

	/**
	 * 由Mon Sep 19 18:25:10 CST 2011转成格式化日期
	 * @param
	 * @param
	 * @return
	 */
	public static String parseToDate(String parseTime) {
		SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
		Date date;
		String newStartTime="";
		try {
			date = (Date) sdf.parse(parseTime);
			newStartTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		} catch (ParseException e) {
			logger.error("日期转换异常"+e.getMessage(),e);
			return parseTime;
		}
		return newStartTime;
	}


	/**
	 * 生成指定位数的随机码
	 *
	 * */
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };
	public static String getShortUuidByNums(int num) {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < num; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}


	/**
	 * 随机生成指定位数数字字符串
	 * @param num
	 * @return
	 */
	public static String getStringRandNum(int num){
		String returnStr="";
		char[] str = "0123456789".toCharArray();
		for (int i = 0; i < num; i++) {
			int indexnumber = (int) (Math.random() * str.length);
			returnStr += str[indexnumber];
		}
		return returnStr;
	}
	/**
	 * 随机生成指定位数数字字符串
	 * @param num   返回几位
	 * @param
	 * @return
	 */
	public static String getRandNum(int num,int length){
		String returnStr="";
		if(length>10) {
			return null;
		}
		String  s1 = "0123456789";
		s1 = s1.substring(0,length);
		char[] str = s1.toCharArray();
		for (int i = 0; i < num; i++) {
			int indexnumber = (int) (Math.random() * str.length);
			returnStr += str[indexnumber];
		}
		return returnStr;
	}


	/**
	 * list删除重复对象
	 * @param list
	 * @return
	 */
	public static List listDistinct(List list) {
		HashSet h = new HashSet(list);
		List ll = new ArrayList();
		ll.addAll(h);
		return ll;
	}
	/**
	 * //文件类型\n1图片 \n2视频  \n3文档  \n4音频  \n5其他\n
	 * @param name
	 * @return
	 */
	public static int getFileTypeByFileName(String name){
		int type = -1;
		String img [] = {"WEBP","BMP","PCX","TIFF","TIF","GIF","JPEG","JPG","TGA","EXIF","FPX","SVG","PSD","CDR","PCD","PIC","DXF"
				,"UFO","EPS","AI","PNG","HDRI","RAW","WMF","FLIC","EMF","ICO"};
		String video [] = {"3GP","3G2","AVI","DV","DIF","MOV","QT","SWF","MP2","OGG","AAC","M4A","NUT"
				,"AC3","H261","H264","M4V","YUV","MP4","RA","RAM","RMVB","WMV"
				,"MPG","MPEG","MPA","VOB","DAT","CUE","MKV","AIFF","AU","CDA","AVS"
				,"PSP","SMK","NSV"};
		String doc [] = {"RTF","HLP","TXT","PDF","DOCX","DOC","XLS","XLSX","PPT","PPTX","PUB","MPP","TF","HTM","HTML","WPD","DOCM","DOTX","DOTM","DOT"
				,"XPS","MHT","MHTML","XML","ODT","WTF","WPS"};
		String audio [] = {"CD","OGG","MP3","ASF","WMA","WAV","MP3PRO","RM","REAL","APE","MODULE","MIDI","VQF"};

		try {
			if(name.indexOf(".") != -1 ){
				name = name.substring(name.indexOf(".")+1);
				if(isNotBlank(name)){
					if(ArrayUtils.contains(img, name.toUpperCase())){
						type = 1;
					}else if(ArrayUtils.contains(video, name.toUpperCase())){
						type = 2;
					}else if(ArrayUtils.contains(doc, name.toUpperCase())){
						type = 3;
					}else if(ArrayUtils.contains(audio, name.toUpperCase())){
						type = 4;
					}else{
						type = 5;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return type;
	}
	//生成随机颜色
	public static String getRandColorCode(){
		String r,g,b;
		Random random = new Random();
		r = Integer.toHexString(random.nextInt(256)).toUpperCase();
		g = Integer.toHexString(random.nextInt(256)).toUpperCase();
		b = Integer.toHexString(random.nextInt(256)).toUpperCase();

		r = r.length()==1 ? "0" + r : r ;
		g = g.length()==1 ? "0" + g : g ;
		b = b.length()==1 ? "0" + b : b ;

		return r+g+b;
	}


	public static void main(String[] args) throws NoSuchAlgorithmException {
		System.out.println(ffaInstallPath());
	}


}
