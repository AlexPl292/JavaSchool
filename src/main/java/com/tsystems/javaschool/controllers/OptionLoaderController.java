package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import org.apache.log4j.Logger;

import javax.persistence.EntityGraph;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by alex on 01.09.16.
 * <p>
 * Loading some cases of options
 * Case is defined by "loadtype"
 * Load types:
 * - toDisable: returns which options should be disabled, if this option is chosen
 * used in new option adding, by dependencies settings
 * For example: If option A is chosen in required, this option should be disabled in incompatible
 * <p>
 * - possibleOfTariff: load possible options of chosen tariff
 * - newOptionDependency (else): load options that are used by chosen tariffs
 * used in new option adding. Option cannot depends on option, those are not available for chosen tariffs
 */
//@WebServlet("/load_option")
public class OptionLoaderController extends HttpServlet {

    private final transient OptionService service = OptionServiceImpl.getInstance();
    private static final Logger logger = Logger.getLogger(OptionLoaderController.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject json = new JsonObject();
        String type = request.getParameter("loadtype");
        if ("toDisable".equals(type)) {
            Integer id = 0;
            try {
                id = Integer.parseInt(request.getParameter("data"));
            } catch (NumberFormatException e) {
                logger.error("Wrong id format!", e);
            }

            EntityGraph<Option> graph = service.getEntityGraph();
            Set<Option> notForbidden = new HashSet<>();
            Set<Option> notRequiredFrom;

            graph.addAttributeNodes("required", "forbidden", "requiredMe");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Option connectedToOption = service.loadByKey(id, hints);

            if ("requiredFrom".equals(request.getParameter("type"))) {
                notForbidden = new HashSet<>(connectedToOption.getRequired());
                notRequiredFrom = new HashSet<>(connectedToOption.getForbidden());
                notForbidden.add(connectedToOption);  // Add ref to itself
            } else {// "if forbidden
                notRequiredFrom = new HashSet<>(connectedToOption.getRequiredMe());
                notRequiredFrom.add(connectedToOption);
            }

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement elementNotForbidden = gson.toJsonTree(notForbidden);
            JsonElement elementNotRequiredFrom = gson.toJsonTree(notRequiredFrom);
            json.add("not_forbidden", elementNotForbidden);
            json.add("not_required_from", elementNotRequiredFrom);
        } else if ("possibleOfTariff".equals(type)) {
            Integer tariffId = 0;
            try {
                tariffId = Integer.parseInt(request.getParameter("tariff_id"));
            } catch (NumberFormatException e) {
                logger.error("Wrong id format!", e);
            }

//            TariffService tariffService = TariffServiceImpl.getInstance();
            TariffService tariffService = new TariffServiceImpl(null);
            EntityGraph graph = tariffService.getEntityGraph();

            graph.addAttributeNodes("possibleOptions");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Tariff tariff = tariffService.loadByKey(tariffId, hints);

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(tariff.getPossibleOptions());
            json.add("data", element);
        } else if ("getDependencies".equals(type)) {
            Integer id = 0;
            try {
                id = Integer.parseInt(request.getParameter("data"));
            } catch (NumberFormatException e) {
                logger.error("Wrong id format!", e);
            }

            EntityGraph<Option> graph = service.getEntityGraph();

            graph.addAttributeNodes("required", "forbidden");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Option option = service.loadByKey(id, hints);

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

            Set<Option> required = option.getRequired();
            if (required == null)
                required = new HashSet<>();

            Set<Option> forbidden = option.getForbidden();
            if (forbidden == null)
                forbidden = new HashSet<>();

            json.add("required", gson.toJsonTree(required));
            json.add("forbidden", gson.toJsonTree(forbidden));
        } else {
            List<Integer> tariffs = Arrays.stream(request.getParameterValues("forTariffs")).map(Integer::parseInt).collect(Collectors.toList());
            List<Option> options = service.loadOptionsByTariffs(tariffs);

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(options);
            json.add("data", element);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        } catch (IOException e) {
            logger.error("Get writer exception!", e);
        }
    }
}
