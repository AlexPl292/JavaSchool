package com.tsystems.javaschool.util;

import com.tsystems.javaschool.db.entities.*;
import com.tsystems.javaschool.db.repository.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by alex on 15.10.16.
 */
@Component
public class FillData {

    private HashMap<String, List<String>> emails;
    private Set<String> passNumbers;
    private Set<String> numbers;

    private final TariffRepository tariffRepository;
    private final CustomerRepository customerRepository;
    private final ContractRepository contractRepository;
    private final OptionRepository optionRepository;
    private final UserRepository userRepository;

    private int COUNT_OF_CUSTOMERS = 100;

    @Autowired
    public FillData(TariffRepository tariffRepository, CustomerRepository customerRepository, ContractRepository contractRepository, OptionRepository optionRepository, UserRepository userRepository) {
        this.tariffRepository = tariffRepository;
        this.customerRepository = customerRepository;
        this.contractRepository = contractRepository;
        this.optionRepository = optionRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void fill() {
        constractUqies();

        if (tariffRepository.findAll().size() != 0 ||
                userRepository.findAll().size() != 0 ||
                contractRepository.findAll().size() != 0 ||
                optionRepository.findAll().size() != 0)
            return;

        BCryptPasswordEncoder coder = new BCryptPasswordEncoder();

        Staff admin = new Staff();
        admin.setName("Admin");
        admin.setSurname("Admin");
        admin.setEmail("admin@ts.ru");
        admin.setPassword(coder.encode("adminadmin"));
        admin.setTmpPassword("");
        userRepository.saveAndFlush(admin);


        Tariff simple = addTariff("Simple", 0d, "Simple tariff without additional options");
        Tariff smsUser = addTariff("Sms user", 10d, "Additional options for sms!");
        Tariff mmsActive = addTariff("Mms active", 10d, "Additional options for sms!");
        Tariff internet = addTariff("Internet monster", 10d, "Stay online!");
        Tariff business = addTariff("Business", 200d, "Only unlimited options!");

        Option smsBasic = addOption("Sms basic", 0d, 1d, "Just use sms", new HashSet<>(Arrays.asList(smsUser, mmsActive)), null, null);
        Option free10 = addOption("Sms 10 pack", 5d, 0d, "Pack with 10 sms", Collections.singleton(smsUser), Collections.singleton(smsBasic), null);
        Option free20 = addOption("Sms 20 pack", 10d, 0d, "Pack with 20 sms", Collections.singleton(smsUser), Collections.singleton(smsBasic), Collections.singleton(free10));
        Option free50 = addOption("Sms 50 pack", 30d, 0d, "Pack with 50 sms", Collections.singleton(smsUser), Collections.singleton(smsBasic), new HashSet<>(Arrays.asList(free10, free20)));
        Option smsUnlim = addOption("Sms unlimited", 100d, 200d, "Unlimited sms!", new HashSet<>(Arrays.asList(business, smsUser)), null, new HashSet<>(Arrays.asList(free10, free20, free50)));

        Option mmsBasic = addOption("Mms basic", 0d, 5d, "Just use mms", new HashSet<>(Arrays.asList(smsUser, mmsActive)), null, null);
        Option free5mms = addOption("Mms 5 pack", 10d, 0d, "Pack with 5 mms", Collections.singleton(mmsActive), Collections.singleton(mmsBasic), null);
        Option free10mms = addOption("Mms 10 pack", 20d, 0d, "Pack with 10 mms", Collections.singleton(mmsActive), Collections.singleton(mmsBasic), Collections.singleton(free5mms));
        Option free30mms = addOption("Mms 30 pack", 30d, 0d, "Pack with 30 mms", Collections.singleton(mmsActive), Collections.singleton(mmsBasic), new HashSet<>(Arrays.asList(free5mms, free10mms)));
        Option mmsUnlim = addOption("Mms unlimited", 30d, 0d, "Unlimited mms!", new HashSet<>(Arrays.asList(business, mmsActive)), null, new HashSet<>(Arrays.asList(free5mms, free10mms, free30mms)));

        Option internetBasic = addOption("Internet basic", 0d, 10d, "Just use internet", Collections.singleton(internet), null, null);
        Option internet1gb = addOption("Internet 1Gb/m", 30d, 0d, "1Gb of internet for one month!", Collections.singleton(internet), Collections.singleton(internetBasic), null);
        Option internet2gb = addOption("Internet 2Gb/m", 50d, 0d, "2Gb of internet for one month!", Collections.singleton(internet), Collections.singleton(internetBasic), Collections.singleton(internet1gb));
        Option internet5gb = addOption("Internet 5Gb/m", 100d, 0d, "5 of internet for one month!", Collections.singleton(internet), Collections.singleton(internetBasic), new HashSet<>(Arrays.asList(internet1gb, internet2gb)));
        Option internetUnlim = addOption("Internet unlimited", 500d, 0d, "Unlimited internet!", new HashSet<>(Arrays.asList(business, internet)), null, new HashSet<>(Arrays.asList(internet1gb, internet2gb, internet5gb)));

        Map<Tariff, List<Set<Option>>> combinations = new HashMap<>();
        combinations.put(simple, Collections.singletonList(new HashSet<>()));
        combinations.put(smsUser, Arrays.asList(Collections.singleton(smsBasic),
                new HashSet<Option>(Arrays.asList(smsBasic, free10)),
                new HashSet<Option>(Arrays.asList(smsBasic, free20)),
                new HashSet<Option>(Arrays.asList(smsBasic, free50)),
                Collections.singleton(smsUnlim)
                ));
        combinations.put(mmsActive, Arrays.asList(Collections.singleton(mmsBasic),
                new HashSet<Option>(Arrays.asList(mmsBasic, free5mms)),
                new HashSet<Option>(Arrays.asList(mmsBasic, free10mms)),
                new HashSet<Option>(Arrays.asList(mmsBasic, free30mms)),
                Collections.singleton(mmsUnlim)
                ));
        combinations.put(internet, Arrays.asList(Collections.singleton(internetBasic),
                new HashSet<Option>(Arrays.asList(internetBasic, internet1gb)),
                new HashSet<Option>(Arrays.asList(internetBasic, internet2gb)),
                new HashSet<Option>(Arrays.asList(internetBasic, internet5gb)),
                Collections.singleton(internetUnlim)
                ));
        combinations.put(business, Arrays.asList(Collections.singleton(smsUnlim),
                Collections.singleton(mmsUnlim),
                Collections.singleton(internetUnlim)
                ));

        Iterator<String> iter = passNumbers.iterator();
        Iterator<String> numberIter = numbers.iterator();

        Customer alex = new Customer();
        alex.setName("Alex");
        alex.setSurname("Plate");
        alex.setPassportNumber("DJWANLDI8");
        alex.setEmail("alexpl292@gmail.com");
        alex.setPassword(coder.encode("adminadmin"));
        alex.setTmpPassword("");
        alex.setAddress("Die Stadt: " + getRandomCity()+"\n"+getStub(5));
        alex.setPassportData(getStub(3));
        alex.setIsBlocked(0);
        alex.setDateOfBirth(getDate());
        alex = customerRepository.saveAndFlush(alex);

        List<Contract> contracts = new ArrayList<>();
        contracts.addAll(getContracts(combinations, numberIter, alex));

        Customer test = new Customer();
        test.setName("Testname");
        test.setSurname("Testsurname");
        test.setPassportNumber("ZZZZZZZZZ");
        test.setEmail("test@test.ru");
        test.setPassword(coder.encode("adminadmin"));
        test.setTmpPassword("");
        test.setAddress("TEST CUSTOMER. DO NOT CHANGE.");
        test.setPassportData("TEST CUSTOMER. DO NOT CHANGE.");
        test.setIsBlocked(0);
        test.setDateOfBirth(getDate());
        test = customerRepository.saveAndFlush(test);

        Contract contractTest1 = new Contract();
        contractTest1.setBalance(BigDecimal.valueOf(100));
        contractTest1.setIsBlocked(2);
        contractTest1.setNumber("+7 (000) 000-0000");
        contractTest1.setTariff(simple);
        contractTest1.setCustomer(test);
        contractRepository.saveAndFlush(contractTest1);

        Contract contractTest2 = new Contract();
        contractTest2.setBalance(BigDecimal.valueOf(100));
        contractTest2.setIsBlocked(0);
        contractTest2.setNumber("+7 (111) 111-1111");
        contractTest2.setTariff(smsUser);
        contractTest2.setUsedOptions(new HashSet<>(Arrays.asList(smsBasic, free10)));
        contractTest2.setCustomer(test);
        contractRepository.saveAndFlush(contractTest2);

        Contract contractTest3 = new Contract();
        contractTest3.setBalance(BigDecimal.valueOf(100));
        contractTest3.setIsBlocked(1);
        contractTest3.setNumber("+7 (222) 222-2222");
        contractTest3.setTariff(internet);
        contractTest3.setUsedOptions(new HashSet<>(Arrays.asList(internetBasic, internet1gb)));
        contractTest3.setCustomer(test);
        contractRepository.saveAndFlush(contractTest3);

        for (Map.Entry<String, List<String>> user : emails.entrySet()) {
            if (iter.hasNext()) {
                Customer c = new Customer();
                c.setName(user.getValue().get(0));
                c.setSurname(user.getValue().get(1));
                c.setEmail(user.getKey());
                c.setPassportNumber(iter.next());
                c.setPassword(coder.encode("adminadmin"));
                c.setTmpPassword("");
                c.setAddress("Die Stadt: "+getRandomCity() + "\n" + getStub(5));
                c.setDateOfBirth(getDate());
                c.setIsBlocked(0);
                c.setPassportData(getStub(3));

                c = customerRepository.saveAndFlush(c);
                contracts.addAll(getContracts(combinations, numberIter, c));
            }
        }

        contractRepository.save(contracts);
    }

    private Tariff addTariff(String name, Double cost, String description) {
        Tariff tariff = new Tariff();
        tariff.setName(name);
        tariff.setCost(BigDecimal.valueOf(cost));
        tariff.setDescription(description);
        return tariffRepository.saveAndFlush(tariff);
    }

    private Option addOption(String name,
                             Double connectCost,
                             Double cost,
                             String desc,
                             Set<Tariff> forTariffs,
                             Set<Option> req,
                             Set<Option> forb) {
        Option option = new Option();
        option.setName(name);
        option.setConnectCost(BigDecimal.valueOf(connectCost));
        option.setCost(BigDecimal.valueOf(cost));
        option.setDescription(desc);
        option.setPossibleTariffsOfOption(forTariffs);

        if (req != null)
            option.addRequiredFromOptions(req);
        if (forb != null)
            option.addForbiddenWithOptions(forb);

        return optionRepository.saveAndFlush(option);
    }

    private void constractUqies() {
        emails = new HashMap<>();
        passNumbers = new HashSet<>();
        numbers = new HashSet<>();

        while (emails.size() < COUNT_OF_CUSTOMERS) {
            Random rnd = new Random();
            String name = names.get(rnd.nextInt(names.size()));
            String surname = surnames.get(rnd.nextInt(surnames.size()));
            emails.put(name+"@"+surname+".de", Arrays.asList(name, surname));
        }
         while (passNumbers.size() < COUNT_OF_CUSTOMERS) {
             passNumbers.add(RandomStringUtils.random(10, true, true).toUpperCase());
         }
         while (numbers.size() < COUNT_OF_CUSTOMERS*3) {
             String number = RandomStringUtils.random(10, false, true);
             if (Arrays.asList("0000000000", "1111111111", "2222222222").contains(number))
                 continue;
             number = "+7 ("+number.substring(0,3)+") "+number.substring(3, 6)+"-"+number.substring(6);
             numbers.add(number);
         }
    }

    private String getRandomCity() {
        return cities.get(new Random().nextInt(cities.size()));
    }

    private Date getDate() {
        long beginTime = Timestamp.valueOf("1940-01-01 00:00:00").getTime();
        long endTime = Timestamp.valueOf("1995-12-31 00:00:00").getTime();
        long diff = endTime - beginTime + 1;
        return new Date(beginTime + (long) (Math.random() * diff));
    }

    private String getStub(int j) {
        Random rnd = new Random();
        StringBuilder ips = new StringBuilder();
        for (int i = 1; i < j*4; i++) {
            ips.append(ipsum[rnd.nextInt(ipsum.length)]+" ");
            if (i%4 == 0)
                ips.append("\n");
        }
        String s = ips.toString();
        return s.substring(0, Math.min(s.length(), 254));
    }

    private Set<Contract> getContracts(Map<Tariff, List<Set<Option>>> combinations, Iterator<String> numberIter, Customer customer) {
        List<Tariff> tariffs = new ArrayList<>(combinations.keySet());
        Random rnd = new Random();
        Set<Contract> contracts = new HashSet<>();
        for (int i = 0; i < 1+rnd.nextInt(3); i++) {
            if (numberIter.hasNext()) {
                Integer tariff = rnd.nextInt(tariffs.size());
                Contract contract = new Contract();
                int balance = rnd.nextInt(1000) - 100;
                contract.setBalance(BigDecimal.valueOf(balance));
                contract.setIsBlocked(getBlocked());
                contract.setNumber(numberIter.next());
                contract.setTariff(tariffs.get(tariff));
                List<Set<Option>> options = combinations.get(tariffs.get(tariff));
                contract.setUsedOptions(options.get(rnd.nextInt(options.size())));
                contract.setCustomer(customer);
                contracts.add(contract);
            }
        }
        return contracts;
    }

    private Integer getBlocked() {
        Random rnd = new Random();
        int x = rnd.nextInt(100);
        if (x >= 30)
            return 0;
        else if (x < 30 && x > 15 )
            return 1;
        else
            return 2;
    }

    List<String> names = Arrays.asList(
            "Lea", "Julia", "Laura", "Anna", "Lisa", "Lena", "Sarah", "Katharina", "Johanna", "Sophie", "Marie",
            "Leonie", "Vanessa", "Alina", "Lara", "Jana", "Hannah", "Jessica", "Annika", "Luisa", "Michelle", "Melanie",
            "Jasmin", "Sabrina", "Linda", "Sandra", "Anja", "Christina", "Nina", "Nadine", "Maria", "Anne", "Carina",
            "Pia", "Nicole", "Céline", "Eva", "Sophia", "Jenny", "Jennifer", "Steffi", "Janine", "Franzi", "Franziska",
            "Carolin", "Marina", "Elena", "Antonia", "Kim", "Elisa", "Alex", "Lukas", "Michael", "Daniel", "Philipp",
            "Jonas", "Fabian", "Marcel", "Tim", "Kevin", "Jan", "David", "Tom", "Markus", "Sebastian", "Julian",
            "Leon", "Christoph", "Simon", "Felix", "Andreas", "Nils", "Nico", "Martin", "Max", "Florian", "Dennis",
            "Patrick", "Thomas", "Christopher", "Moritz", "Nick", "Chris", "Paul", "Jonathan", "Tobias", "Jakob", "Christian",
            "Adrian", "Matthias", "Dominik", "Stefan", "René", "Ali", "Marco", "Vincent", "Mohamed", "Kai", "Erik",
            "Ludwig");
    List<String> surnames = Arrays.asList(
            "Schmidt", "Schneider", "Fischer", "Weber", "Meyer", "Wagner", "Becker", "Bauer", "Hoffmann", "Schulz",
            "Koch", "Richter", "Klein", "Wolf", "Neuman", "Braun", "Werner", "Schwarz", "Hofmann", "Zimmermann",
            "Schmitt", "Hartmann", "Schmid", "Schmitz", "Lange", "Meier", "Walter", "Maier", "Beck", "Krause",
            "Schulze", "Huber", "Mayer", "Frank", "Lehmann", "Kaiser", "Fuchs", "Herrmann", "Lang", "Thomas",
            "Peters", "Stein", "Jung", "Berger", "Martin", "Friedrich", "Scholz", "Keller", "Groß", "Hahn",
            "Roth", "Vogel", "Schuster", "Lorenz", "Ludwig", "Baumann", "Heinrich", "Otto", "Simon", "Graf",
            "Kraus", "Schulte", "Albrecht", "Franke", "Winter", "Schumacher", "Vogt", "Haas", "Sommer", "Schreiber",
            "Engel", "Ziegler", "Dietrich", "Brandt", "Seidel", "Kuhn", "Busch", "Horn", "Arnold", "Bergmann",
            "Pohl", "Pfeiffer", "Wolff", "Voigt", "Sauer");
    List<String> cities = Arrays.asList(
            "Berlin", "Hamburg", "Munich", "Cologne", "Frankfurt", "Stuttgart", "Dusseldorf", "Dortmund", "Essen",
            "Leipzig", "Bremen", "Dresden", "Hanover", "Nuremberg", "Duisburg", "Bochum", "Wuppertal", "Bielefeld",
            "Bonn", "Munster", "Karlsruhe", "Mannheim", "Augsburg", "Wiesbaden", "Gelsenkirchen", "Monchengladbach", "Braunschweig",
            "Chemnitz", "Kiel", "Aachen", "Halle", "Magdeburg", "Freiburg", "Krefeld", "Lubeck", "Oberhausen",
            "Erfurt", "Mainz", "Rostock", "Kassel", "Hagen", "Hamm", "Saarbrucken", "Mulheim an der Ruhr", "Potsdam",
            "Ludwigshafen am Rhein", "Oldenburg", "Leverkusen", "Osnabruck", "Solingen" );

    String[] ipsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec non sapien eget libero tristique ullamcorper. Proin commodo dolor in scelerisque egestas. Vivamus fermentum dui erat, at dapibus lacus sodales at. Maecenas aliquet vel nisl a gravida. Praesent volutpat dapibus purus, et ullamcorper nibh cursus quis. Morbi ullamcorper nec felis quis vehicula. In finibus arcu leo, nec tempor mi elementum id. Nunc suscipit pulvinar velit, at commodo mi commodo nec. Fusce pulvinar, ipsum non cursus luctus, metus nisl porta arcu, at molestie quam lectus et metus. Nulla volutpat, odio dignissim malesuada faucibus, orci tortor laoreet felis, ac malesuada urna nulla non felis. Donec tincidunt tellus a est luctus, commodo suscipit tellus scelerisque. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed ullamcorper metus vitae dolor tempor, eget tempus nisi iaculis. Quisque viverra eget ante ultricies faucibus. Morbi mattis, nisi at auctor bibendum, dolor justo lacinia mi, ornare sagittis turpis erat et arcu.".split(" ");
}
