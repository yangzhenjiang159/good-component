package com.yzj.groovydemo.utils;

import com.yzj.groovydemo.dao.CommoncriptDao;
import com.yzj.groovydemo.entity.CommonScript;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.script.CompiledScript;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GroovyScriptVar {
    private static final Logger log = LoggerFactory.getLogger(GroovyScriptVar.class);

    // 缓存编译后的脚本
    private static final Map<String, CompiledScript> SCRIPT_MAP = new ConcurrentHashMap<>();

    @Resource
    public CommoncriptDao commonScriptDao;


    @PostConstruct
    public void init() {
        refresh();
    }

    @SneakyThrows
    public void refresh() {
        synchronized (GroovyScriptVar.class) {
            // 从数据库中加载脚本
            List<CommonScript> list = commonScriptDao.findAll();
            SCRIPT_MAP.clear();
            if(CollectionUtils.isEmpty(list)) return;
            for (CommonScript script : list) {
                try {
                    SCRIPT_MAP.put(script.getUniqueKey(), GroovyEngineUtils.compile(script.getScript()));
                } catch (Exception e) {
                    log.error("compile err");
                }
            }
            log.info("//// Groovy脚本初始化，加载数量：{}",list.size());
        }
    }


    public CompiledScript get(String uniqueKey){
        return SCRIPT_MAP.get(uniqueKey);
    }

}