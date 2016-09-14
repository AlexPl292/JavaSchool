package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.LogLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by alex on 10.09.16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TariffServiceImpl.class, TariffDaoImpl.class, EMU.class, OptionDaoImpl.class})
public class TariffServiceImplTest {
    @Before
    public void setUp() throws Exception {
        LogLog.setQuietMode(true);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addNew() throws Exception {
        PowerMockito.mockStatic(TariffDaoImpl.class);
        PowerMockito.mockStatic(OptionDaoImpl.class);
        PowerMockito.mockStatic(EMU.class);
        TariffDaoImpl mockTariffDao = mock(TariffDaoImpl.class);
        OptionDaoImpl mockOptionDao = mock(OptionDaoImpl.class);

        Mockito.when(TariffDaoImpl.getInstance()).thenReturn(mockTariffDao);
        Mockito.when(OptionDaoImpl.getInstance()).thenReturn(mockOptionDao);

        List<Integer> optionsId = new ArrayList<>();
        optionsId.add(1);
        optionsId.add(2);
        Set<Option> options = new HashSet<>();
        Option option1 = new Option(1, "x1", new BigDecimal("100"), new BigDecimal("200"), "Desc1");
        Option option2 = new Option(2, "x2", new BigDecimal("101"), new BigDecimal("201"), "Desc2");
        options.add(option1);
        options.add(option2);
        Mockito.when(mockOptionDao.loadOptionsByIds(optionsId)).thenReturn(options);

        Tariff tariff = new Tariff();
        tariff.setName("Test");
//        Mockito.when(mockTariffDao.create(tariff)).thenReturn();

        PowerMockito.doNothing().when(EMU.class, "closeEntityManager");
        PowerMockito.doNothing().when(EMU.class, "beginTransaction");
        PowerMockito.doNothing().when(EMU.class, "commit");
        PowerMockito.doNothing().when(EMU.class, "rollback");


        TariffServiceImpl service = TariffServiceImpl.getInstance();
        Tariff loadedTariff = service.addNew(tariff, optionsId);
        assertEquals(options, loadedTariff.getPossibleOptions());
    }
}