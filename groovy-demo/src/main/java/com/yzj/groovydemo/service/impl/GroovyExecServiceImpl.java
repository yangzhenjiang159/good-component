package com.yzj.groovydemo.service.impl;

import com.yzj.groovydemo.service.GroovyExecService;
import com.yzj.groovydemo.utils.GroovyEngineUtils;
import com.yzj.groovydemo.utils.GroovyScriptVar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.script.CompiledScript;
import java.util.HashMap;
import java.util.Map;


@Service
public class GroovyExecServiceImpl implements GroovyExecService {
    private static final Logger log = LoggerFactory.getLogger(GroovyEngineUtils.class);

    @Resource
    private GroovyScriptVar groovyScriptVar;


    @Override
    public void exec() {
        Map<String, Object> args=new HashMap<>();
        args.put("status","COMPLETED");
        CompiledScript compiledScript = groovyScriptVar.get("test");
        Object result = GroovyEngineUtils.eval(compiledScript, args);
        log.info("/// 脚本执行结果{}",(String)result);
    }

    @Override
    public void refresh() {
        groovyScriptVar.refresh();
    }
}
