package com.playground.tesseract;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.sun.org.apache.bcel.internal.util.ClassPath;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

@Log4j
public class TesseractPlaygroundLambdaHandleTest {

    TesseractPlaygroundLambdaHandle subject = new TesseractPlaygroundLambdaHandle();

    @Test
    public void shoulHandlRequest() throws IOException {

        File file = new File(getClass().getClassLoader().getResource("test1.txt").getFile());
        String inputText = FileUtils.readFileToString(file, "UTF-8");

        APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
        event.setBody(inputText);
        APIGatewayProxyResponseEvent responseEvent = subject.handleRequest(event, null);

        log.info(responseEvent.getBody());

    }
}