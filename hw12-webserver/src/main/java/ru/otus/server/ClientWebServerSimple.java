package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.TemplateProcessor;
import ru.otus.servlet.ClientApiServlet;
import ru.otus.servlet.ClientsServlet;

public class ClientWebServerSimple implements ClientWebServer {

    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final DBServiceClient dbServiceClient;

    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;

    public ClientWebServerSimple(DBServiceClient dbServiceClient, Gson gson, TemplateProcessor templateProcessor, int port) {
        this.dbServiceClient = dbServiceClient;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/clients"));

        server.setHandler(sequence);
    }

    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new ClientsServlet(templateProcessor, dbServiceClient)), "/clients");
        servletContextHandler.addServlet(new ServletHolder(new ClientApiServlet(templateProcessor, dbServiceClient)), "/client/create");

        return servletContextHandler;
    }
}
