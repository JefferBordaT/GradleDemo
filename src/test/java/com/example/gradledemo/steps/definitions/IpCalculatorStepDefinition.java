package com.example.gradledemo.steps.definitions;


import com.appgate.dtp.util.constants.Constants;
import com.appgate.dtp.util.enums.Context;
import com.appgate.dtp.util.report.ReportConfig;
import com.example.gradledemo.entities.Query;
import com.example.gradledemo.entities.Response;
import com.example.gradledemo.steps.AbstractStep;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class IpCalculatorStepDefinition {

    @Autowired
    AbstractStep step;
    Response response;

    public IpCalculatorStepDefinition() {
    }

    @Given("^publishes a message in kafka with the following data$")
    public void publishesAMessageInKafkaWithTheFollowingData(Query inputIpCalculator) {
        step.cleanMessagesBroker();
        step.setContext(Context.INPUT_MESSAGE, inputIpCalculator);
        step.sendMessage(step.getContext(Context.INPUT_MESSAGE, Query.class).toString());
        ReportConfig.generateReport(
            "Send message",
            step.getContext(Context.INPUT_MESSAGE, Query.class).toString());
    }

    @Given("^publishes a message empty in kafka with the following data$")
    public void publishesAMessageEmptyInKafkaWithTheFollowingData() {
        step.cleanMessagesBroker();
        step.sendMessage(Constants.EMPTY_BODY);
        ReportConfig.generateReport("Send message", Constants.EMPTY_BODY);
    }

    @And("^The message is sent to kafka$")
    public void TheMessageIsSentToKafka() throws JsonProcessingException {
        String message = step.getMessage(1);
        step.setContext(Context.OUTPUT_MESSAGE, message);
        ReportConfig.generateReport("receive message broker", message);
        response = step.getObjectMapper().readValue(message, Response.class);
    }

    @Then("^the data published in the topic is validated with the following data$")
    public void theDataPublishedInTheTopicIsValidatedWithTheFollowingData(Query inputIpCalculator) {
        assertThat(response, is(notNullValue()));
        assertThat(response.getData().getTraceabilityId(), is(inputIpCalculator.getTraceabilityId()));
        assertThat(response.getData().getUrl(), is(inputIpCalculator.getUrl()));
    }

    @And("^ip is not null$")
    public void ipIsNotNull() {
        assertThat(response.getData().getIp(), is(notNullValue()));
    }

    @And("^ip is null$")
    public void ipIsNull() {
        assertThat(response.getData().getIp(), is(nullValue()));
    }

    @And("^status Code is (\\d+)$")
    public void statusCodeIs(Integer statusCode) {
        assertThat(response.getStatusCode(), is(statusCode));
    }

    @And("^message is contains \"([^\"]*)\"$")
    public void messageIsContains(String message) {
        assertThat(response.getMessage(), is(containsString(message)));
    }

    @Then("^data is empty$")
    public void dataIsEmpty() {
        assertThat(response.getData().toString(), is(Constants.EMPTY_BODY));
    }
}
