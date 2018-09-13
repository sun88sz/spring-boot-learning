package com.sun.version;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

/**
 * @author Sun
 * @date : 2018/9/13 14:10
 */
@AllArgsConstructor
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {

    /**
     * extract the version part from url. example [v0-9]
     */
    private final static Pattern VERSION_PREFIX_PATTERN = Pattern.compile("/v(\\d+)/");

    @Getter
    private int apiVersion;

    @Override
    public ApiVersionCondition combine(ApiVersionCondition other) {
        // latest defined would be take effect, that means, methods definition with
        // override the classes definition
        return new ApiVersionCondition(other.getApiVersion());
    }

    @Override
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if (m.find()) {
            Integer version = Integer.valueOf(m.group(1));
            // when applying version number bigger than configuration, then it will take
            if (version >= this.apiVersion) {
                // effect
                return this;
            }
        }
        return null;
    }

    /**
     * 当匹配到多个mapping的时候，按此方法比较优先级
     *
     * @param other
     * @param request
     * @return
     */
    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // when more than one configured version number passed the match rule, then only the biggest one will take effect.
        // 优先匹配最大的版本号
        return other.getApiVersion() - this.apiVersion;
    }


}
