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
 *
 * Loading some cases of options
 * Case is defined by "loadtype"
 * Load types:
 *  - toDisable: returns which options should be disabled, if this option is chosen
 *  used in new option adding, by dependencies settings
 *  For example: If option A is chosen in required, this option should be disabled in incompatible
 *
 *  - possibleOfTariff: load possible options of chosen tariff
 *  - newOptionDependency (else): load options that are used by chosen tariffs
 *  used in new option adding. Option cannot depends on option, those are not available for chosen tariffs
 */
@WebServlet("/load_options")
public class OptionLoaderController extends HttpServlet{

    OptionService service = OptionServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        JsonObject json = new JsonObject();
        String type = request.getParameter("loadtype");
        if ("toDisable".equals(type)) {
            Integer id = Integer.parseInt(request.getParameter("data"));

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
        }else if ("possibleOfTariff".equals(type)) {
            Integer tariffId = Integer.parseInt(request.getParameter("tariff_id"));
            TariffService tariffService = TariffServiceImpl.getInstance();
            EntityGraph<Option> graph = tariffService.getEntityGraph();

            graph.addAttributeNodes("possibleOptions");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            Tariff tariff = tariffService.loadByKey(tariffId, hints);

            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(tariff.getPossibleOptions());
            json.add("data", element);
        } else if ("getDependencies".equals(type)) {
            Integer id = Integer.parseInt(request.getParameter("data"));

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
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
