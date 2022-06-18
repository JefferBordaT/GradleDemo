package com.example.gradledemo.config;


import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
        features = {"classpath:feature"},
        glue = {"com.appgate.dtp","com.example"},
        tags = "@DTPIpCalculator"
)

public class CucumberTest {
}

