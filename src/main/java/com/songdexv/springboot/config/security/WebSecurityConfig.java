package com.songdexv.springboot.config.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityAuthorizeMode;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.ConsensusBased;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.header.writers.HstsHeaderWriter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.songdexv.springboot.constant.Constant;

/**
 * Created by songdexv on 2017/8/7.
 */
@Configuration
@EnableConfigurationProperties(WebSecurityConfigProperties.class)
@ConditionalOnClass({EnableWebSecurity.class, AuthenticationEntryPoint.class})
@ConditionalOnExpression("${web.security.autoconfig:false}")
@ConditionalOnWebApplication
@EnableWebSecurity
public class WebSecurityConfig {
    private static List<String> DEFAULT_IGNORED = Arrays.asList("/css/**", "/js/**",
            "/images/**", "/**/favicon.ico");

    @Autowired
    private static WebSecurityConfigProperties webSecurityConfigProperties;

    @Bean
    @ConditionalOnMissingBean({IgnoredPathsWebSecurityConfigurerAdapter.class})
    public IgnoredPathsWebSecurityConfigurerAdapter ignoredPathsWebSecurityConfigurerAdapter() {
        return new IgnoredPathsWebSecurityConfigurerAdapter();
    }

    public static void configureHeaders(HeadersConfigurer<?> configurer,
                                        SecurityProperties.Headers headers) throws Exception {
        if (headers.getHsts() != SecurityProperties.Headers.HSTS.NONE) {
            boolean includeSubdomains = headers.getHsts() == SecurityProperties.Headers.HSTS.ALL;
            HstsHeaderWriter writer = new HstsHeaderWriter(includeSubdomains);
            writer.setRequestMatcher(AnyRequestMatcher.INSTANCE);
            configurer.addHeaderWriter(writer);
        }
        if (!headers.isContentType()) {
            configurer.contentTypeOptions().disable();
        }
        if (!headers.isXss()) {
            configurer.xssProtection().disable();
        }
        if (!headers.isCache()) {
            configurer.cacheControl().disable();
        }
        if (!headers.isFrame()) {
            configurer.frameOptions().disable();
        }
    }

    public static List<String> getIgnored(SecurityProperties security) {
        List<String> ignored = new ArrayList<String>(security.getIgnored());
        if (ignored.isEmpty()) {
            ignored.addAll(DEFAULT_IGNORED);
        } else if (ignored.contains("none")) {
            ignored.remove("none");
        }
        return ignored;
    }

    // Get the ignored paths in early
    @Order(SecurityProperties.IGNORED_ORDER)
    private static class IgnoredPathsWebSecurityConfigurerAdapter
            implements WebSecurityConfigurer<WebSecurity> {

        @Autowired(required = false)
        private ErrorController errorController;

        @Autowired
        private SecurityProperties security;

        @Autowired
        private ServerProperties server;

        @Override
        public void configure(WebSecurity builder) throws Exception {
        }

        @Override
        public void init(WebSecurity builder) throws Exception {
            List<String> ignored = getIgnored(this.security);
            if (this.errorController != null) {
                ignored.add(normalizePath(this.errorController.getErrorPath()));
            }
            String[] paths = this.server.getPathsArray(ignored);
            if (!ObjectUtils.isEmpty(paths)) {
                builder.ignoring().antMatchers(paths);
            }
        }

        private String normalizePath(String errorPath) {
            String result = StringUtils.cleanPath(errorPath);
            if (!result.startsWith("/")) {
                result = "/" + result;
            }
            return result;
        }

    }

