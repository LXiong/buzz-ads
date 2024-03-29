package com.buzzinate.buzzads.ip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * * 用来读取QQwry.dat文件，以根据ip获得好友位置，QQwry.dat的格式是 一. 文件头，共8字节 1. 第一个起始IP的绝对偏移， 4字节
 * 2. 最后一个起始IP的绝对偏移， 4字节 二. "结束地址/国家/区域"记录区 四字节ip地址后跟的每一条记录分成两个部分 1. 国家记录 2.
 * 地区记录 但是地区记录是不一定有的。而且国家记录和地区记录都有两种形式 1. 以0结束的字符串 2. 4个字节，一个字节可能为0x1或0x2 a.
 * 为0x1时，表示在绝对偏移后还跟着一个区域的记录，注意是绝对偏移之后，而不是这四个字节之后 b. 为0x2时，表示在绝对偏移后没有区域记录
 * 不管为0x1还是0x2，后三个字节都是实际国家名的文件内绝对偏移
 * 如果是地区记录，0x1和0x2的含义不明，但是如果出现这两个字节，也肯定是跟着3个字节偏移，如果不是 则为0结尾字符串 三.
 * "起始地址/结束地址偏移"记录区 1. 每条记录7字节，按照起始地址从小到大排列 a. 起始IP地址，4字节 b. 结束ip地址的绝对偏移，3字节
 * 
 * 注意，这个文件里的ip地址和所有的偏移量均采用little-endian格式，而java是采用 big-endian格式的，要注意转换
 * 
 * 
 */
// CHECKSTYLE:OFF
@edu.umd.cs.findbugs.annotations.SuppressWarnings
@SuppressWarnings("all")
public class IPSeeker {

	private static Logger log = LoggerFactory.getLogger(IPSeeker.class);

	/**
	 * * 用来封装ip相关信息，目前只有两个字段，ip所在的国家和地区
	 */
	private class IPLocation {
		public String country;
		public String area;

		public IPLocation() {
			country = area = "";
		}

		public IPLocation getCopy() {
			IPLocation ret = new IPLocation();
			ret.country = country;
			ret.area = area;
			return ret;
		}
	}

	// 一些固定常量，比如记录长度等等
	private static final int IP_RECORD_LENGTH = 7;
	private static final byte AREA_FOLLOWED = 0x01;
	private static final byte NO_AREA = 0x2;

	// 用来做为cache，查询一个ip时首先查看cache，以减少不必要的重复查找
	// private HashMap<String, IPLocation> ipCache;
	// 随机文件访问类
	private RandomAccessFile ipFile;
	// 单一模式实例
	private static IPSeeker instance = new IPSeeker();
	// 起始地区的开始和结束的绝对偏移
	private long ipBegin, ipEnd;

	/**
	 * 私有构造函数
	 */
	private IPSeeker() {
		try {
			InputStream is = IPSeeker.class.getResourceAsStream("/qqwry.dat");
			File file = File.createTempFile("qqwry", ".tmp");
			convertStreamToFile(is, file);
			ipFile = new RandomAccessFile(file, "r");
		} catch (FileNotFoundException e) {
			log.info("IP地址信息文件没有找到，IP显示功能将无法使用");
			ipFile = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 如果打开文件成功，读取文件头信息
		if (ipFile != null) {
			try {
				ipBegin = readLong4(0);
				ipEnd = readLong4(4);
				if (ipBegin == -1 || ipEnd == -1) {
					ipFile.close();
					ipFile = null;
				}
			} catch (IOException e) {
				log.info("IP地址信息文件格式有错误，IP显示功能将无法使用");
				ipFile = null;
			}
		}
	}

	/**
	 * @return 单一实例
	 */
	public static IPSeeker getInstance() {
		return instance;
	}

	private static void convertStreamToFile(InputStream is, File file) throws IOException {
		BufferedInputStream reader = new BufferedInputStream(is);
		BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(file));
		byte array[] = new byte[100];
		int n;
		while ((n = reader.read(array)) > 0) {
			writer.write(array, 0, n);
		}
		reader.close();
		writer.flush();
		writer.close();
		is.close();
	}

	/**
	 * 根据IP得到国家名
	 * 
	 * @param ip
	 *            ip的字节数组形式
	 * @return 国家名字符串
	 */
	private String getCountry(byte[] ip) {
		// 检查ip地址文件是否正常
		if (ipFile == null)
			return "错误的IP数据库文件";
		// 保存ip，转换ip字节数组为字符串形式
		String ipStr = getIpStringFromBytes(ip);
		IPLocation loc = getIPLocation(ip);
		return loc.country;
	}

