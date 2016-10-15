package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.ContractDto;
import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.repository.ContractRepository;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
//import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.testng.Assert.assertEquals;

/**
 * Created by alex on 14.10.16.
 */
//@RunWith(PowerMockRunner.class)
@PrepareForTest(DataBaseValidator.class)
public class ContractServiceImplTest extends PowerMockTestCase{

    private ContractService service;

    private ContractRepository contractRepository;

    @BeforeMethod
    public void setUp() throws Exception {
        contractRepository = mock(ContractRepository.class);

        Contract myContract = new Contract();

        Tariff tariff = new Tariff();
        tariff.setId(1);
        myContract.setTariff(tariff);

        Option option1 = new Option();
        option1.setConnectCost(BigDecimal.valueOf(5));
        option1.setId(1);

        Option option2 = new Option();
        option2.setConnectCost(BigDecimal.valueOf(10));
        option2.setId(2);

        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        myContract.setUsedOptions(options);

        when(contractRepository.findOne(1)).thenReturn(myContract);

        service = new ContractServiceImpl(contractRepository);

        mockStatic(DataBaseValidator.class);
        PowerMockito.doNothing().when(DataBaseValidator.class);
        DataBaseValidator.checkAllOptions(anyInt(), anyList());
    }

    @Test
    public void testUpdateContractSameOptions() throws Exception {
        Contract myContract = new Contract();
        myContract.setBalance(BigDecimal.valueOf(1000));
        Option option1 = new Option();
        option1.setConnectCost(BigDecimal.valueOf(5));
        option1.setId(1);

        Option option2 = new Option();
        option2.setConnectCost(BigDecimal.valueOf(10));
        option2.setId(2);

        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        myContract.setUsedOptions(options);
        when(contractRepository.saveAndFlush(any(Contract.class))).thenReturn(myContract);

        ContractDto contractDto = service.updateContract(1, 1, Arrays.asList(1, 2));
        assertEquals(contractDto.getBalance(), BigDecimal.valueOf(1000));
    }

    @Test
    public void testUpdateContractOneChange() throws Exception {
        Contract myContract = new Contract();
        myContract.setBalance(BigDecimal.valueOf(1000));
        Option option1 = new Option();
        option1.setConnectCost(BigDecimal.valueOf(5));
        option1.setId(1);

        Option option2 = new Option();
        option2.setConnectCost(BigDecimal.valueOf(10));
        option2.setId(3);

        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        myContract.setUsedOptions(options);
        when(contractRepository.saveAndFlush(any(Contract.class))).thenReturn(myContract);

        ContractDto contractDto = service.updateContract(1, 1, Arrays.asList(1, 3));
        assertEquals(contractDto.getBalance(), BigDecimal.valueOf(990));
    }

    @Test
    public void testUpdateContractAllChange() throws Exception {
        Contract myContract = new Contract();
        myContract.setBalance(BigDecimal.valueOf(1000));
        Option option1 = new Option();
        option1.setConnectCost(BigDecimal.valueOf(5));
        option1.setId(4);

        Option option2 = new Option();
        option2.setConnectCost(BigDecimal.valueOf(10));
        option2.setId(3);

        Set<Option> options = new HashSet<>();
        options.add(option1);
        options.add(option2);
        myContract.setUsedOptions(options);
        when(contractRepository.saveAndFlush(any(Contract.class))).thenReturn(myContract);

        ContractDto contractDto = service.updateContract(1, 1, Arrays.asList(4, 3));
        assertEquals(contractDto.getBalance(), BigDecimal.valueOf(985));
    }
}