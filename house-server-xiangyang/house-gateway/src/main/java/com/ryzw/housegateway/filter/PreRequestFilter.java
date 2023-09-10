package com.ryzw.housegateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.ryzw.housegateway.entity.OperationLog;
import com.ryzw.housegateway.service.OperationLogService;
import com.ryzw.housegateway.util.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

/**
 * <p>
 * Zuul前置驱动
 * </p>
 *
 * @author yz
 * @since 2018/11/27
 */
@Component
@Slf4j
public class PreRequestFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext cxt = RequestContext.getCurrentContext();
        HttpServletRequest request = cxt.getRequest();
        if (!(request.getRequestURL().toString().toLowerCase().contains("/operationLog/list".toLowerCase()))) {
            OperationLog operationLog = new OperationLog();
            operationLog.setUserName(request.getRemoteUser());
            operationLog.setMethod(request.getMethod());
            operationLog.setUrl(request.getRequestURL().toString());
            operationLog.setBody(readAsChars(request));
            operationLog.setOperationTime(new Date());
            OperationLogService operationLogService = SpringUtils.getBean(OperationLogService.class);
            operationLogService.save(operationLog);
        }
        return null;
    }

    /**
     * 从HttpServletRequest中读取Body
     *
     * @param request
     * @return
     */
    public String readAsChars(HttpServletRequest request) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            log.error("ReadAsChars Error", e);
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error("ReadAsChars Close Error", e);
                }
            }
        }
        return sb.toString();
    }
}
