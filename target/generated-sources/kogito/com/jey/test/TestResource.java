package com.jey.test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.kie.api.runtime.process.WorkItemNotFoundException;
import org.kie.kogito.Application;
import org.kie.kogito.auth.SecurityPolicy;
import org.kie.kogito.process.Process;
import org.kie.kogito.process.ProcessInstance;
import org.kie.kogito.process.ProcessInstanceExecutionException;
import org.kie.kogito.process.WorkItem;
import org.kie.kogito.process.workitem.Policy;
import org.kie.kogito.process.impl.Sig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jey.test.TestModel;
import com.jey.test.TestModelOutput;

@Path("/test")
@javax.enterprise.context.ApplicationScoped()
public class TestResource {

    @javax.inject.Inject()
    @javax.inject.Named("test")
    Process<TestModel> process;

    @javax.inject.Inject()
    Application application;

    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public TestModelOutput createResource_test(@Context HttpHeaders httpHeaders, @QueryParam("businessKey") String businessKey, @javax.validation.Valid() @javax.validation.constraints.NotNull() TestModelInput resource) {
        if (resource == null) {
            resource = new TestModelInput();
        }
        final TestModelInput value = resource;
        return org.kie.kogito.services.uow.UnitOfWorkExecutor.executeInUnitOfWork(application.unitOfWorkManager(), () -> {
            ProcessInstance<TestModel> pi = process.createInstance(businessKey, mapInput(value, new TestModel()));
            String startFromNode = httpHeaders.getHeaderString("X-KOGITO-StartFromNode");
            if (startFromNode != null) {
                pi.startFrom(startFromNode);
            } else {
                pi.start();
            }
            return getModel(pi);
        });
    }

    @GET()
    @Produces(MediaType.APPLICATION_JSON)
    public List<TestModelOutput> getResources_test() {
        return process.instances().values().stream().map(pi -> mapOutput(new TestModelOutput(), pi.variables())).collect(Collectors.toList());
    }

    @GET()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TestModelOutput getResource_test(@PathParam("id") String id) {
        return process.instances().findById(id).map(pi -> mapOutput(new TestModelOutput(), pi.variables())).orElse(null);
    }

    @DELETE()
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TestModelOutput deleteResource_test(@PathParam("id") final String id) {
        return org.kie.kogito.services.uow.UnitOfWorkExecutor.executeInUnitOfWork(application.unitOfWorkManager(), () -> {
            ProcessInstance<TestModel> pi = process.instances().findById(id).orElse(null);
            if (pi == null) {
                return null;
            } else {
                pi.abort();
                return getModel(pi);
            }
        });
    }

    @POST()
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TestModelOutput updateModel_test(@PathParam("id") String id, TestModel resource) {
        return org.kie.kogito.services.uow.UnitOfWorkExecutor.executeInUnitOfWork(application.unitOfWorkManager(), () -> {
            ProcessInstance<TestModel> pi = process.instances().findById(id).orElse(null);
            if (pi == null) {
                return null;
            } else {
                pi.updateVariables(resource);
                return mapOutput(new TestModelOutput(), pi.variables());
            }
        });
    }

    @GET()
    @Path("/{id}/tasks")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getTasks_test(@PathParam("id") String id, @QueryParam("user") final String user, @QueryParam("group") final List<String> groups) {
        return process.instances().findById(id).map(pi -> pi.workItems(policies(user, groups))).map(l -> l.stream().collect(Collectors.toMap(WorkItem::getId, WorkItem::getName))).orElse(null);
    }

    protected TestModelOutput getModel(ProcessInstance<TestModel> pi) {
        if (pi.status() == ProcessInstance.STATE_ERROR && pi.error().isPresent()) {
            throw new ProcessInstanceExecutionException(pi.id(), pi.error().get().failedNodeId(), pi.error().get().errorMessage());
        }
        return mapOutput(new TestModelOutput(), pi.variables());
    }

    protected Policy[] policies(String user, List<String> groups) {
        if (user == null) {
            return new Policy[0];
        }
        org.kie.kogito.auth.IdentityProvider identity = null;
        if (user != null) {
            identity = new org.kie.kogito.services.identity.StaticIdentityProvider(user, groups);
        }
        return new Policy[] { SecurityPolicy.of(identity) };
    }

    protected TestModel mapInput(@javax.validation.Valid() @javax.validation.constraints.NotNull() TestModelInput input, TestModel resource) {
        resource.fromMap(input.toMap());
        return resource;
    }

    protected TestModelOutput mapOutput(TestModelOutput output, TestModel resource) {
        output.fromMap(resource.getId(), resource.toMap());
        return output;
    }
}
