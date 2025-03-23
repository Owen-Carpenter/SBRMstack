package dev.carpenter.sbrmstack.utils;


import java.util.Map;

public class Response {

    private boolean status;
    private String details;
    private Map<String, String> data;

    public Response() {
        super();
        this.status = true;
    }

    public Response(boolean status, String details) {
        super();
        this.status = status;
        this.details = details;
    }

    public Response(boolean status, String details, Map<String, String> data) {
        super();
        this.status = status;
        this.details = details;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response [status=" + status + ", details=" + details + ", data=" + data + "]";
    }





}