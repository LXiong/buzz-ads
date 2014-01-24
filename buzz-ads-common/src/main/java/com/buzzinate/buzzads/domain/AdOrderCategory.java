package com.buzzinate.buzzads.domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Johnson
 *
 */
public class AdOrderCategory implements Serializable {

    private static final long serialVersionUID = 9166520658614082869L;
    
    private int id;
    private int parentId;
    private String name;
    private String enName;
    private List<AdOrderCategory> subCategories;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getParentId() {
        return parentId;
    }
    
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEnName() {
        return enName;
    }
    
    public void setEnName(String enName) {
        this.enName = enName;
    }
    
    public List<AdOrderCategory> getSubCategories() {
        return subCategories;
    }
    
    public void setSubCategories(List<AdOrderCategory> subCategories) {
        this.subCategories = subCategories;
    }

}
