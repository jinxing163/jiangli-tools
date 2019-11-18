package com.jiangli.commons.jtest.core.data.handler;


import com.jiangli.commons.jtest.core.InvokeContext;
import com.jiangli.commons.jtest.core.data.DataCollector;

/**
 * @author Jiangli
 * @date 2017/12/28 11:18
 */
public interface DataHandler {
    void handler(InvokeContext context, DataCollector model);
}
