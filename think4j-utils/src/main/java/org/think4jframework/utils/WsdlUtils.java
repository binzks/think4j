package org.think4jframework.utils;

import org.apache.axis.client.Service;

import javax.xml.namespace.QName;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.encoding.XMLType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by zhoubin on 15/9/22.
 * wsdl调用公共方法
 */
public class WsdlUtils {

    public static String getString(String address, String nameSpace, String method, Map<String, Object> params) throws Exception {
        Service service = new Service();
        Call call = (Call) service.createCall();
        call.setTargetEndpointAddress(address);
        call.setOperationName(new QName(nameSpace, method));
        // 设置入参
        List<Object> values = new ArrayList<>();
        for (Entry<String, Object> entry : params.entrySet()) {
            call.addParameter(entry.getKey(), XMLType.XSD_STRING, ParameterMode.IN);
            values.add(entry.getValue());
        }
        // 设置返回值格式
        call.setReturnType(XMLType.XSD_STRING);
        // 接口调用
        return (String) call.invoke(values.toArray());
    }
}
