package com.example.demo.user.service.impl;

import com.example.demo.user.dao.UserMapper;
import com.example.demo.user.entity.User;
import com.example.demo.user.service.ExcelExportService;
import com.example.demo.user.service.UserService;
import com.example.demo.utils.DataTablesParams;
import com.example.demo.utils.ExcelUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportServiceImpl implements ExcelExportService {
    @Resource
    private UserMapper userMapper;

    @Override
    public XSSFWorkbook exportExcel(String fileName, Map<String, Object> map, String sheetName) {
        XSSFWorkbook wb = null;
        String mC=null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<List<String>> selectAllByParm =  new ArrayList<>();
        List<String> titlList = new ArrayList<>();
        if ("userListExport".equals(sheetName)) {
            String[] title = { "用户名","密码","备注","角色" };// 标题
            titlList = Arrays.asList(title);
            selectAllByParm = contacts((DataTablesParams)map.get("param"));
        }
        wb = ExcelUtils.getXSSFExportExcel(sheetName, titlList, selectAllByParm,new XSSFWorkbook());
        return wb;
    }

    /**
     * 获取数据
     * @param maps
     * @return
     */
    public List<List<String>> contacts(DataTablesParams maps){
        List<List<String>> selectAllByParm = new ArrayList<>();
        List<User> ChatMsgList = userMapper.selectList(null);
        if(ChatMsgList!=null&&!ChatMsgList.isEmpty()){
            if(ChatMsgList!=null&&!ChatMsgList.isEmpty()){
                for (User user : ChatMsgList) {
                    List<String> excel = new ArrayList<>();
                    excel.add(user.getUserName());
                    excel.add(user.getPassword());
                    excel.add(user.getRemark());
                    excel.add(user.getRoleId()+"");
                    selectAllByParm.add(excel);
                }
            }
        }
        return selectAllByParm;
    }
}
