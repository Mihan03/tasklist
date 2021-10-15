package ru.project.tasklist.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.SessionManagementFilter;
import ru.project.tasklist.auth.filter.AuthTokenFilter;
import ru.project.tasklist.auth.filter.ExceptionHandlerFilter;
import ru.project.tasklist.auth.service.UserDetailsServiceImpl;

import javax.servlet.FilterRegistration;

@Configuration
@EnableWebSecurity
public class SpringConfig extends WebSecurityConfigurerAdapter {

    // для получения пользователя из БД
    private UserDetailsServiceImpl userDetailsService;
    private AuthTokenFilter authTokenFilter;
    private ExceptionHandlerFilter exceptionHandlerFilter;

    @Autowired
    public void setUserDetailsService(UserDetailsServiceImpl userDetailsService) { // внедряем наш компонент Spring @Service
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setExceptionHandlerFilter(ExceptionHandlerFilter exceptionHandlerFilter) { // внедряем наш компонент Spring @Service
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // используем стандартный готовый authenticationManager из Spring контейнера (используется для проверки логина-пароля)
    // эти методы доступны в документации Spring Security - оттуда их можно копировать, чтобы не писать вручную
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // указываем наш сервис userDetailsService для проверки пользователя в БД и кодировщик паролей
    // эти методы доступны в документации Spring Security - оттуда их можно копировать, чтобы не писать вручную
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception { // настройки AuthenticationManager для правильной проверки логин-пароль
        authenticationManagerBuilder.
                userDetailsService(userDetailsService). // использовать наш сервис для загрузки User из БД
                passwordEncoder(passwordEncoder()); // указываем, что используется кодировщик пароля (для корректной проверки пароля)
    }

    @Autowired
    public void setAuthTokenFilter(AuthTokenFilter authTokenFilter) {
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public FilterRegistrationBean registration(AuthTokenFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // порядок следования настроек внутри метода - неважен

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* если используется другая клиентская технология (не SpringMVC, а например Angular, React и пр.),
            то выключаем встроенную Spring-защиту от CSRF атак,
            иначе запросы от клиента не будут обрабатываться, т.к. Spring Security будет пытаться в каждом входящем запроcе искать спец. токен для защиты от CSRF
        */
        http.csrf().disable(); // на время разработки проекта не будет ошибок (для POST, PUT и др. запросов) - недоступен и т.д.


        http.formLogin().disable(); // отключаем, т.к. форма авторизации создается не на Spring технологии (например, Spring MVC + JSP), а на любой другой клиентской технологии
        http.httpBasic().disable(); // отключаем стандартную браузерную форму авторизации

        http.requiresChannel().anyRequest().requiresSecure(); // обязательное исп. HTTPS

        http.addFilterBefore(authTokenFilter, SessionManagementFilter.class);

        http.addFilterBefore(exceptionHandlerFilter, AuthTokenFilter.class);
    }
}
