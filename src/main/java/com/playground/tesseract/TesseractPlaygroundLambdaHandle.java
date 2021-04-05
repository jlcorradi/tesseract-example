package com.playground.tesseract;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import lombok.extern.log4j.Log4j;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Base64;

@Log4j
public class TesseractPlaygroundLambdaHandle implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        Base64.Decoder decoder = Base64.getDecoder();
        String inputBody = input.getBody();

        Tesseract tesseract = new Tesseract();
        //tesseract.setDatapath("/home/jorgecorradi/Downloads/");
        try {
            String decodedText = tesseract.doOCR(decodeToImage(inputBody));
            return createResponse(decodedText);
        } catch (TesseractException e) {
            e.printStackTrace();
            return createResponse(e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent createResponse(String responseContent) {
        APIGatewayProxyResponseEvent responseEvent = new APIGatewayProxyResponseEvent();
        responseEvent.setBody(responseContent);
        responseEvent.setStatusCode(200);
        return responseEvent;
    }

    public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            log.error("Error decoding base64 to image", e);
        }
        return image;
    }

}

