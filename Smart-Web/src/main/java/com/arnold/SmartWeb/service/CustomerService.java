package com.arnold.SmartWeb.service;

import com.arnold.SmartFramework.annotation.Transaction;
import com.arnold.SmartFramework.helper.DatabaseHelper;
import com.arnold.SmartWeb.helper.DatabasePoolHelper;
import com.arnold.SmartWeb.model.Customer;

import java.util.List;
import java.util.Map;

public class CustomerService {

    /*public List<Customer> getCustomerList() {
        //使用threadLocal后不用
        //Connection connection = DatabaseHelper.getConnection();
        String sql = "select * from customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }*/

    public List<Customer> getCustomerList() {
        String sql = "select * from customer";
        return DatabasePoolHelper.queryEntityList(Customer.class, sql);
    }

    @Transaction
    public boolean createCustomer(Map<String, Object> fieldMap) {
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }


}
