package top.la.service;

import top.la.vo.ResponseVo;

import java.util.Map;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/12 1:40
 * @Description
 */


public interface IGetRequestInvoker<T> {

    ResponseVo<T> get(String url, Map<String, String> params, Map<String, String> headers, IResult result);
}
