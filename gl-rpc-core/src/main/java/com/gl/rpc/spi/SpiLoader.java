package com.gl.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import com.gl.rpc.serializer.Serializer;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SpiLoader {

    //存储已经加载的类
    private static Map<String, Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();

    //对象实例缓存
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SCAN_DIRS = new String[]{RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};

    /**
     * 动态加载类的列表
     */
    private static final List<Class<?>> LOAD_CLASS_LIST = Arrays.asList(Serializer.class);

    public static void loadAll(){
        log.info("加载所有SPI");
        for(Class<?> aClass:LOAD_CLASS_LIST){
            load(aClass);
        }
    }

    public static <T> T getInstance(Class<?> tClass,String key){
        String tClassName = tClass.getName();
        Map<String, Class<?>> keyClassMap = loaderMap.get(tClass.getName());
        if(keyClassMap == null){
            throw new RuntimeException(String.format("SPI 未加载 %s类型",tClassName));
        }
        if(!keyClassMap.containsKey(key)){
            throw new RuntimeException(String.format("不存在 %s类型",tClassName));
        }
        Class<?> implClass = keyClassMap.get(key);
        String name = implClass.getName();
        if(!instanceCache.containsKey(name)){
            try {
                instanceCache.put(name,implClass.newInstance());
            } catch (Exception e) {
                log.error("%s 实例化失败" ,name);
            }
        }
        return (T) instanceCache.get(name);

    }
    public static Map<String,Class<?>> load(Class<?> aClass){
        log.info("加载类型为{} 的spi",aClass.getName());
        Map<String,Class<?>> keyClassMap = new HashMap<>();
        for(String scanDir:SCAN_DIRS){
            List<URL> resources = ResourceUtil.getResources(scanDir + aClass.getName());
            for(URL resource:resources){
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while((line = bufferedReader.readLine())!=null){
                        String[] split = line.split("=");
                        if(split.length>1){
                            String key = split[0];
                            String className = split[1];
                            keyClassMap.put(key,Class.forName(className));
                        }
                    }
                } catch (IOException |ClassNotFoundException e) {
                    log.error("spi load error",e);
                }
            }
        }
        loaderMap.put(aClass.getName(),keyClassMap);
        return keyClassMap;
    }
}