	/**
	 * 根据IP得到国家名
	 * 
	 * @param ip
	 *            IP的字符串形式
	 * @return 国家名字符串
	 */
	private String getCountry(String ip) {
		return getCountry(getIpByteArrayFromString(ip));
	}

	/**
	 * 根据ip搜索ip信息文件，得到IPLocation结构，所搜索的ip参数从类成员ip中得到
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return IPLocation结构
	 */
	private IPLocation getIPLocation(byte[] ip) {
		IPLocation info = null;
		long offset = locateIP(ip);
		if (offset != -1)
			info = getIPLocation(offset);
		if (info == null) {
			info = new IPLocation();
			info.country = "未知国家";
			info.area = "未知地区";
		}
		return info;
	}

	/**
	 * 从offset位置读取4个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong4(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			ret |= (ipFile.readByte() & 0xFF);
			ret |= ((ipFile.readByte() << 8) & 0xFF00);
			ret |= ((ipFile.readByte() << 16) & 0xFF0000);
			ret |= ((ipFile.readByte() << 24) & 0xFF000000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从offset位置读取3个字节为一个long，因为java为big-endian格式，所以没办法 用了这么一个函数来做转换
	 * 
	 * @param offset
	 * @return 读取的long值，返回-1表示读取文件失败
	 */
	private long readLong3(long offset) {
		long ret = 0;
		try {
			ipFile.seek(offset);
			byte[] b3 = new byte[3];
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从当前位置读取3个字节转换成long
	 * 
	 * @return
	 */
	private long readLong3() {
		long ret = 0;
		try {
			byte[] b3 = new byte[3];
			ipFile.readFully(b3);
			ret |= (b3[0] & 0xFF);
			ret |= ((b3[1] << 8) & 0xFF00);
			ret |= ((b3[2] << 16) & 0xFF0000);
			return ret;
		} catch (IOException e) {
			return -1;
		}
	}

	/**
	 * 从offset位置读取四个字节的ip地址放入ip数组中，读取后的ip为big-endian格式，但是
	 * 文件中是little-endian形式，将会进行转换
	 * 
	 * @param offset
	 * @param ip
	 */
	private void readIP(long offset, byte[] ip) {
		try {
			ipFile.seek(offset);
			ipFile.readFully(ip);
			byte temp = ip[0];
			ip[0] = ip[3];
			ip[3] = temp;
			temp = ip[1];
			ip[1] = ip[2];
			ip[2] = temp;
		} catch (IOException e) {
			log.error("" + e);
		}
	}

	/**
	 * 把类成员ip和beginIp比较，注意这个beginIp是big-endian的
	 * 
	 * @param ip
	 *            要查询的IP
	 * @param beginIp
	 *            和被查询IP相比较的IP
	 * @return 相等返回0，ip大于beginIp则返回1，小于返回-1。
	 */
	private int compareIP(byte[] ip, byte[] beginIp) {
		for (int i = 0; i < 4; i++) {
			int r = compareByte(ip[i], beginIp[i]);
			if (r != 0)
				return r;
		}
		return 0;
	}

	/**
	 * 把两个byte当作无符号数进行比较
	 * 
	 * @param b1
	 * @param b2
	 * @return 若b1大于b2则返回1，相等返回0，小于返回-1
	 */
	private int compareByte(byte b1, byte b2) {
		if ((b1 & 0xFF) > (b2 & 0xFF)) // 比较是否大于
			return 1;
		else if ((b1 ^ b2) == 0)// 判断是否相等
			return 0;
		else
			return -1;
	}

	/**
	 * 这个方法将根据ip的内容，定位到包含这个ip国家地区的记录处，返回一个绝对偏移 方法使用二分法查找。
	 * 
	 * @param ip
	 *            要查询的IP
	 * @return 如果找到了，返回结束IP的偏移，如果没有找到，返回-1
	 */
	private long locateIP(byte[] ip) {
		long m = 0;
		int r;
		byte[] b4 = new byte[4];
		// 比较第一个ip项
		readIP(ipBegin, b4);
		r = compareIP(ip, b4);
		if (r == 0)
			return ipBegin;
		else if (r < 0)
			return -1;
		// 开始二分搜索
		for (long i = ipBegin, j = ipEnd; i < j;) {
			m = getMiddleOffset(i, j);
			readIP(m, b4);
			r = compareIP(ip, b4);
			// log.debug(Utils.getIpStringFromBytes(b));
			if (r > 0)
				i = m;
			else if (r < 0) {
				if (m == j) {
					j -= IP_RECORD_LENGTH;
					m = j;
				} else
					j = m;
			} else
				return readLong3(m + 4);
		}
		// 如果循环结束了，那么i和j必定是相等的，这个记录为最可能的记录，但是并非
		// 肯定就是，还要检查一下，如果是，就返回结束地址区的绝对偏移
		m = readLong3(m + 4);
		readIP(m, b4);
		r = compareIP(ip, b4);
		if (r <= 0)
			return m;
		else
			return -1;
	}

	/**
	 * 得到begin偏移和end偏移中间位置记录的偏移
	 * 
	 * @param begin
	 * @param end
	 * @return
	 */
	private long getMiddleOffset(long begin, long end) {
		long records = (end - begin) / IP_RECORD_LENGTH;
		records >>= 1;
		if (records == 0)
			records = 1;
		return begin + records * IP_RECORD_LENGTH;
	}

	/**
	 * 给定一个ip国家地区记录的偏移，返回一个IPLocation结构
	 * 
	 * @param offset
	 * @return
	 */
	private IPLocation getIPLocation(long offset) {
		try {
			// 跳过4字节ip
			IPLocation loc = new IPLocation();
			ipFile.seek(offset + 4);
			// 读取第一个字节判断是否标志字节
			byte b = ipFile.readByte();
			if (b == AREA_FOLLOWED) {
				// 读取国家偏移
				long countryOffset = readLong3();
				// 跳转至偏移处
				ipFile.seek(countryOffset);
				// 再检查一次标志字节，因为这个时候这个地方仍然可能是个重定向
				b = ipFile.readByte();
				if (b == NO_AREA) {
					loc.country = readString(readLong3());
					ipFile.seek(countryOffset + 4);
				} else
					loc.country = readString(countryOffset);
				// 读取地区标志
				loc.area = readArea(ipFile.getFilePointer());
			} else if (b == NO_AREA) {
				loc.country = readString(readLong3());
				loc.area = readArea(offset + 8);
			} else {
				loc.country = readString(ipFile.getFilePointer() - 1);
				loc.area = readArea(ipFile.getFilePointer());
			}
			return loc;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 从offset偏移开始解析后面的字节，读出一个地区名
	 * 
	 * @param offset
	 * @return 地区名字符串
	 * @throws IOException
	 */
	private String readArea(long offset) throws IOException {
		ipFile.seek(offset);
		byte b = ipFile.readByte();
		if (b == 0x01 || b == 0x02) {
			long areaOffset = readLong3(offset + 1);
			if (areaOffset == 0)
				return "未知地区";
			else
				return readString(areaOffset);
		} else
			return readString(offset);
	}

	/**
	 * 从offset偏移处读取一个以0结束的字符串
	 * 
	 * @param offset
	 * @return 读取的字符串，出错返回空字符串
	 */
	private String readString(long offset) {
		try {
			byte[] buf = new byte[100];
			ipFile.seek(offset);
			int i;
			for (i = 0, buf[i] = ipFile.readByte(); buf[i] != 0; buf[++i] = ipFile.readByte())
				;
			if (i != 0)
				return getString(buf, 0, i, "GBK");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return "";
	}

	/**
	 * 从ip的字符串形式得到字节数组形式
	 * 
	 * @param ip
	 *            字符串形式的ip
	 * @return 字节数组形式的ip
	 */
	private byte[] getIpByteArrayFromString(String ip) {
		byte[] ret = new byte[4];
		java.util.StringTokenizer st = new java.util.StringTokenizer(ip, ".");
		try {
			ret[0] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[1] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[2] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
			ret[3] = (byte) (Integer.parseInt(st.nextToken()) & 0xFF);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return ret;
	}

	/**
	 * 根据某种编码方式将字节数组转换成字符串
	 * 
	 * @param b
	 *            字节数组
	 * @param offset
	 *            要转换的起始位置
	 * @param len
	 *            要转换的长度
	 * @param encoding
	 *            编码方式
	 * @return 如果encoding不支持，返回一个缺省编码的字符串
	 */
	private String getString(byte[] b, int offset, int len, String encoding) {
		try {
			return new String(b, offset, len, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(b, offset, len);
		}
	}

	/**
	 * @param ip
	 *            ip的字节数组形式
	 * @return 字符串形式的ip
	 */
	private static String getIpStringFromBytes(byte[] ip) {
		StringBuffer sb = new StringBuffer();
		sb.append(ip[0] & 0xFF);
		sb.append('.');
		sb.append(ip[1] & 0xFF);
		sb.append('.');
		sb.append(ip[2] & 0xFF);
		sb.append('.');
		sb.append(ip[3] & 0xFF);
		return sb.toString();
	}

	/**
	 * 通过IP获得所在省份，省份为两个汉字
	 * 
	 * @param ip
	 * @return
	 */
	public String getProvince(String ip) {
		String address = getCountry(ip);
		String province = StringUtils.substring(address, 0, 2);
		if (province.equals("黑龙")) {
			province = "黑龙江";
		}
		return province;
	}

}
// CHECKSTYLE:OFF