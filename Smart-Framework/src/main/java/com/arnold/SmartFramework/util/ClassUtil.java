package com.arnold.SmartFramework.util;

import com.sun.org.apache.bcel.internal.generic.LoadClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassUtil.class);


    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Set<Class> getClassSet(String packageName) {
        Set<Class> classSet = new HashSet<>();
        try {
            Enumeration<URL> urls = getClassLoader().getResources(packageName.replace(".", "/"));
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                if (url != null) {
                    String protocol = url.getProtocol();
                    if (protocol.equals("file")) {
                        String packagePath = url.getPath().replaceAll("%20", " ");//空格替换
                        addClass(classSet, packagePath, packageName);//path 和 name
                    } else if (protocol.equals("jar")) {
                        JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                        if (jarURLConnection != null) {
                            JarFile jarFile = jarURLConnection.getJarFile();
                            if (jarFile != null) {
                                Enumeration<JarEntry>jarEntries= jarFile.entries();
                                while (jarEntries.hasMoreElements()) {
                                    JarEntry jarEntry = jarEntries.nextElement();
                                    String jarEntryName = jarEntry.getName();
                                    if (jarEntryName.endsWith(".class")) {
                                        String className = jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replace("/",".");
                                        doAddClass(classSet, className);//没考虑jar包中有文件夹?
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("get classSet fail", e);
            throw new RuntimeException(e);
        }
        return classSet;

    }

    private static void addClass(Set<Class> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return (pathname.isFile() && pathname.getName().endsWith(".class")) || pathname.isDirectory(); //目录或者是class结尾的类
            }
        });

        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                String directoryPath = packagePath + "/" + fileName;//没有考虑packagePath为空?
                String subPackageName = packageName + "." + fileName;
                addClass(classSet, directoryPath, subPackageName);
            } else {
                String className = fileName.substring(0, fileName.indexOf("."));
                className = packageName + "." + className;//com.arnold.XXXX类名
                doAddClass(classSet, className);
            }
        }
    }

    private static void doAddClass(Set<Class> classSet, String className) {
        Class cls = loadClass(className,false);
        classSet.add(cls);
    }

    public static Class loadClass(String className, boolean isInit) {
        Class cls;
        try {
            cls = Class.forName(className,isInit,getClassLoader());
        } catch (ClassNotFoundException e) {
            LOGGER.error("load class fail", e);
            throw new RuntimeException("ClassUtil.loadClass fail", e);
        }
        return cls;
    }


}
