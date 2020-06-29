package controllers;

import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Controller {

    public Document ret(String ret) {
        String pattern = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String date = simpleDateFormat.format(new Date());
        Document response = new Document()
                .append("ret", ret)
                .append("result", true)
                .append("cloudtime", date);
//        System.out.println("Response : " + response.toJson());
        return response;
    }



}
