package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * mybatis测试
 *(PersonInfo)表控制层
 * @author makejava
 * @since 2020-11-20 14:33:53
 */
@RestController
public class MainController {

    /**
     * 跳转主页入口
     * @return
     */
    @RequestMapping("/")
    public ModelAndView index(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("/index");
        return mv;
    }

    /**
     * 跳转excel入口
     * @return
     */
    @RequestMapping("/excel")
    public ModelAndView excel(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("/utile/excel");
        return mv;
    }

    /**
     * 跳转word入口
     * @return
     */
    @RequestMapping("/word")
    public ModelAndView word(){
        ModelAndView mv=new ModelAndView();
        mv.setViewName("/utile/word");
        return mv;
    }

}