package com.arnold.SmartFramework.Bean;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Request {

    private String requestPath;

    private String requestMethod;

    public String getRequestPath() {
        return requestPath;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public Request(String requestPath, String requestMethod) {
        this.requestPath = requestPath;
        this.requestMethod = requestMethod;
    }

    @Override
    public boolean equals(Object o) {
        /*if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Request request = (Request) o;

        return new EqualsBuilder()
                .append(requestPath, request.requestPath)
                .append(requestMethod, request.requestMethod)
                .isEquals();*/
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
        /*return new HashCodeBuilder(17, 37)
                .append(requestPath)
                .append(requestMethod)
                .toHashCode();*/
    }
}
