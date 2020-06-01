package com.xatu.gware.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractJsonpResponseBodyAdvice;

/**
 * @param
 * @return
 */

@ControllerAdvice(basePackages = {"com.xatu.gware"})
public class JsonpController extends AbstractJsonpResponseBodyAdvice {
    public JsonpController(){
        super("callback","jsonp");
    }

}
