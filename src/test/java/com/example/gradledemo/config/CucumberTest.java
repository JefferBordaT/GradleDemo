package com.example.gradledemo.config;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = {"classpath:feature"},
        glue = {"com.example.gradledemo","com.appgate.dtp.util"},
        tags = "@DTPIpCalculator"
)
public class CucumberTest {
}

