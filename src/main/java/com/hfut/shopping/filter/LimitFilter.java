package com.hfut.shopping.filter;

import java.util.concurrent.TimeUnit;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.hfut.shopping.limitUtil.TokenBucket;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class LimitFilter extends ZuulFilter{
	
	private static TokenBucket bucket=new TokenBucket(10000, 100, TimeUnit.SECONDS, 1);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		if(!bucket.get()) {
			RequestContext context = RequestContext.getCurrentContext();
			context.setSendZuulResponse(true);
		}
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return 10;
	}

}
