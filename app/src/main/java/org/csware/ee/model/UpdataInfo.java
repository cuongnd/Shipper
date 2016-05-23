package org.csware.ee.model;


/**
 * 应用实体信息
 * @author Administrator
 *
 */
public class UpdataInfo extends Return{


	/**
	 * Version : {"VersionCode":"1","Description":"初始版本","VersionName":"1.0","Time":"2015-08-27T15:03:33+08:00","Id":1,"Url":"url"}
	 */
	public VersionEntity Version;

	public static class VersionEntity {
		/**
		 * VersionCode : 1
		 * Description : 初始版本
		 * VersionName : 1.0
		 * Time : 2015-08-27T15:03:33+08:00
		 * Id : 1
		 * Url : url
		 */
		public String VersionCode;
		public String Description;
		public String VersionName;
		public String Time;
		public int Id;
		public String Url;
	}
}

