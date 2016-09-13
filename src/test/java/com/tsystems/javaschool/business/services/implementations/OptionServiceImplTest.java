package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by alex on 13.09.16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({EMU.class, OptionDaoImpl.class, TariffDaoImpl.class})
public class OptionServiceImplTest {
    @Before
    public void setUp() throws Exception {
        LogLog.setQuietMode(true);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addNew() throws Exception {
        PowerMockito.mockStatic(OptionDaoImpl.class);
        PowerMockito.mockStatic(TariffDaoImpl.class);
        PowerMockito.mockStatic(EMU.class);
        OptionDaoImpl mockOptionDao = mock(OptionDaoImpl.class);
        TariffDaoImpl mockTariffDao = mock(TariffDaoImpl.class);

        Mockito.when(OptionDaoImpl.getInstance()).thenReturn(mockOptionDao);
        Mockito.when(TariffDaoImpl.getInstance()).thenReturn(mockTariffDao);
        Tariff tariff = new Tariff(1, "name", new BigDecimal(100), "desc");
        Mockito.when(mockTariffDao.read(anyInt())).thenReturn(tariff);

        PowerMockito.doNothing().when(EMU.class, "closeEntityManager");
        PowerMockito.doNothing().when(EMU.class, "beginTransaction");
        PowerMockito.doNothing().when(EMU.class, "commit");
        PowerMockito.doNothing().when(EMU.class, "rollback");

        Option option1 = new Option(1, "one", new BigDecimal(100), new BigDecimal(200), "desc1");
        Option option2 = new Option(2, "two", new BigDecimal(200), new BigDecimal(400), "desc2");
        option2.addRequiredFromOptions(option1);
        Option option3 = new Option(3, "three", new BigDecimal(300), new BigDecimal(500), "desc3");

        OptionServiceImpl service = Mockito.spy(OptionServiceImpl.getInstance());
        EntityGraph<Option> graph = mock(EntityGraph.class);
        doReturn(graph).when(service).getEntityGraph();

//        when(mockOptionDao.read(1, anyMap())).thenReturn(option1);
//        when(mockOptionDao.read(2, anyMap())).thenReturn(option2);
        Mockito.when(mockOptionDao.read(anyInt(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(option1);
        Mockito.when(mockOptionDao.read(anyInt(), Mockito.anyMapOf(String.class, Object.class))).thenReturn(option2);

        Map<String, String[]> dep = new HashMap<>();
        dep.put("forTariffs", new String[]{"1"});
        dep.put("requiredFrom", new String[]{"2"});
        dep.put("forbiddenWith", new String[]{});

        Option newOpt = service.addNew(option3, dep);
        assertEquals(2, newOpt.getRequired().size());
    }
}