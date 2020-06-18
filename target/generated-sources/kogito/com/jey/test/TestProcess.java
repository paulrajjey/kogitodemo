package com.jey.test;

import org.jbpm.process.core.datatype.impl.type.ObjectDataType;
import org.jbpm.ruleflow.core.RuleFlowProcessFactory;
import org.drools.core.util.KieFunctions;

@javax.enterprise.context.ApplicationScoped()
@javax.inject.Named("test")
public class TestProcess extends org.kie.kogito.process.impl.AbstractProcess<com.jey.test.TestModel> {

    @javax.inject.Inject()
    javax.enterprise.inject.Instance<org.kie.api.runtime.process.WorkItemHandler> handlers;

    com.jey.Application app;

    public TestProcess() {
    }

    @javax.inject.Inject()
    public TestProcess(com.jey.Application app) {
        super(app.config().process());
        this.app = app;
    }

    public com.jey.test.TestProcessInstance createInstance(com.jey.test.TestModel value) {
        return new com.jey.test.TestProcessInstance(this, value, this.createLegacyProcessRuntime());
    }

    public com.jey.test.TestProcessInstance createInstance(java.lang.String businessKey, com.jey.test.TestModel value) {
        return new com.jey.test.TestProcessInstance(this, value, businessKey, this.createLegacyProcessRuntime());
    }

    public com.jey.test.TestModel createModel() {
        return new com.jey.test.TestModel();
    }

    public com.jey.test.TestProcessInstance createInstance(org.kie.kogito.Model value) {
        return this.createInstance((com.jey.test.TestModel) value);
    }

    public com.jey.test.TestProcessInstance createInstance(java.lang.String businessKey, org.kie.kogito.Model value) {
        return this.createInstance(businessKey, (com.jey.test.TestModel) value);
    }

    public TestProcess configure() {
        super.configure();
        return this;
    }

    protected void registerListeners() {
    }

    public org.kie.api.definition.process.Process legacyProcess() {
        RuleFlowProcessFactory factory = RuleFlowProcessFactory.createProcess("test");
        factory.name("test");
        factory.packageName("com.jey.test");
        factory.dynamic(false);
        factory.version("1.0");
        factory.visibility("Public");
        factory.metaData("TargetNamespace", "http://www.omg.org/bpmn20");
        org.jbpm.ruleflow.core.factory.EndNodeFactory endNode1 = factory.endNode(1);
        endNode1.name("End");
        endNode1.terminate(false);
        endNode1.metaData("UniqueId", "_B4E73ED0-7AC7-40F1-B0B1-93A774176A34");
        endNode1.metaData("x", 500);
        endNode1.metaData("width", 56);
        endNode1.metaData("y", 181);
        endNode1.metaData("height", 56);
        endNode1.done();
        org.jbpm.ruleflow.core.factory.ActionNodeFactory actionNode2 = factory.actionNode(2);
        actionNode2.name("test");
        actionNode2.action(kcontext -> {
            System.out.println("test");;
        });
        actionNode2.metaData("UniqueId", "_2B12EDD6-679B-43E4-92E7-3F873B3576D7");
        actionNode2.metaData("elementname", "test");
        actionNode2.metaData("NodeType", "ScriptTask");
        actionNode2.metaData("x", 286);
        actionNode2.metaData("width", 154);
        actionNode2.metaData("y", 158);
        actionNode2.metaData("height", 102);
        actionNode2.done();
        org.jbpm.ruleflow.core.factory.StartNodeFactory startNode3 = factory.startNode(3);
        startNode3.name("Start");
        startNode3.interrupting(false);
        startNode3.metaData("UniqueId", "_272912EF-90ED-4E81-B56E-A66C4452E4EC");
        startNode3.metaData("x", 155);
        startNode3.metaData("width", 56);
        startNode3.metaData("y", 181);
        startNode3.metaData("height", 56);
        startNode3.done();
        factory.connection(2, 1, "_6F5B930C-790B-4BEF-A76E-19BFE534E77A");
        factory.connection(3, 2, "_8E45171B-3194-4507-A709-238E069403F2");
        factory.validate();
        return factory.getProcess();
    }

    public void init(@javax.enterprise.event.Observes() io.quarkus.runtime.StartupEvent event) {
        this.activate();
    }
}
