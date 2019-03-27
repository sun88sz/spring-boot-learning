package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 */
@JobHandler(value = "demo2JobHandler")
@Component
public class Demo2JobHandler extends IJobHandler {

    Logger log = LoggerFactory.getLogger(Demo2JobHandler.class);

    @Override
    public ReturnT<String> execute(String param) throws Exception {

        log.info("Demo 2, Hello World.");
        XxlJobLogger.log("Demo 2, Hello World.");

        return SUCCESS;
    }
}
