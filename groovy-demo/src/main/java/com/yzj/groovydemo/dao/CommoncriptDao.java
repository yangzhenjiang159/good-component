package com.yzj.groovydemo.dao;

import com.yzj.groovydemo.entity.CommonScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommoncriptDao {
    public List<CommonScript> findAll() {
        CommonScript commonScript = new CommonScript();
        String script = "if (\"COMPLETED\" == status) {\n" +
                "    // 如果条件为COMPLETED\n" +
                "    return \"条件为已完成\"\n" +
                "}else{\n" +
                "    return \"条件为未完成\"\n" +
                "}";
        commonScript.setScript(script);
        commonScript.setUniqueKey("test");
        List<CommonScript> res = new ArrayList<>();
        res.add(commonScript);
        return res;
    }

}
