package top.la.vo;

import top.la.service.IParamsBuilder;
import top.la.service.IResult;

import java.util.Map;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/11 18:15
 * @Description
 */


public class GetRequestVO<T> {
    /**
     *  请求url
     */
    private String url;
    /**
     *  请求参数
     */
    private Map<String, String> params;
    /**
     *  请求头
     */
    private Map<String, String> headers;
    /**
     *  请求结果
     */
    private IResult result;
    /**
     *  参数构造
     */
    private IParamsBuilder paramsBuilder;

    public GetRequestVO(String url, Map<String, String> params, Map<String, String> headers, IResult<T> result) {
        this.params = params;
        this.url = url;
        this.headers = headers;
        this.result = result;
    }

    public GetRequestVO(String url, IResult<T> result, IParamsBuilder paramsBuilder) {
        this.url = url;
        this.result = result;
        this.paramsBuilder = paramsBuilder;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public IResult getResult() {
        return result;
    }

    public void setResult(IResult result) {
        this.result = result;
    }

    public IParamsBuilder getParamsBuilder() {
        return paramsBuilder;
    }

    public void setParamsBuilder(IParamsBuilder paramsBuilder) {
        this.paramsBuilder = paramsBuilder;
    }
}
