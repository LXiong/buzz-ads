package com.buzzinate.buzzads.common.log;

/**
 * 日志配置,装载xml配置文件内容
 * 
 * @author Harry Feng<xiaobo.feng@buzzinate.com>
 * 
 *         2012-12-25
 */
public class LogConfig {
    
    private String type;
    private String objtype;
    private String objtypename;
    private String name;
    private String description;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObjtype() {
        return objtype;
    }

    public void setObjtype(String objtype) {
        this.objtype = objtype;
    }

    public String getObjtypename() {
        return objtypename;
    }

    public void setObjtypename(String objtypename) {
        this.objtypename = objtypename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
