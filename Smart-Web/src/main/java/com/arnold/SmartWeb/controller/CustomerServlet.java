package com.arnold.SmartWeb.controller;




import com.arnold.SmartWeb.model.Customer;
import com.arnold.SmartWeb.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 进入 客户列表 界面1
 */
//@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private CustomerService customerService;


    @Override
    public void init() throws ServletException {
        customerService = new CustomerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Customer> customerList = customerService.getCustomerList();
        req.setAttribute("customerList", customerList);
        req.getRequestDispatcher("/WEB-INF/jsp/customer.jsp").forward(req, resp);
    }
}
