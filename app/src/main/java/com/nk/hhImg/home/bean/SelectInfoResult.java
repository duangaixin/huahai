package com.nk.hhImg.home.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by win on 2016/8/18.
 */
public class SelectInfoResult implements Serializable {
    /**
     * token : 工号不存在
     * busi_num : 20160807
     * types : [{"typeName":"未分类","typeCode":"noClassify"},
     * {"typeName":"类型2","typeCode":"3"},
     * {"typeName":"类型3","typeCode":"2"}]
     * sysCode : ccb
     * key : 1
     * url : http://10.5.1.185:7001/uip_hhbx/PhoneUploadServlet
     */

    private String token;
    private String busi_num;
    private String sysCode;
    private String key;
    private String url;
    /**
     * typeName : 未分类
     * typeCode : noClassify
     */

    private List<TypesBean> types;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBusi_num() {
        return busi_num;
    }

    public void setBusi_num(String busi_num) {
        this.busi_num = busi_num;
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public static class TypesBean implements Serializable {
        private String typeName;
        private String typeCode;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }
    }
   /* *
     * busi_num : 20160804
     * types : [{"typeName":"未分类","typeCode":"0"},
     * {"typeName":"黄老师1","typeCode":"1"},
     * {"typeName":"黄老师3","typeCode":"3"},
     * {"typeName":"黄老师2","typeCode":"2"}]
     * key : 1
     * url : http://10.2.16.193:8080/uip_hhbx/uploadImage
     *//*

    private String busi_num;
    private String key;
    private String url;
    *//**
     * typeName : 未分类
     * typeCode : 0
     *//*

    private List<TypesBean> types;

    public String getBusi_num() {
        return busi_num;
    }

    public void setBusi_num(String busi_num) {
        this.busi_num = busi_num;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TypesBean> getTypes() {
        return types;
    }

    public void setTypes(List<TypesBean> types) {
        this.types = types;
    }

    public static class TypesBean implements Serializable{
        private String typeName;
        private String typeCode;

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }
    }*/
}
