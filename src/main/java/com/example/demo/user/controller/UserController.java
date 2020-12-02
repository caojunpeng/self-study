package com.example.demo.user.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.example.demo.user.service.ExcelExportService;
import com.example.demo.utils.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2020-11-27 11:21:09
 */
@RestController
@RequestMapping("/user")
public class UserController extends ApiController {

    @Value("#{T(java.lang.System).getProperty('user.dir')}")
    private String installDir;

    /**
     * 服务对象
     */
    @Resource
    private UserService userService;/**
     * 服务对象
     */
    @Resource
    private ExcelExportService excelExportService;

    /**
     * 跳转到user界面
     * @return
     */
    @RequestMapping("/userList")
    public ModelAndView userList(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/user/user-list");
        return modelAndView;
    }

    /**
     * datatable页面
     * @param params
     * @return
     */
    @ResponseBody
    @RequestMapping("/userTable")
    public DataTablesResult<User> userTable(DataTablesParams params){
        return  userService.getUserTable(params);
    }

    /**
     * 跳转到Excel入库界面
     * @return
     */
    @RequestMapping("/userExcel")
    public ModelAndView userExcelAnalysis(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/user/userExcelAnalyse");
        return modelAndView;
    }

    /**
     * 跳转到word页面
     * @return
     */
    @RequestMapping("/userWord")
    public ModelAndView userWordAnalysis(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/user/userWordAnalyse");
        return modelAndView;
    }
    /**
     * 跳转到word页面
     * @return
     */
    @RequestMapping("/ztreePage")
    public ModelAndView ztreePage(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("/user/userZTree");
        return modelAndView;
    }

    /**
     * 解析excel文件
     * @param multipartFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userExcelExport", method = {RequestMethod.POST,RequestMethod.GET})
    public List<Map<String, Object>> userExport(@RequestParam("excelFile") MultipartFile multipartFile) {
        List<Map<String, Object>> maps=null;
        try {
            maps = ExcelUtils.readExcelToMaps(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(maps);
        return maps;
    }

    /**
     * word解析
     * @param multipartFile
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/userWordAnalyse", method = {RequestMethod.POST,RequestMethod.GET})
    public String userWordAnalyse(@RequestParam("wordFile") MultipartFile multipartFile) {
        String result=null;
        try {
            result = WordUtils.wordAnalys(multipartFile,installDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    /**
     * excel导出
     * @param response
     * @param request
     */
    @RequestMapping("/userListExport")
    @ResponseBody
    public void personInfoComparisonToXml(HttpServletResponse response,DataTablesParams param,  HttpServletRequest request) {
        String type = "userListExport";
        String fileName = "userList"+StringDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss")+".xlsx";
        String path=installDir+ File.separator+"excelExport";
        Map<String, Object> map = new HashMap<>();
        map.put("param", param);
        //fileName:文件名 map:参数 type:实现类中的标识
        XSSFWorkbook wb = excelExportService.exportExcel(fileName, map, type);
        //浏览器下载
        ExcelUtils.downFile(response, fileName, wb, request);
    }




}