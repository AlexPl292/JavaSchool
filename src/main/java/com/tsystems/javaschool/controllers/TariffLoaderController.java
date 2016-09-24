package com.tsystems.javaschool.controllers;

import com.tsystems.javaschool.business.dto.TariffDto;
import com.tsystems.javaschool.business.services.interfaces.TariffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by alex on 24.09.16.
 */
@RestController
@RequestMapping("/admin/tariff")
public class TariffLoaderController {

    private final TariffService service;

    @Autowired
    public TariffLoaderController(TariffService service) {
        this.service = service;
    }

    @RequestMapping("/getAll")
    public List<TariffDto> loadAll() {
        return service.loadAll();
    }
}
