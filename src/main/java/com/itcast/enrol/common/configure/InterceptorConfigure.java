package com.itcast.enrol.common.configure;

import com.itcast.enrol.common.interceptor.LoginInterceptorForManager;
import com.itcast.enrol.common.interceptor.LoginInterceptorForStudent;
import com.itcast.enrol.common.interceptor.PermissInterceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @Author liuzhongshuai
 * Created by liuzhongshuai on 2017/10/20.
 */
@Configuration
public class InterceptorConfigure extends WebMvcConfigurerAdapter {


    /**
     * 前端登录验证
     *
     * @return
     */
    @Bean
    public HandlerInterceptor studentLoginInterceptor() {
        return new LoginInterceptorForStudent();
    }

    /**
     * 后端登录验证
     *
     * @return
     */
    @Bean
    public HandlerInterceptor managerLoginInterceptor() {
        return new LoginInterceptorForManager();
    }

    /**
     * 管理端用户鉴权
     *
     * @return
     */
    @Bean
    public HandlerInterceptor permissInteceptor() {
        return new PermissInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 前台登录验证
        registry.addInterceptor(studentLoginInterceptor()).addPathPatterns("/**/orderController/**");
        registry.addInterceptor(studentLoginInterceptor()).addPathPatterns("/**/myInfoController/**");
        registry.addInterceptor(studentLoginInterceptor()).addPathPatterns("/**/payController/**");
        registry.addInterceptor(studentLoginInterceptor()).addPathPatterns("/**/invoiceController/**");
        registry.addInterceptor(studentLoginInterceptor()).addPathPatterns("/**/contractController/**");
        //后台登录验证
        registry.addInterceptor(managerLoginInterceptor()).addPathPatterns("/**/managenment/**")
                .excludePathPatterns("/**/managenment/user/userLogin")
                .excludePathPatterns("/**/managenment/user/validateCodeRef");

        //后台用户鉴权
        registry.addInterceptor(permissInteceptor()).addPathPatterns("/**/managenment/**")
                .excludePathPatterns("/**/managenment/user/userLogin")
                .excludePathPatterns("/**/managenment/user/validateCodeRef")
                .excludePathPatterns("/**/managenment/user/modifyPassword")
                .excludePathPatterns("/**/managenment/common/**")
                .excludePathPatterns("/**/managenment/banner/queryBanners")
                .excludePathPatterns("/**/managenment/banner/getBanner")
                .excludePathPatterns("/**/managenment/beschool/queryBeSchoolStus")
                .excludePathPatterns("/**/managenment/beschool/stuDetail")
                .excludePathPatterns("/**/managenment/beschool/getBeSchoolForClass")
                .excludePathPatterns("/**/managenment/beschool/stuClassDetail")
                .excludePathPatterns("/**/managenment/class/queryClassList")
                .excludePathPatterns("/**/managenment/goods/queryGoodsList")
                .excludePathPatterns("/**/managenment/goods/goodsDetail")
                .excludePathPatterns("/**/managenment/goods/queryUsersBydepart")
                .excludePathPatterns("/**/managenment/goods/getStuInfoByEms")
                .excludePathPatterns("/**/managenment/marketing/queryMarketing")
                .excludePathPatterns("/**/managenment/marketing/marketDetail")
                .excludePathPatterns("/**/managenment/marketing/marketInit")
                .excludePathPatterns("/**/managenment/orderMain/queryOrderMains")
                .excludePathPatterns("/**/managenment/orderMain/orderMainDetail")
                .excludePathPatterns("/**/managenment/orderMain/getReceipt")
                .excludePathPatterns("/**/managenment/payFlow/queryPayFlow")
                .excludePathPatterns("/**/managenment/config/selectAcceptCamputs")
                .excludePathPatterns("/**/managenment/school/querySchoolStus")
                .excludePathPatterns("/**/managenment/school/showStuByClass")
                .excludePathPatterns("/**/managenment/school/getBeSchoolForClass")
                .excludePathPatterns("/**/managenment/school/stuDetail")
                .excludePathPatterns("/**/managenment/subject/queryList")
                .excludePathPatterns("/**/managenment/invoice/queryInvoice")
                .excludePathPatterns("/**/managenment/invoice/getInvoiceDetail")
                .excludePathPatterns("/**/managenment/contract/queryAbleContract")
                .excludePathPatterns("/**/managenment/contract/queryUnableContract")
                .excludePathPatterns("/**/managenment/org/queryOrgs")
                .excludePathPatterns("/**/managenment/invoice/recordInvoiceNo");

        super.addInterceptors(registry);
    }


}
