package org.dcv.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
//@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    //public class WebConfig extends WebMvcConfigurerAdapter {
//    private MdcServletInterceptor mdcServletInterceptor;

//    public WebConfig(final MdcServletInterceptor mdcServletInterceptor) {
//        this.mdcServletInterceptor = mdcServletInterceptor;
//    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {

//        registry.addInterceptor(mdcServletInterceptor);
        log.info("addInterceptors");
        registry.addInterceptor(new MdcServletInterceptor());
    }
}
