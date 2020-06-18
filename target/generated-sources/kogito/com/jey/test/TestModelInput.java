package com.jey.test;

import java.util.Map;
import java.util.HashMap;

@org.kie.internal.kogito.codegen.Generated(value = "kogito-codegen", reference = "test", name = "Test", hidden = true)
public class TestModelInput implements org.kie.kogito.Model {

    @Override
    public Map<String, Object> toMap() {
        Map<String, Object> params = new HashMap();
        return params;
    }

    @Override
    public void fromMap(Map<String, Object> params) {
        fromMap(null, params);
    }

    public void fromMap(String id, Map<String, Object> params) {
    }
}
