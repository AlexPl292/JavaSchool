package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.business.dto.CustomerDto;

import java.util.List;

/**
 * Created by alex on 19.08.16.
 *
 * Interface for customer service
 */
public interface CustomerService extends GenericService<CustomerDto, Integer> {
    /**
     * Remove contract of customer
     * @param customerId  id of customer
     * @param contractId id of contract
     */
    void removeContract(Integer customerId, Integer contractId);
}
