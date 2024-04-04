package com.sr_developers.sowetodrycleaners.models;

public class OrderDetails {
    String name, price, kgs, email, subscribed, method, status, id, order_Id, date;

    public OrderDetails() {
    }

    public OrderDetails(String name, String price, String kgs, String email, String subscribed, String method, String status, String id, String order_Id, String date) {
        this.name = name;
        this.price = price;
        this.kgs = kgs;
        this.email = email;
        this.subscribed = subscribed;
        this.method = method;
        this.status = status;
        this.id = id;
        this.order_Id = order_Id;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKgs() {
        return kgs;
    }

    public void setKgs(String kgs) {
        this.kgs = kgs;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(String subscribed) {
        this.subscribed = subscribed;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_Id() {
        return order_Id;
    }

    public void setOrder_Id(String order_Id) {
        this.order_Id = order_Id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}