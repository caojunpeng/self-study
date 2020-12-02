package com.example.demo.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class BController {
    private static String path="https://www.bilibili.com/anime/index/#season_version=-1&area=-1&is_finish=-1&copyright=-1&season_status=-1&season_month=-1&year=-1&style_id=-1&order=3&st=1&sort=0&page=1";
    public static void main(String[] args) {
        Document doc=null;
        {
            try {
                doc = Jsoup.connect(path).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String title = doc.title();
        System.out.println(title);
    }
}
