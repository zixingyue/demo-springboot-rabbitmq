package com.demo.rabbitmq.pojo;/**
 * Created by I on 2017/6/7.
 */

import java.io.Serializable;

/**
 * @author
 * @create 2017-06-07 21:24
 **/
public class RmqMsg< T > implements Serializable
    {
        private static final long serialVersionUID = -6487839157908352120L;

        private String vHost;

        private String exchange;//交换器

        private String routeKey;//路由key

        private Long userId;

        private String type;

        private T msg;

        public RmqMsg(){}

        public RmqMsg(String vHost, String exchange, String routeKey, Long userId, String type, T msg) {
            this.vHost = vHost;
            this.exchange = exchange;
            this.routeKey = routeKey;
            this.userId = userId;
            this.type = type;
            this.msg = msg;
        }

        public String getvHost() {
            return vHost;
        }

        public void setvHost(String vHost) {
            this.vHost = vHost;
        }

        public String getExchange() {
            return exchange;
        }

        public void setExchange(String exchange) {
            this.exchange = exchange;
        }

        public String getRouteKey() {
            return routeKey;
        }

        public void setRouteKey(String routeKey) {
            this.routeKey = routeKey;
        }

        public T getMsg() {
            return msg;
        }

        public void setMsg(T msg) {
            this.msg = msg;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
