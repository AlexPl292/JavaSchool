package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Customer;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.helpers.LogLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import javax.persistence.EntityGraph;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by alex on 13.09.16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ContractDaoImpl.class, EMU.class, OptionDaoImpl.class, ContractServiceImpl.class})
public class ContractServiceImplTest {

    private OptionDaoImpl mockOptionDao;
    private ContractDaoImpl mockContractDao;
    private Contract contract;

    @Before
    public void setUp() throws Exception {
        LogLog.setQuietMode(true);
        PowerMockito.mockStatic(ContractDaoImpl.class);
        PowerMockito.mockStatic(OptionDaoImpl.class);
        PowerMockito.mockStatic(EMU.class);
        mockContractDao = mock(ContractDaoImpl.class);
        mockOptionDao = mock(OptionDaoImpl.class);

//        Mockito.when(ContractDaoImpl.getInstance()).thenReturn(mockContractDao);
//        Mockito.when(OptionDaoImpl.getInstance()).thenReturn(mockOptionDao);

        contract = Mockito.spy(Contract.class);
        contract.setNumber("123");
        contract.setIsBlocked(0);
        contract.setBalance(new BigDecimal(300));
        Set<Option> usedOptions = new HashSet<>();
        Option option2 = new Option(2, "x2", new BigDecimal("101"), new BigDecimal("201"), "Desc2");
        Option option3 = new Option(3, "x3", new BigDecimal("103"), new BigDecimal("203"), "Desc3");
        usedOptions.add(option2);
        usedOptions.add(option3);
        contract.setUsedOptions(usedOptions);
        Tariff tariff = new Tariff(1, "123", new BigDecimal("100"), "desc");
        contract.setTariff(tariff);
        contract.setCustomer(new Customer());
        contract.setId(1);
    }

    @After
    public void tearDown() throws Exception {
        Mockito.reset(mockOptionDao);
        Mockito.reset(mockContractDao);
    }

    @Test
    public void updateContract() throws Exception {
/*        powermockito.mockstatic(tariffdaoimpl.class);
        tariffdaoimpl mocktariffdao = mock(tariffdaoimpl.class);

        //Mockito.when(TariffDaoImpl.getInstance()).thenReturn(mockTariffDao);

        List<Integer> optionsId = new ArrayList<>();
        optionsId.add(1);
        optionsId.add(2);
        Set<Option> options = new HashSet<>();
        Option option1 = new Option(1, "x1", new BigDecimal("100"), new BigDecimal("200"), "Desc1");
        Option option2 = new Option(2, "x2", new BigDecimal("101"), new BigDecimal("201"), "Desc2");
        options.add(option1);
        options.add(option2);
        Tariff tariff = new Tariff(2, "124", new BigDecimal("101"), "desc2");

        Mockito.when(mockOptionDao.loadOptionsByIds(anyListOf(Integer.class))).thenReturn(options);
        Mockito.when(mockContractDao.read(anyInt(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(contract);
        Mockito.when(mockTariffDao.read(anyInt())).thenReturn(tariff);


        ContractServiceImpl service = Mockito.spy(ContractServiceImpl.getInstance());
        EntityGraph<Contract> graph = mock(EntityGraph.class);
        doReturn(graph).when(service).getEntityGraph();
        Contract updated = service.updateContract(1, 2, optionsId);
        assertEquals(tariff, updated.getTariff());
        assertEquals(options, updated.getUsedOptions());*/
    }

    @Test
    public void addNew() throws Exception {
        List<Integer> optionsId = new ArrayList<>();
        optionsId.add(1);
        optionsId.add(2);
        Set<Option> options = new HashSet<>();
        Option option1 = new Option(1, "x1", new BigDecimal("100"), new BigDecimal("200"), "Desc1");
        Option option2 = new Option(2, "x2", new BigDecimal("101"), new BigDecimal("201"), "Desc2");
        options.add(option1);
        options.add(option2);
        Mockito.when(mockOptionDao.loadOptionsByIds(anyListOf(Integer.class))).thenReturn(options);


        PowerMockito.doNothing().when(EMU.class, "closeEntityManager");
        PowerMockito.doNothing().when(EMU.class, "beginTransaction");
        PowerMockito.doNothing().when(EMU.class, "commit");
        PowerMockito.doNothing().when(EMU.class, "rollback");


        ContractServiceImpl service = ContractServiceImpl.getInstance();
        Contract loadedContract = service.addNew(contract, optionsId);
        assertEquals(options, loadedContract.getUsedOptions());
    }
}