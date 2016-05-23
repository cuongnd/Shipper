package org.csware.ee.utils.pinyin;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.utils.sort.HashList;
import org.csware.ee.utils.sort.KeySort;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AssortPinyinList {

	private HashList<String,String> hashList=new HashList<String,String>(new KeySort<String,String>(){
		public String getKey(String value) {
			return getFirstChar(value);
		}});

	//获得字符串的首字母 首字符 转汉语拼音
	public  String getFirstChar(String value) {
		// 首字符
		char firstChar = 0;
		if (!Guard.isNullOrEmpty(value)) {
			firstChar = value.charAt(0);
		}
		// 首字母分类
		String first = null;
		// 是否是非汉字
		String[] print = PinyinHelper.toHanyuPinyinStringArray(firstChar);

		if (print == null) {

			// 将小写字母改成大写
			if ((firstChar >= 97 && firstChar <= 122)) {
				firstChar -= 32;
			}
			if (firstChar >= 65 && firstChar <= 90) {
				first = String.valueOf((char) firstChar);
			} else {
				// 认为首字符为数字或者特殊字符
				first = "#";
			}
		} else {
			// 如果是中文 分类大写字母
			first = String.valueOf((char)(print[0].charAt(0) -32));
		}
		if (first == null) {
			first = "?";
		}
		return first;
	}

	public HashList<String, String> getHashList() {
		return hashList;
	}

	/**
	 * 提取每个汉字的首字母(大写)
	 *
	 * @param str
	 * @return
	 */
	public static String getPinYinHeadChar(String str) {
		if (Guard.isNullOrEmpty(str)) {
			return "";
		}
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			// 提取汉字的首字母
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			}
			else {
				convert += word;
			}
		}

		convert = string2AllTrim(convert);
		return convert.toUpperCase();
	}

	/**
	 * 提取每个汉字的全拼
	 * */
	public static String converterToSpell(String chines){
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				} catch (NullPointerException e){
					Tracer.e("AssortPinYin",Guard.isNull(nameChar[i])+" ===== "+defaultFormat);
					e.printStackTrace();
				}
			}else{
				pinyinName += nameChar[i];
			}
		}
		return pinyinName.toUpperCase();
	}

    /*
    * 判断字符串是否为空
    */

	public static boolean isNull(Object strData) {
		if (strData == null || String.valueOf(strData).trim().equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * 去掉字符串包含的所有空格
	 *
	 * @param value
	 * @return
	 */
	public static String string2AllTrim(String value) {
		if (Guard.isNullOrEmpty(value)) {
			return "";
		}
		return value.trim().replace(" ", "");
	}

}
