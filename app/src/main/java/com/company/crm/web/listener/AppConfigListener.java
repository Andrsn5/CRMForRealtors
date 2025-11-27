package com.company.crm.web.listener;

import com.company.crm.config.AppConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppConfigListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        AppConfig config = new AppConfig();
        ctx.setAttribute("appConfig", config);
        System.out.println("AppConfig initialized and added to ServletContext");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // cleanup if needed
    }
}
