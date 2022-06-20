package com.sparta.cloneproject.responsedto;

import java.util.HashMap;
import java.util.Map;

public class ResponseMap extends JwtResponseDto{

    private Map responseData = new HashMap();

    public ResponseMap() {
        setResult(responseData);
    }

    public void setResponseData(String key, Object value){
        this.responseData.put(key, value);
    }
}