/*
 * Licensed Materials - Property of IBM
 * © Copyright IBM Corporation 2015. All Rights Reserved.
 */

package com.ibm.mil.cafejava;

import android.support.annotation.NonNull;
import android.support.annotation.StringDef;

import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponseListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation for invoking a procedure from a Java based adapter.
 *
 * @see JSProcedureInvoker
 *
 * @author John Petitto  (github @jpetitto)
 * @author Tanner Preiss (github @t-preiss)
 */
public final class JavaProcedureInvoker implements ProcedureInvoker {
    /** StringDef for basic HTTP method types: {@code GET, POST, PUT, DELETE}. */
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            GET,
            POST,
            PUT,
            DELETE
    })
    public @interface HttpMethod {}
    /** Annotated with the {@code HttpMethod} StringDef */
    public static final String GET = WLResourceRequest.GET;
    /** Annotated with the {@code HttpMethod} StringDef */
    public static final String POST = WLResourceRequest.POST;
    /** Annotated with the {@code HttpMethod} StringDef */
    public static final String PUT = WLResourceRequest.PUT;
    /** Annotated with the {@code HttpMethod} StringDef */
    public static final String DELETE = WLResourceRequest.DELETE;

    private String adapterName;
    private String path;
    private HashMap<String, String> pathParams;
    private HashMap<String, String> queryParams;
    private @HttpMethod String httpMethod;

    private JavaProcedureInvoker(String adapterName, String path) {
        this.adapterName = adapterName;
        this.path = path;
    }

    @Override
    public void invoke(WLResponseListener wlResponseListener) {
        try {
            URI uri = new URI("adapters/" + adapterName + buildPath(path, pathParams));
            WLResourceRequest request = new WLResourceRequest(uri, httpMethod);
            request.setQueryParameters(queryParams);
            request.send(pathParams, wlResponseListener);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String buildPath(String path, Map<String, String> params) {
        // insert initial forward slash if missing
        if (!path.startsWith("/")) {
            path = "/" + path;
        }

        // inject each path param into path
        for (Map.Entry<String, String> param : params.entrySet()) {
            // replace param name delimited by curly braces with param value
            path = path.replace("{" + param.getKey() + "}", param.getValue());
        }

        return path;
    }

    /** Configures and instantiates a {@code JavaProcedureInvoker}. */
    public static class Builder {
        private final String adapterName;
        private final String path;
        private HashMap<String, String> pathParams = new HashMap<>();
        private HashMap<String, String> queryParams = new HashMap<>();
        private @HttpMethod String httpMethod = GET;

        public Builder(String adapterName, String path) {
            this.adapterName = adapterName;
            this.path = path;
        }

        public Builder pathParam(@NonNull String name, @NonNull String value) {
            pathParams.put(name, value);
            return this;
        }

        public Builder pathParams(@NonNull Map<String, String> params) {
            pathParams.putAll(params);
            return this;
        }

        public Builder queryParam(@NonNull String name, @NonNull String value) {
            queryParams.put(name, value);
            return this;
        }

        public Builder queryParams(@NonNull Map<String, String> params) {
            queryParams.putAll(params);
            return this;
        }

        /** Expects an {@code HttpMethod} StringDef constant value. Default is {@code GET}. */
        public Builder httpMethod(@NonNull @HttpMethod String httpMethod) {
            this.httpMethod = httpMethod;
            return this;
        }

        public JavaProcedureInvoker build() {
            JavaProcedureInvoker invoker = new JavaProcedureInvoker(adapterName, path);
            invoker.pathParams = pathParams;
            invoker.queryParams = queryParams;
            invoker.httpMethod = httpMethod;
            return invoker;
        }
    }

}
