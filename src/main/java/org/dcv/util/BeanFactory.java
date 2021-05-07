package org.dcv.util;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static javax.validation.Validation.buildDefaultValidatorFactory;

//@Slf4j
public class BeanFactory {
//    @Inject
//    private ConfigurationProvider configuration;

//    public BeanFactory() {
//        log.info("providePrivateKey: {}", configuration.getMapDbFile().toString());
//    }
    //    @Produces
//    @ApplicationScoped
//    public ValidatorFactory getValidatorFactory() {
//        return buildDefaultValidatorFactory();
//    }

//    @Produces
//    @ApplicationScoped
//    public PrivateKeyProvider getPrivateKeyProvider() {
//        return new PrivateKeyProvider();
//    }

    @Produces
    @ApplicationScoped
    public Validator getValidator() {
        final ValidatorFactory factory = buildDefaultValidatorFactory();
        return factory.getValidator();
    }

//    public DB getMapDb() {
//
//    }
}
