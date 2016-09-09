package com.tsystems.javaschool.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tsystems.javaschool.business.services.implementations.ContractServiceImpl;
import com.tsystems.javaschool.db.entities.Contract;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by alex on 09.09.16.
 */
@WebServlet("/editContract")
public class EditContractController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        JsonObject json = new JsonObject();
        Map<String, String> errors = new HashMap<>();

        Integer contractId = Integer.parseInt(request.getParameter("contract_id"));
        Integer tariffId = Integer.parseInt(request.getParameter("tariff"));
        List<Integer> options = Arrays.stream(request.getParameterValues("optionsEdit"+contractId)).map(Integer::parseInt).collect(Collectors.toList());
        Contract contract = ContractServiceImpl.getInstance().updateContract(contractId, tariffId, options);

        if (!errors.isEmpty()) {
            JsonElement element = new Gson().toJsonTree(errors);
            json.addProperty("success", false);
            json.add("errors", element);
        } else {
            json.addProperty("success", true);
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            JsonElement element = gson.toJsonTree(contract);
            json.add("data", element);
        }


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(json.toString());
        out.flush();
    }
}