    @Configuration
    @ConditionalOnExpression("${web.security.autoconfig:false}")
    @ConditionalOnProperty(prefix = "security.basic", name = "enabled", havingValue = "false")
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    protected static class ApplicationNoWebSecurityConfigurerAdapter
            extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.requestMatcher(new RequestMatcher() {
                @Override
                public boolean matches(HttpServletRequest request) {
                    return false;
                }
            });
        }
    }

    @Configuration
    @ConditionalOnExpression("${web.security.autoconfig:false}")
    @ConditionalOnProperty(prefix = "security.basic", name = "enabled", matchIfMissing = true)
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    protected static class ApplicationWebSecurityConfigurerAdapter
            extends WebSecurityConfigurerAdapter {

        @Autowired
        private SecurityProperties security;

        @Autowired
        private CustomizedFiltersInjector customizedFilters;

        @Bean(autowire = Autowire.BY_TYPE)
        @ConditionalOnMissingBean(CustomizedFiltersInjector.class)
        public CustomizedFiltersInjector customizedFiltersInjector() {
            return new CustomizedFiltersInjector();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if (this.security.isRequireSsl()) {
                http.requiresChannel().anyRequest().requiresSecure();
            }
            if (!this.security.isEnableCsrf()) {
                http.csrf().disable();
            }
            // No cookies for application endpoints by default
            http.sessionManagement().sessionCreationPolicy(this.security.getSessions());
            SpringBootWebSecurityConfiguration.configureHeaders(http.headers(),
                    this.security.getHeaders());
            String[] paths = getSecureApplicationPaths();
            if (paths.length > 0) {
                AuthenticationEntryPoint entryPoint = customEntryPoint();
                http.exceptionHandling().authenticationEntryPoint(entryPoint);
                http.httpBasic().authenticationEntryPoint(entryPoint);
                http.requestMatchers().antMatchers(paths);
                String[] roles = this.security.getUser().getRole().toArray(new String[0]);
                SecurityAuthorizeMode mode = this.security.getBasic().getAuthorizeMode();
                if (mode == null || mode == SecurityAuthorizeMode.ROLE) {
                    http.authorizeRequests().anyRequest().hasAnyRole(roles);
                } else if (mode == SecurityAuthorizeMode.AUTHENTICATED) {
                    http.authorizeRequests().anyRequest().authenticated();
                }
            }
            http.addFilterBefore(customPreAuthenticatedProcessingFilter(),
                    AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(responseInjectCsrfHeaderFilter(), CsrfFilter.class)
                    .addFilterBefore(filterSecurityInterceptor(), FilterSecurityInterceptor.class);

            if (customizedFilters != null) {
                for (CustomizedFilter customizedFilter : customizedFilters.list) {
                    if (customizedFilter.isBefore()) {
                        http.addFilterBefore(customizedFilter.getFilter(), customizedFilter.getPositionFlag());
                    } else {
                        http.addFilterAfter(customizedFilter.getFilter(), customizedFilter.getPositionFlag());
                    }
                }
            }

        }

        private String[] getSecureApplicationPaths() {
            List<String> list = new ArrayList<String>();
            for (String path : this.security.getBasic().getPath()) {
                path = (path == null ? "" : path.trim());
                if (path.equals("/**")) {
                    return new String[] {path};
                }
                if (!path.equals("")) {
                    list.add(path);
                }
            }
            return list.toArray(new String[list.size()]);
        }

        /**
         * entryPoint()是spring-security默认的切面,这里我们不用,我们用passportEntryPoint()
         *
         * @return
         */
        private AuthenticationEntryPoint entryPoint() {
            BasicAuthenticationEntryPoint entryPoint = new BasicAuthenticationEntryPoint();
            entryPoint.setRealmName(this.security.getBasic().getRealm());
            return entryPoint;
        }

        public CustomEntryPoint customEntryPoint() {
            CustomEntryPoint entryPoint = new CustomEntryPoint();
            entryPoint.setLoginUrl(webSecurityConfigProperties.getLoginUrl());
            return entryPoint;
        }

        public CustomPreAuthenticatedProcessingFilter customPreAuthenticatedProcessingFilter() {
            CustomPreAuthenticatedProcessingFilter passportPreAuthenticatedProcessingFilter =
                    new CustomPreAuthenticatedProcessingFilter();
            passportPreAuthenticatedProcessingFilter.setSessionDataGetter(sessionDataGetter());
            passportPreAuthenticatedProcessingFilter.setAuthenticationManager(authenticationManager);
            return passportPreAuthenticatedProcessingFilter;
        }

        public ResponseInjectCsrfHeaderFilter responseInjectCsrfHeaderFilter() {
            ResponseInjectCsrfHeaderFilter responseInjectCsrfHeaderFilter =
                    new ResponseInjectCsrfHeaderFilter(webSecurityConfigProperties);
            return responseInjectCsrfHeaderFilter;
        }

        @Autowired
        RoleService securityRoleService;

        @Autowired
        AuthenticationManager authenticationManager;

        @Bean(autowire = Autowire.BY_NAME)
        @ConditionalOnMissingBean(name = "authenticationManager")
        public AuthenticationManager authenticationManager() {
            NonPasswordUserDetailsService nonPasswordUserDetailsService = new
                    NonPasswordUserDetailsService();
            nonPasswordUserDetailsService.setRoleService(securityRoleService);
            nonPasswordUserDetailsService.setDefaultRoleCode(Constant.DEFAULT_ROLE_NAME);
            nonPasswordUserDetailsService.setDefaultRole(Constant.DEFAULT_ROLE_NAME);
            // 参数1:authenticationManager
            NonPasswordDaoAuthenticationProvider nonPasswordDaoAuthenticationProvider = new
                    NonPasswordDaoAuthenticationProvider();
            nonPasswordDaoAuthenticationProvider.setUserDetailsService(nonPasswordUserDetailsService);

            List<AuthenticationProvider> authenticationProviderList = new ArrayList<AuthenticationProvider>();
            authenticationProviderList.add(nonPasswordDaoAuthenticationProvider);
            AuthenticationManager authenticationManager = new ProviderManager(authenticationProviderList);
            return authenticationManager;
        }

        /**
         * 决策器支持,一般security的决策器有三种.默认unanimous
         * AffirmativeBased：只要有一个voter投同意票，就授权成功
         * ConsensusBased：只要投同意票的大于投反对票的，就授权成功
         * UnanimousBased：需要一致通过才授权成功具体决策规则很简单，只是根据voter返回的结果做处理
         * 这个 accessDecisionManager跟 ${web.security.autoconfig.decision} 是绑定
         */
        @Autowired
        AccessDecisionManager accessDecisionManager;

        @Bean(autowire = Autowire.BY_NAME)
        @ConditionalOnMissingBean(name = "accessDecisionManager")
        AccessDecisionManager accessDecisionManager() {
            // 参数2:affirmativeBased ConsensusBased UnanimousBased等决策器
            org.springframework.security.access.vote.RoleVoter roleVoter = new org.springframework.security.access.vote
                    .RoleVoter();
            roleVoter.setRolePrefix("");
            AuthenticatedVoter authenticatedVoter = new AuthenticatedVoter();
            List<AccessDecisionVoter<? extends Object>> vos = new ArrayList<AccessDecisionVoter<? extends Object>>();
            vos.add(roleVoter);
            vos.add(authenticatedVoter);

            if (StringUtils.isEmpty(this.decision)) {
                UnanimousBased unanimousBased = new UnanimousBased(vos);
                return unanimousBased; // 默认unanimousBased
            }
            if ("affirmative".equals(this.decision)) {
                AffirmativeBased affirmativeBased = new AffirmativeBased(vos);
                return affirmativeBased;
            } else if ("consensus".equals(this.decision)) {
                ConsensusBased consensusBased = new ConsensusBased(vos);
                return consensusBased;
            } else if ("unanimous".equals(this.decision)) {
                UnanimousBased unanimousBased = new UnanimousBased(vos);
                return unanimousBased;
            }
            // 其他的什么的,全部报错
            throw new AccessDeniedException("web.security.autoconfig.decision is undefined, use "
                    + "affirmative|consensus|unanimous'");
        }

        // filterInvocationSecurityMetadataSource 元数据器,支持通过url得到设定的权限
        @Autowired
        private FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource;

        @Bean(autowire = Autowire.BY_NAME)
        @ConditionalOnMissingBean(name = "filterInvocationSecurityMetadataSource")
        FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
            JdbcFilterInvocationDefinitionSource jdbcFilterInvocationDefinitionSource =
                    new JdbcFilterInvocationDefinitionSource();
            jdbcFilterInvocationDefinitionSource.setRoleService(securityRoleService);
            jdbcFilterInvocationDefinitionSource.setDefaultRole(Constant.DEFAULT_ROLE_NAME);
            return jdbcFilterInvocationDefinitionSource;
        }

        public FilterSecurityInterceptor filterSecurityInterceptor() {
            FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();

            filterSecurityInterceptor.setAuthenticationManager(authenticationManager); // 验证器

            filterSecurityInterceptor.setAccessDecisionManager(accessDecisionManager); // 决策器

            // 参数 3:securityMetadataSource.. 必须是FilterInvocationSecurityMetadataSource类型
            filterSecurityInterceptor.setSecurityMetadataSource(filterInvocationSecurityMetadataSource); // 元数据器
            return filterSecurityInterceptor;
        }

        /**
         * 此处SessionDataGetter只有一个类customPreAuthenticatedProcessingFilter使用,不用注册到spring容器里面去共享
         * <p/>
         * 但是SessionDataGetterImpl实现了InitializingBean,在spring容器中会进行afterPropertiesSet操作,所以还是定义成一个bean.
         *
         * @return
         */
        @Bean
        @ConditionalOnMissingBean(SessionDataGetterImpl.class)
        public SessionDataGetter sessionDataGetter() {
            return new SessionDataGetterImpl();
        }

        /**
         * 决策器的类型,默认就是空值
         */
        @Value("${web.security.decision:}")
        private String decision;

        public String getDecision() {
            return decision;
        }

        public void setDecision(String decision) {
            this.decision = decision;
        }
    }
}
