package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import com.tsystems.javaschool.db.entities.Option;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityGraph;
import javax.servlet.http.HttpServlet;
import java.util.*;

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
@RestController
//@RequestMapping("/load_option")
@RequestMapping("/admin/option")
public class OptionLoaderController extends HttpServlet {

    private final OptionService service;
    private static final Logger logger = Logger.getLogger(OptionLoaderController.class);

    @Autowired
    public OptionLoaderController(OptionService service) {
        this.service = service;
    }

    @RequestMapping("/getAll")
    public List<OptionDto> loadAdd() {
        return service.loadAll();
    }

    @RequestMapping(produces="application/json")
    public ResponseHelper loadOptions(@RequestParam String loadtype,
                                     @RequestParam(required = false) Integer data,
                                     @RequestParam(required = false) String type,
                                     @RequestParam(required = false, value = "tariff_id") Integer tariffId,
                                     @RequestParam(required = false, value = "forTariffs") List<Integer> tariffs) {

//        JsonObject json = new JsonObject();
//        String type = request.getParameter("loadtype");
        ResponseHelper rh = new ResponseHelper();
        if ("toDisable".equals(loadtype)) {
            Integer id = data;

            EntityGraph<Option> graph = service.getEntityGraph();
            Set<OptionDto> notForbidden = new HashSet<>();
            Set<OptionDto> notRequiredFrom;

            graph.addAttributeNodes("required", "forbidden", "requiredMe");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            OptionDto connectedToOption = service.loadByKey(id, hints);

            if ("requiredFrom".equals(type)) {
                notForbidden = new HashSet<>(connectedToOption.getRequiredFrom());
                notRequiredFrom = new HashSet<>(connectedToOption.getForbiddenWith());
                notForbidden.add(connectedToOption);  // Add ref to itself
            } else {// "if forbidden
                notRequiredFrom = new HashSet<>(connectedToOption.getRequiredMe());
                notRequiredFrom.add(connectedToOption);
            }

//            JsonElement elementNotForbidden = new Gson().toJsonTree(notForbidden);
//            JsonElement elementNotRequiredFrom = new Gson().toJsonTree(notRequiredFrom);
//            json.add("not_forbidden", elementNotForbidden);
//            json.add("not_required_from", elementNotRequiredFrom);
            rh.setNot_forbidden(notForbidden);
            rh.setNot_required_from(notRequiredFrom);
        } else if ("possibleOfTariff".equals(loadtype)) {

//            TariffService tariffService = TariffServiceImpl.getInstance();
            TariffService tariffService = new TariffServiceImpl(null, null);
            EntityGraph graph = tariffService.getEntityGraph();

            graph.addAttributeNodes("possibleOptions");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            TariffDto tariff = tariffService.loadByKey(tariffId, hints);

            JsonElement element = new Gson().toJsonTree(tariff.getPossibleOptions());
//            json.add("data", element);
            rh.setData(new ArrayList<>(tariff.getPossibleOptions()));
        } else if ("getDependencies".equals(loadtype)) {
            Integer id = data;

            EntityGraph<Option> graph = service.getEntityGraph();

            graph.addAttributeNodes("required", "forbidden");
            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", graph);

            OptionDto option = service.loadByKey(id, hints);


            Set<OptionDto> required = option.getRequiredFrom();
            if (required == null)
                required = new HashSet<>();

            Set<OptionDto> forbidden = option.getForbiddenWith();
            if (forbidden == null)
                forbidden = new HashSet<>();

//            json.add("required", new Gson().toJsonTree(required));
//            json.add("forbidden", new Gson().toJsonTree(forbidden));
            rh.setRequired(required);
            rh.setForbidden(forbidden);
        } else {
//            List<Integer> tariffs = Arrays.stream(request.getParameterValues("forTariffs")).map(Integer::parseInt).collect(Collectors.toList());
            List<OptionDto> options = service.loadOptionsByTariffs(tariffs!=null? tariffs:new ArrayList<Integer>());

//            JsonElement element = new Gson().toJsonTree(options);
//            json.add("data", element);
            rh.setData(options);

        }

        return rh;

/*        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.print(json.toString());
            out.flush();
        } catch (IOException e) {
            logger.error("Get writer exception!", e);
        }*/
    }

    class ResponseHelper {
        List<OptionDto> data;
        Set<OptionDto> required;
        Set<OptionDto> forbidden;
        Set<OptionDto> not_forbidden;
        Set<OptionDto> not_required_from;

        public List<OptionDto> getData() {
            return data;
        }

        public void setData(List<OptionDto> data) {
            this.data = data;
        }

        public Set<OptionDto> getRequired() {
            return required;
        }

        public void setRequired(Set<OptionDto> required) {
            this.required = required;
        }

        public Set<OptionDto> getForbidden() {
            return forbidden;
        }

        public void setForbidden(Set<OptionDto> forbidden) {
            this.forbidden = forbidden;
        }

        public Set<OptionDto> getNot_forbidden() {
            return not_forbidden;
        }

        public void setNot_forbidden(Set<OptionDto> not_forbidden) {
            this.not_forbidden = not_forbidden;
        }

        public Set<OptionDto> getNot_required_from() {
            return not_required_from;
        }

        public void setNot_required_from(Set<OptionDto> not_required_from) {
            this.not_required_from = not_required_from;
        }
    }
}
