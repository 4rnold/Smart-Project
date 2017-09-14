package com.arnold.SmartFramework.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class DemoTest {

    @Test
    public void test() {
        String[] a = {"11","2","3"};
        Map<String, String[]> map = new HashMap<>();
        map.put("a1", new String[]{"11", "2", "3"});
        map.put("a2", new String[]{"444", "5", "6"});


        Map<String, String> map2 = new HashMap<>();
        map.forEach((k,v) -> map2.put(k, StringUtils.join(v)));

        System.out.println("aa");
    }
}
