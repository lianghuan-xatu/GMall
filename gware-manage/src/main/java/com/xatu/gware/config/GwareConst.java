package com.xatu.gware.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @param
 * @return
 */
@Configuration
public class GwareConst {

    @Value("${order.split.url:novalue}")
    public static String ORDER_SPLIT_URL;
}
