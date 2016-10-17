package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.dto.OptionDto;
import com.tsystems.javaschool.business.services.interfaces.OptionService;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.repository.OptionRepository;
import com.tsystems.javaschool.exceptions.JSException;
import com.tsystems.javaschool.exceptions.WrongOptionConfigurationException;
import com.tsystems.javaschool.util.DataBaseValidator;
import org.mockito.AdditionalAnswers;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * Created by alex on 07.10.16.
 */
@PrepareForTest(DataBaseValidator.class)
public class OptionServiceImplTest extends PowerMockTestCase {

    private OptionService service;

    @BeforeMethod
    public void setUp() throws Exception {
        OptionRepository optionRepository = mock(OptionRepository.class);
        //------ test options
        Option plainOption1 = new Option();
        plainOption1.setId(100);
        Option plainOption2 = new Option();
        plainOption2.setId(101);

        Set<Option> plainOptions = new HashSet<>();
        plainOptions.add(plainOption1);
        plainOptions.add(plainOption2);

        Option optionWithReq = new Option();
        optionWithReq.setId(1);
        optionWithReq.addRequiredFromOptions(plainOptions);

        Option optionWithForb = new Option();
        optionWithForb.setId(2);
        optionWithForb.addForbiddenWithOptions(plainOptions);

        Option optionRequiredMe = new Option();
        optionRequiredMe.setId(3);
        optionRequiredMe.addRequiredMeOptions(plainOptions);
        //------ end options

        when(optionRepository.findOne(1)).thenReturn(optionWithReq);
        when(optionRepository.findOne(2)).thenReturn(optionWithForb);
        when(optionRepository.findOne(3)).thenReturn(optionRequiredMe);
        when(optionRepository.findOne(100)).thenReturn(plainOption1);
        when(optionRepository.saveAndFlush(any(Option.class))).then(AdditionalAnswers.returnsFirstArg());

        service = new OptionServiceImpl(optionRepository);

        mockStatic(DataBaseValidator.class);
        doNothing().when(DataBaseValidator.class);
    }

    @AfterMethod
    public void tearDown() throws Exception {

    }

    /**
     * "#" (forbidden)
     * -> (required)
     * <p>
     * Add option to required with required:
     * new --> 1 --> 2
     * <p>
     * Expected:
     * ..new
     * ./   \
     * v     v
     * 1 --> 2
     */
    @Test
    public void testAddReqWithReq() throws Exception {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(1);
        TreeSet<OptionDto> optionDtoSet = new TreeSet<>();
        optionDtoSet.add(optionDto1);

        optionDto.setRequiredFrom(optionDtoSet);

        OptionDto savedDto = service.addNew(optionDto);

        // Check: return same object
        assertEquals(savedDto.getId(), Integer.valueOf(200));

        // Check: dependencies are added
        assertEquals(savedDto.getRequiredFrom().size(), 3);

        // Check: 'option1' is added
        assertTrue(savedDto.getRequiredFrom().stream().filter(e -> e.getId().equals(1)).findFirst().isPresent());

        // Check: 'plainOption's are added
        assertTrue(savedDto.getRequiredFrom().stream().filter(e -> e.getId().equals(100)).findFirst().isPresent());
        assertTrue(savedDto.getRequiredFrom().stream().filter(e -> e.getId().equals(101)).findFirst().isPresent());

        // Check: forbidden set is empty
        assertTrue(savedDto.getForbiddenWith().isEmpty());

        // Check: requiredMe set is empty
        assertTrue(savedDto.getRequiredMe().isEmpty());
    }

