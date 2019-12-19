package com.sun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class ConverterConfig  {

    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @Bean
    public StringToDateConverter stringToDateConverter() {
        return new StringToDateConverter();
    }

//    @Bean
    public StringToLocalDateTimeConverter stringToLocalDateTimeConverter() {
        return new StringToLocalDateTimeConverter();
    }

//    @PostConstruct
//    public void initEditableValidation() {
//        if (handlerAdapter != null) {
//            ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) handlerAdapter.getWebBindingInitializer();
//            if (initializer != null && initializer.getConversionService() != null) {
//                GenericConversionService genericConversionService = (GenericConversionService) initializer.getConversionService();
//                genericConversionService.addConverter(stringToDateConverter());
//                genericConversionService.addConverter(stringToLocalDateTimeConverter());
//            }
//        }
//    }
//
//    @Override
//    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(stringToDateConverter());
////        registry.addConverter(stringToLocalDateTimeConverter());
//    }


    //
//    @Configuration
//    public class WebMvcConfig  {
//        /**
//         * 配置全局日期转换器
//         */
//        @Bean
//        @Autowired
//        public ConversionService getConversionService(DateConverter dateConverter){
//            ConversionServiceFactoryBean factoryBean = new ConversionServiceFactoryBean();
//
//            Set<Converter> converters = new HashSet<Converter>();
//
//            converters.add(dateConverter);
//
//            factoryBean.setConverters(converters);
//
//            return factoryBean.getObject();
//        }
//    }

//    @Bean
//    public Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean(@Autowired DateTimeJacksonConverter dateTimeJacksonConverter,
//                                                                           @Autowired DateJacksonConverter dateJacksonConverter) {
//        Jackson2ObjectMapperFactoryBean jackson2ObjectMapperFactoryBean = new Jackson2ObjectMapperFactoryBean();
//        jackson2ObjectMapperFactoryBean.setDeserializers(dateTimeJacksonConverter, dateJacksonConverter);
//        return jackson2ObjectMapperFactoryBean;
//    }
//
//    @Bean
//    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(@Autowired ObjectMapper objectMapper) {
//        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//        mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
//        return mappingJackson2HttpMessageConverter;
//    }

}
