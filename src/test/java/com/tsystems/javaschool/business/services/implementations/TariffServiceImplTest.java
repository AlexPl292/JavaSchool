package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.db.entities.Tariff;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.util.EMU;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.SuppressCode.suppressConstructor;

/**
 * Created by alex on 10.09.16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TariffServiceImpl.class, TariffDaoImpl.class, EMU.class})
@SuppressStaticInitializationFor("com.tsystems.javaschool.util.EMU")
public class TariffServiceImplTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void loadByKey() throws Exception {
        PowerMockito.mockStatic(TariffDaoImpl.class);
        PowerMockito.mockStatic(EMU.class);
        TariffDaoImpl mockDao = mock(TariffDaoImpl.class);

        Mockito.when(TariffDaoImpl.getInstance()).thenReturn(mockDao);
        Tariff tariff = new Tariff();
        tariff.setName("xxx");
        Mockito.when(mockDao.read(1)).thenReturn(tariff);
        PowerMockito.doNothing().when(EMU.class, "closeEntityManager");


        TariffServiceImpl service = TariffServiceImpl.getInstance();
        Tariff loadedTariff = service.loadByKey(1);
        assertEquals("xxx", loadedTariff.getName());
    }

}