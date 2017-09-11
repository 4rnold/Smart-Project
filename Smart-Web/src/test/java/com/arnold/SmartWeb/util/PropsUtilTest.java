package com.arnold.SmartWeb.util;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Properties;

/**
 * PropsUtil Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>09/10/2017</pre>
 */
public class PropsUtilTest {

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: loadProps(String fileName)
     */
    @Test
    public void testLoadProps() throws Exception {
        Properties properties = PropsUtil.loadProps("config.properties");
        System.out.println(properties.get("jdbc.password"));
    }


} 
