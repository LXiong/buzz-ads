package com.buzzinate.buzzads.common.log;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 本地log缓存
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 *
 * 2012-12-24
 */
public final class LocalLogBuffer {
    
    private static final ThreadLocal<List<Map<String, Object>>> HOLDER = new ThreadLocal<List<Map<String, Object>>>();
    
    private LocalLogBuffer() { }
    
    public static void add(Map<String, Object> log) {
        List<Map<String, Object>> list = get();
        if (list == null) {
            list = new LinkedList<Map<String, Object>>();
        }
        list.add(log);
        HOLDER.set(list);
    }

    public static List<Map<String, Object>> get() {
        return HOLDER.get();
    }
    
    public static void clear() {
        HOLDER.remove();
    }

}
