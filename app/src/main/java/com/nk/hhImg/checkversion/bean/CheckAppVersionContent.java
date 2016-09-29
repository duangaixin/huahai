package com.nk.hhImg.checkversion.bean;

import java.io.Serializable;
/**
 * Created by dgx on 2016/9/7.
 */
public class CheckAppVersionContent implements Serializable{

    /**
     * version : 1.0.1
     * url : http://www.baidu.com/
     * title : 修复严重bug
     * state : 1
     */

       private String version;
        private String url;
        private String title;
        private String state;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
}

