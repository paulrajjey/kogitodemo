package com.jey.test;

public class TestProcessInstance extends org.kie.kogito.process.impl.AbstractProcessInstance<TestModel> {

    public TestProcessInstance(com.jey.test.TestProcess process, TestModel value, org.kie.api.runtime.process.ProcessRuntime processRuntime) {
        super(process, value, processRuntime);
    }

    public TestProcessInstance(com.jey.test.TestProcess process, TestModel value, java.lang.String businessKey, org.kie.api.runtime.process.ProcessRuntime processRuntime) {
        super(process, value, businessKey, processRuntime);
    }

    protected java.util.Map<String, Object> bind(TestModel variables) {
        return variables.toMap();
    }

    protected void unbind(TestModel variables, java.util.Map<String, Object> vmap) {
        variables.fromMap(this.id(), vmap);
    }
}
