package com.arnold.SmartWeb.controller;

import com.arnold.SmartFramework.Bean.Param;
import com.arnold.SmartFramework.Bean.View;
import com.arnold.SmartFramework.annotation.Action;
import com.arnold.SmartFramework.annotation.Controller;

@Controller
public class CustomerSmartController {

    @Action("get:/customer")
    public View index(Param param) {
        return new View("customer.jsp");
    }
}
