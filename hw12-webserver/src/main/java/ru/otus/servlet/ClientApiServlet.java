package ru.otus.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DBServiceClient;
import ru.otus.services.TemplateProcessor;

import java.io.IOException;
import java.util.Arrays;

public class ClientApiServlet extends HttpServlet {

    private static final String PARAM_NAME = "name";
    private static final String PARAM_ADDRESS = "address";
    private static final String PARAM_PHONES = "phones";

    private final transient DBServiceClient serviceClient;
    private final transient TemplateProcessor templateProcessor;

    public ClientApiServlet(TemplateProcessor templateProcessor, DBServiceClient serviceClient) {
        this.templateProcessor = templateProcessor;
        this.serviceClient = serviceClient;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        var name = req.getParameter(PARAM_NAME);
        var address = req.getParameter(PARAM_ADDRESS);
        var phones = req.getParameter(PARAM_PHONES);
        var client = new Client(
                null,
                name,
                address.isEmpty() ? null : new Address(null, address),
                phones.isEmpty()
                        ? null
                        : Arrays.stream(phones.split("\n"))
                        .map(item -> new Phone(null, item))
                        .toList());

        serviceClient.saveClient(client);
        resp.sendRedirect("/clients");
    }

}
