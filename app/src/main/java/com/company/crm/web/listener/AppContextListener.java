package com.company.crm.web.listener;

import com.company.crm.config.AppConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

public class AppContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        AppConfig config = new AppConfig();
        ServletContext ctx = sce.getServletContext();
        ctx.setAttribute("appConfig", config);
    }
}
