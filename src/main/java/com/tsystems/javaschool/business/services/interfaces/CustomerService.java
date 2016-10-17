package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.CustomerDto;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 *
 * Interface for customer service
 */
public interface CustomerService extends GenericService<CustomerDto, Integer> {
    void removeContract(Integer customerId, Integer contractId);
}
