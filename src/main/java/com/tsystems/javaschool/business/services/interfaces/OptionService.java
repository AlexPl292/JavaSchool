package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.OptionDto;

import java.util.List;

/**
 * Created by alex on 27.08.16.
 */
public interface OptionService extends GenericService<OptionDto, Integer> {
    List<OptionDto> findByName(String name);
}
