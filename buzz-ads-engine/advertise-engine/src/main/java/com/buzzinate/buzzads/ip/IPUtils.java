package com.buzzinate.buzzads.ip;

import com.buzzinate.common.util.ip.ProvinceName;

/**
 * thread-safe ip util
 * @author feeling
 *
 */
public final class IPUtils {

	private static IPSeeker seeker = IPSeeker.getInstance();

	private IPUtils() {
	}

	/**
	 * 通过IP获得所在省份
	 * 
	 * @param ip
	 * @return
	 */
	public static String getProvince(String ip) {
		return seeker.getProvince(ip);
	}

	/**
	 * 通过IP获得所在省份代码，代码为两个字符，字符列表请参照ProvinceName
	 * 
	 * @param ip
	 * @return
	 */
	public static String getProvinceCode(String ip) {
		return ProvinceName.getCode(getProvince(ip));
	}

	/**
	 * 通过省份代码获得省份字符
	 * 
	 * @param code
	 * @return
	 */
	public static String getProvinceFromCode(String code) {
		return ProvinceName.getProvinceName(code);
	}

}
