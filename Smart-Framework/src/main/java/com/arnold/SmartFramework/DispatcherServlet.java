package com.arnold.SmartFramework;

import com.arnold.SmartFramework.Bean.Data;
import com.arnold.SmartFramework.Bean.Handler;
import com.arnold.SmartFramework.Bean.Param;
import com.arnold.SmartFramework.Bean.View;
import com.arnold.SmartFramework.helper.*;
import com.arnold.SmartFramework.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/*", loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        HelperLoader.init();
        ServletContext servletContext = config.getServletContext();


        //注册 defaultservlet jspservlet
        ServletRegistration jspServlet = servletContext.getServletRegistration("jsp");
        jspServlet.addMapping("/index.jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath() + "*");


        ServletRegistration defaultServlet = servletContext.getServletRegistration("default");
        defaultServlet.addMapping("/favicon.ico");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath() + "*");

        UploadHelper.init(servletContext);

    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String reqMethod = req.getMethod();
        String reqPath = req.getPathInfo();

        Handler handler = ControllerHelper.getHandler(reqMethod, reqPath);
        if (handler != null) {
            //Class<?> controllerClass = handler.getControllerClass();
            //Object controllerBean = BeanHelper.getBean(controllerClass);
            
            //请求参数
            /*Map<String, String[]> parameterMap = req.getParameterMap();
            //req.getParameter();
            Map<String, Object> paramMap = new HashMap<>();
            parameterMap.forEach((k,v) -> paramMap.put(k, StringUtils.join(v)));

            String body = CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if (StringUtil.isNotEmpty(body)) {
                String[] params = StringUtil.splitString(body, "&");
                if (ArrayUtils.isNotEmpty(params)) {
                    for (String param : params) {
                        String[] array = StringUtil.splitString(param, "=");
                        if (ArrayUtils.isNotEmpty(array) && array.length==2) {
                            paramMap.put(array[0], array[1]);
                        }
                    }
                }

            }


            Param param = new Param(paramMap);*/

            Param param;
            if (UploadHelper.isMultipart(req)) {
                param = UploadHelper.createParam(req);
            } else {
                param = RequestHelper.createParam(req);
            }


            //调用action
            //Method actionMethod = handler.getActionMethod();

            Object result = ReflectionUtil.invokeHandler(handler, param);
            if (result instanceof View) {
                View view = (View) result;
                String path = view.getPath();
                if (StringUtil.isNotEmpty(path)){
                    if (path.startsWith("/")){
                        resp.sendRedirect(path);
                    } else {
                        Map<String, Object> model = view.getModel();
                        for (Map.Entry<String, Object> entry : model.entrySet()) {
                            req.setAttribute(entry.getKey(), entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath() + path).forward(req,resp);
                    }
                }
            } else if (result instanceof Data){
                Data data = (Data) result;
                Object model = data.getModel();
                if (model != null) {
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer = resp.getWriter();
                    String json = JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }




        }


    }
}
