package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.server.ClientWebServer;
import ru.otus.server.SecuredWebServer;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

public class WebServerSimpleDemo {
    private static final int WEB_SERVER_PORT = 8081;
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {


        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        DBServiceClient initialDB = initialDB();
        createInitialClients(initialDB);
        UserDao userDao = new InMemoryUserDao();
        UserAuthService authService = new UserAuthServiceImpl(userDao);
        ClientWebServer clientWebServer = new SecuredWebServer(WEB_SERVER_PORT, gson, authService, initialDB, templateProcessor);
        clientWebServer.start();
        clientWebServer.join();
    }

    private static DBServiceClient initialDB() {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        ///
        return new DbServiceClientImpl(transactionManager, clientTemplate);

    }


    private static void createInitialClients(DBServiceClient dbServiceClient) {
        dbServiceClient.saveClient(new Client("Ruslan"));
        dbServiceClient.saveClient(new Client("sasha"));
    }
}
