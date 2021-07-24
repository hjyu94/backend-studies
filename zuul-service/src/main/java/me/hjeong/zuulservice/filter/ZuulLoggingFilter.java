package me.hjeong.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException {
        log.info("************** printing logs: ");

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("************** " + request.getRequestURI());

        return null;
    }

    @Override
    public String filterType() {
        return "pre"; // 사전필터
    }

    @Override
    public int filterOrder() {
        return 1; // 여러 필터가 있다면 필터 순서 설정
    }

    @Override
    public boolean shouldFilter() {
        return true; // 필터로 쓰겠다는 섲렁
    }

}
