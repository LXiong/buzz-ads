package com.buzzinate.advertise.util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

/**
 * See http://en.wikipedia.org/wiki/URL_normalization for a reference Note: some
 * parts of the code are adapted from: http://stackoverflow.com/a/4057470/405418
 * 
 */
public class URLCanonicalizer {
	
	public static void main(String[] args) {
		String url = "http://article.woshao.com/796cfd2a642d11e1a75c000c2959fd2a/";
		System.out.println(getCanonicalURL(url));
		System.out.println(DomainNames.safeGetPLD("http://news.cnnb.com.cn/system/2012/08/21/007428900.shtml"));
	}

	public static String getCanonicalURL(String url) {
		return getCanonicalURL(url, null);
	}
	
	public static String getCanonicalURL(String href, String context) {
		try {
			URL canonicalURL = new URL(UrlResolver.resolveUrl(context == null ? "" : context, href));
			String path = canonicalURL.getPath();

			/*
			 * Normalize: no empty segments (i.e., "//"), no segments equal to
			 * ".", and no segments equal to ".." that are preceded by a segment
			 * not equal to "..".
			 */
			path = new URI(path).normalize().toString();

			/*
			 * Convert '//' -> '/'
			 */
			int idx = path.indexOf("//");
			while (idx >= 0) {
				path = path.replace("//", "/");
				idx = path.indexOf("//");
			}

			/*
			 * Drop starting '/../'
			 */
			while (path.startsWith("/../")) {
				path = path.substring(3);
			}

			/*
			 * Trim
			 */
			path = path.trim();

			final SortedMap<String, String> params = createParameterMap(canonicalURL.getQuery());
			final String queryString;

			if (params != null && params.size() > 0) {
				String canonicalParams = canonicalize(params);
				queryString = (canonicalParams.isEmpty() ? "" : "?"
						+ canonicalParams);
			} else {
				queryString = "";
			}

			/*
			 * Add starting slash if needed
			 */
			if (path.length() == 0) {
				path = "/" + path;
			}

			/*
			 * Drop default port: example.com:80 -> example.com
			 */
			int port = canonicalURL.getPort();
			if (port == canonicalURL.getDefaultPort()) {
				port = -1;
			}

			/*
			 * Lowercasing protocol and host
			 */
			String protocol = canonicalURL.getProtocol().toLowerCase();
			String host = canonicalURL.getHost().toLowerCase();
			path = normalizePath(path);
			String pathAndQueryString = path + queryString;
			URL result = new URL(protocol, host, port, pathAndQueryString);
			return result.toExternalForm();
		} catch (MalformedURLException ex) {
			return "";
		} catch (URISyntaxException ex) {
			return "";
		}
	}
	
	public static String parentUrl(String url) {
		String purl = StringUtils.substringBeforeLast(url, "/");
		if (purl.length() == url.length() - 1) return parentUrl(purl);
		else return purl;
	}

	/**
	 * Takes a query string, separates the constituent name-value pairs, and
	 * stores them in a SortedMap ordered by lexicographical order.
	 * 
	 * @return Null if there is no query string.
	 */
	private static SortedMap<String, String> createParameterMap(final String queryString) {
		if (queryString == null || queryString.isEmpty()) {
			return null;
		}

		final String[] pairs = queryString.split("&");
		final Map<String, String> params = new HashMap<String, String>(pairs.length);

		for (final String pair : pairs) {
			if (pair.length() == 0) continue;

			String[] tokens = pair.split("=", 2);
			switch (tokens.length) {
			case 1:
				if (pair.charAt(0) == '=') {
					params.put("", tokens[0]);
				} else {
					params.put(tokens[0], "");
				}
				break;
			case 2:
				params.put(tokens[0], tokens[1]);
				break;
			}
		}
		return new TreeMap<String, String>(params);
	}

	/**
	 * Canonicalize the query string.
	 * 
	 * @param sortedParamMap
	 *            Parameter name-value pairs in lexicographical order.
	 * @return Canonical form of query string.
	 */
	private static String canonicalize(final SortedMap<String, String> sortedParamMap) {
		if (sortedParamMap == null || sortedParamMap.isEmpty()) {
			return "";
		}

		final StringBuffer sb = new StringBuffer(100);
		for (Map.Entry<String, String> pair : sortedParamMap.entrySet()) {
			final String key = pair.getKey().toLowerCase();
			if (key.equals("jsessionid") || key.equals("phpsessid")
					|| key.equals("aspsessionid")) {
				continue;
			}
			if (sb.length() > 0) {
				sb.append('&');
			}
			sb.append(percentEncodeRfc3986(pair.getKey()));
			if (!pair.getValue().isEmpty()) {
				sb.append('=');
				sb.append(percentEncodeRfc3986(pair.getValue()));
			}
		}
		return sb.toString();
	}

	/**
	 * Percent-encode values according the RFC 3986. The built-in Java
	 * URLEncoder does not encode according to the RFC, so we make the extra
	 * replacements.
	 * 
	 * @param string
	 *            Decoded string.
	 * @return Encoded string per RFC 3986.
	 */
	private static String percentEncodeRfc3986(String string) {
		try {
			string = string.replace("+", "%2B");
			string = URLDecoder.decode(string, "UTF-8");
			string = URLEncoder.encode(string, "UTF-8");
			return string.replace("+", "%20").replace("*", "%2A").replace("%7E", "~");
		} catch (Exception e) {
			return string;
		}
	}

	private static String normalizePath(final String path) {
		return path.replace("%7E", "~").replace(" ", "%20");
	}
}