    /**
     * "#" (forbidden)
     * -> (required)
     * <p>
     * Add option to required with forbidden:
     * new -> 1 ## 2
     * <p>
     * Expected:
     * ..new
     * ./   #
     * 1 ### 2
     */
    @Test
    public void testAddReqWithForb() throws JSException {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(2);
        TreeSet<OptionDto> optionDtoSet = new TreeSet<>();
        optionDtoSet.add(optionDto1);

        optionDto.setRequiredFrom(optionDtoSet);

        OptionDto savedDto = service.addNew(optionDto);

        // Check: return same object
        assertEquals(savedDto.getId(), Integer.valueOf(200));

        // Check: dependencies are added
        assertEquals(savedDto.getForbiddenWith().size(), 2);
        assertEquals(savedDto.getRequiredFrom().size(), 1);

        // Check: 'option2' is added
        assertTrue(savedDto.getRequiredFrom().stream().filter(e -> e.getId().equals(2)).findFirst().isPresent());

        // Check: 'plainOption' is added
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(100)).findFirst().isPresent());
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(101)).findFirst().isPresent());

        // Check: requiredMe set is empty
        assertTrue(savedDto.getRequiredMe().isEmpty());
    }

    /**
     * Add 2 required options, that are forbidden
     * Expected: Exception errorCode = 2
     */
    @Test(expectedExceptions = {WrongOptionConfigurationException.class}, expectedExceptionsMessageRegExp = ".*Error code: 2.*")
    public void testAddReqReqAreForb() throws JSException {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(2);
        OptionDto optionDto2 = new OptionDto();
        optionDto2.setId(100);
        TreeSet<OptionDto> optionDtoSet = new TreeSet<>();
        optionDtoSet.add(optionDto1);
        optionDtoSet.add(optionDto2);

        optionDto.setRequiredFrom(optionDtoSet);

        OptionDto savedDto = service.addNew(optionDto);
    }

    /**
     * Add option, that is forbidden and required
     * <p>
     * Expected: Exception errorCode = 1
     */
    @Test(expectedExceptions = {WrongOptionConfigurationException.class}, expectedExceptionsMessageRegExp = ".*Error code: 1.*")
    public void testAddReqForb() throws JSException {
//        thrown.expect(WrongOptionConfigurationException.class);
//        thrown.expectMessage("Error code: 1. Check docs");

        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(2);
        TreeSet<OptionDto> optionReq = new TreeSet<>();
        optionReq.add(optionDto1);
        TreeSet<OptionDto> optionForb = new TreeSet<>();
        optionForb.add(optionDto1);

        optionDto.setRequiredFrom(optionReq);
        optionDto.setForbiddenWith(optionForb);

        OptionDto savedDto = service.addNew(optionDto);
    }


    /**
     * Expected: Exception errorCode = 3
     */
    @Test(expectedExceptions = {WrongOptionConfigurationException.class}, expectedExceptionsMessageRegExp = ".*Error code: 3.*")
    public void testAddReqForbAreReq() throws JSException {
//        thrown.expect(WrongOptionConfigurationException.class);
//        thrown.expectMessage("Error code: 3. Check docs");

        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(1);
        OptionDto optionDto2 = new OptionDto();
        optionDto2.setId(100);
        TreeSet<OptionDto> optionReq = new TreeSet<>();
        optionReq.add(optionDto1);
        TreeSet<OptionDto> optionForb = new TreeSet<>();
        optionForb.add(optionDto2);

        optionDto.setRequiredFrom(optionReq);
        optionDto.setForbiddenWith(optionForb);

        OptionDto savedDto = service.addNew(optionDto);
    }

    /**
     * "#" (forbidden)
     * -> (required)
     * <p>
     * Add option to forbidden with required:
     * new ### 1 --> 2
     * <p>
     * Expected:
     * ..new
     * .#   #
     * #     #
     * 1 --> 2
     */
    @Test
    public void testAddForbWithReq() throws JSException {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(1);
        TreeSet<OptionDto> optionDtoSet = new TreeSet<>();
        optionDtoSet.add(optionDto1);

        optionDto.setForbiddenWith(optionDtoSet);

        OptionDto savedDto = service.addNew(optionDto);

        // Check: return same object
        assertEquals(savedDto.getId(), Integer.valueOf(200));

        // Check: dependencies are added
        assertEquals(savedDto.getForbiddenWith().size(), 1);

        // Check: 'option1' is added
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(1)).findFirst().isPresent());

        // Check: forbidden set is empty
        assertTrue(savedDto.getRequiredFrom().isEmpty());

        // Check: requiredMe set is empty
        assertTrue(savedDto.getRequiredMe().isEmpty());
    }

    /**
     * "#" (forbidden)
     * -> (required)
     * <p>
     * Add option to forbidden with required:
     * new ### 1 <-- 2
     * <p>
     * Expected:
     * ..new
     * .#   #
     * #     #
     * 1 <-- 2
     */
    @Test
    public void testAddForbWithReqMe() throws JSException {
        OptionDto optionDto = new OptionDto();
        optionDto.setId(200);

        OptionDto optionDto1 = new OptionDto();
        optionDto1.setId(3);
        TreeSet<OptionDto> optionDtoSet = new TreeSet<>();
        optionDtoSet.add(optionDto1);

        optionDto.setForbiddenWith(optionDtoSet);

        OptionDto savedDto = service.addNew(optionDto);

        // Check: return same object
        assertEquals(savedDto.getId(), Integer.valueOf(200));

        // Check: dependencies are added
        assertEquals(savedDto.getForbiddenWith().size(), 3);

        // Check: 'option1' is added
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(3)).findFirst().isPresent());

        // Check: 'plainOption's are added
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(100)).findFirst().isPresent());
        assertTrue(savedDto.getForbiddenWith().stream().filter(e -> e.getId().equals(101)).findFirst().isPresent());

        // Check: forbidden set is empty
        assertTrue(savedDto.getRequiredFrom().isEmpty());

        // Check: requiredMe set is empty
        assertTrue(savedDto.getRequiredMe().isEmpty());
    }
}