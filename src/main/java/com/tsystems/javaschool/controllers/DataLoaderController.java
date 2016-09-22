package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.business.services.implementations.CustomerServiceImpl;
import com.tsystems.javaschool.business.services.implementations.OptionServiceImpl;
import com.tsystems.javaschool.business.services.implementations.TariffServiceImpl;
import com.tsystems.javaschool.business.services.interfaces.*;
import com.tsystems.javaschool.util.TableResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by alex on 23.08.16.
 * <p>
 * Loading data. Also with pagination.
 * request parameters:
 * page (int) - page of pagination
 * updateCount (bool) - make query to update count of entries or not
 * search (string) - search query
 * <p>
 * response parameters:
 * draw - page in pagination
 * data - returned data
 */
//@WebServlet({"/admin/load_customers", "/load_tariffs", "/load_options_table", "/admin/load_contracts"})
@RestController
public class DataLoaderController {

    private static final Logger logger = Logger.getLogger(DataLoaderController.class);
//    @Autowired
//    CustomerService customerService;
    final TariffService tariffService;

    @Autowired
    public DataLoaderController(TariffService tariffService) {
        this.tariffService = tariffService;
    }
/*    @Autowired
    OptionService optionService;*/
//    @Autowired
//    ContractService contractService;

    @GetMapping(value = "/load_tariffs", produces="application/json")
    public TableResponse loadTariffs(@RequestParam boolean updateCount,
                            @RequestParam String page,
                            @RequestParam String search) {
        return load(tariffService, updateCount, page, search);
    }

/*    @GetMapping(value = "/load_options_table", produces="application/json")
    public TableResponse loadOptions(@RequestParam boolean updateCount,
                                     @RequestParam String page,
                                     @RequestParam String search) {
        return load(optionService, updateCount, page, search);
    }*/


    //@RequestMapping({"/admin/load_customers", "/load_options_table", "/admin/load_contracts"})
    protected TableResponse load(GenericService service, boolean updateCount, String page, String searchQuery) {

        List<Object> entitiesList;
        int draw = 1;
        long recordsTotal = -1;
        Map<String, Object> kwargs = new HashMap<>();
        kwargs.put("search", searchQuery);
        TableResponse tableResponse = new TableResponse();

/*        if ("/admin/load_customers".equals(url)) {  // Get service depends on path
            service = null;//customerService;
        } else if ("/load_tariffs".equals(url)) {
            service = tariffService;
        } else if ("/load_options_table".equals(url)) {
            service = null;//optionService;
        } else if ("/admin/load_contracts".equals(url)) {
            service = null;//contractService;
        } else {
            service = null;//customerService;
        }*/

        if (updateCount) {  // Should we update count of all users or not
            recordsTotal = service.count(kwargs);
//            json.addProperty("recordsTotal", recordsTotal);
            tableResponse.setRecordsTotal(recordsTotal);
        }


        if ("first".equals(page)) {  // Define accessible page
            draw = 1;
        } else if ("last".equals(page)) {
            recordsTotal = service.count(kwargs);
            draw = (int) Math.ceil(recordsTotal / 10.0);
        } else {
            if (page != null) { // TODO странная ошибка: иногда page приходит null. Более того, даже эта строчка не всегда помогает
                try {
                    draw = Integer.parseInt(page);
                } catch (NumberFormatException e) {
                    logger.error("Exception while page converting", e);
                    draw = 1;
                }
            }
        }

        kwargs.put("maxEntries", 10);
        kwargs.put("firstIndex", (draw - 1) * 10);

        if ("-1".equals(page))
            entitiesList = service.loadAll();
        else {
            entitiesList = service.load(kwargs);
        }

        tableResponse.setDraw(draw);
        tableResponse.setData(entitiesList);
        return tableResponse;

    }
}
