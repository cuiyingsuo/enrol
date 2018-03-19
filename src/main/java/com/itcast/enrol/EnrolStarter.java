package com.itcast.enrol;

import com.itcast.enrol.common.annotation.SessionHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.system.ApplicationPidFileWriter;
import org.springframework.boot.system.EmbeddedServerPortFileWriter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.List;

/**
 * @Autohor liuzhongshuai
 * Created by liuzhongshuai on 2017/10/19.
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, RedisAutoConfiguration.class})
@EnableScheduling
public class EnrolStarter extends WebMvcConfigurerAdapter {


    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException, InterruptedException, ParseException {
        SpringApplication app = new SpringApplication(EnrolStarter.class);
        app.addListeners(new ApplicationPidFileWriter());
        app.addListeners(new EmbeddedServerPortFileWriter());
        app.run(args);

        /*String mobile = "18611816315";// userEx.getMobile();
        String password = "123456";// userEx.getPassword();
        //aes加密
        byte[] aesBytes = AesUtil.encrypt(mobile + password, "chuanzhienrol123456@.com");
        //保存用户
        String Str16 = AesUtil.parseByte2HexStr(aesBytes);
        System.out.print(Str16);*/
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        argumentResolvers.add(new SessionHandler());
    }
}
