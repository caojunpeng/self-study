package com.example.demo.utils;

import com.deepoove.poi.XWPFTemplate;
import org.apache.commons.io.IOUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WordUtils {

    @Value("#{T(java.lang.System).getProperty('user.dir')}")
    private  String installDir;

    /**
     * 读取word文件的内容
     * @param file file
     * @return 返回文件内容
     */
    public static String readWordToString(File file) {
        String wordContent = "";
        if(file!=null && file.exists()){
            String path = file.getAbsolutePath();
            if(StringDateUtils.isNotBlank(path)){
                try {
                    if (path.endsWith(".doc")) {
                        InputStream is = new FileInputStream(file);
                        WordExtractor ex = new WordExtractor(is);
                        wordContent = ex.getText();
                        ex.close();
                    } else if (path.endsWith("docx")) {
                        OPCPackage opcPackage = POIXMLDocument.openPackage(path);
                        POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
                        wordContent = extractor.getText();
                        extractor.close();
                    } else {
                        System.out.println("此文件不是word文件！");
                    }
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        return wordContent;
    }

    public static String wordAnalys(MultipartFile file,String installDir){
        InputStream stream= null;
        XWPFTemplate template = null;
        FileOutputStream out = null;
        Map<String,Object> map=new HashMap<>();
        map.put("姓名","王三");
        map.put("性别","男");
        map.put("年龄","18");
        String success = installDir + File.separator + StringDateUtils.dateToStr(new Date(), "yyyyMMddHHmmss") +".doc";
        try {
            stream = file.getInputStream();
            if(stream!=null){
                template = XWPFTemplate.compile(stream).render(map);
                File successfile = new File(installDir);
                if (!successfile.exists()) {
                    successfile.mkdirs();
                }
                out = new FileOutputStream(success);
                template.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(stream);
            IOUtils.closeQuietly(out);
        }
        return null;
    }
}
