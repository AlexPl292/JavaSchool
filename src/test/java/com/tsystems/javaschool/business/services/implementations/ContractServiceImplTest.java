package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.ContractRepository;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by alex on 14.10.16.
 */
public class ContractServiceImplTest {

    private ContractService service;

    @Before
    public void setUp() throws Exception {
        ContractRepository contractRepository = mock(ContractRepository.class);

        Contract myContract = new Contract();
        myContract.setId(1);
        myContract.setTariff(new Tariff());

        service = new ContractServiceImpl(contractRepository);
    }

    @Test
    public void updateContract() throws Exception {

    }
}