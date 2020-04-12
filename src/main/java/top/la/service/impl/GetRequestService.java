package top.la.service.impl;

import top.la.service.IGetRequestInvoker;
import top.la.service.IRequest;
import top.la.vo.GetRequestVO;

import java.util.Map;

/***
 *
 * @Author:fsn
 * @Date: 2020/4/11 19:10
 * @Description
 */


public class GetRequestService<T> implements IRequest {
    private GetRequestVO vo;

    public GetRequestService(GetRequestVO vo) {
        this.vo = vo;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void sync() {
        if (vo.getParamsBuilder() == null) {
            return;
        }
        Map<String, Object> result = vo.getParamsBuilder().build();
        vo.setParams((Map<String, String>)result.get("params"));
        vo.setHeaders((Map<String, String>)result.get("headers"));
        IGetRequestInvoker<T> request = new GetRequestInvokerService<>();
        request.get(vo.getUrl(), vo.getParams(), vo.getHeaders(), vo.getResult());
    }
}
