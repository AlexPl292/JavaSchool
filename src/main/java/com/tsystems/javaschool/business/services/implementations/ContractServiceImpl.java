package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.db.interfaces.OptionDao;
import com.tsystems.javaschool.db.interfaces.TariffDao;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityGraph;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 24.08.16.
 */
@Service
public class ContractServiceImpl implements ContractService{

    private final ContractDao contractDao;
    private final TariffDao tariffDao;
    private final OptionDao optionDao;
    private static final Logger logger = Logger.getLogger(ContractServiceImpl.class);

    @Autowired
    public ContractServiceImpl(OptionDao optionDao, ContractDao contractDao, TariffDao tariffDao) {
        this.optionDao = optionDao;
        this.contractDao = contractDao;
        this.tariffDao = tariffDao;
    }

    @Override
    public void addNew(Contract contract) {
        try {
            EMU.beginTransaction();
            contractDao.create(contract);
            EMU.commit();
            logger.info("New contract is created. Id = "+contract.getId());
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract loadByKey(Integer key) {
        Contract contract = contractDao.read(key);
        EMU.closeEntityManager();
        return contract;
    }

    @Override
    public EntityGraph getEntityGraph() {
        return contractDao.getEntityGraph();
    }

    @Override
    public void remove(Integer key) {
        try {
            EMU.beginTransaction();
            contractDao.delete(key);
            EMU.commit();
            logger.info("Contract is removed. Id = "+key);
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract addNew(Contract contract, List<Integer> optionsIds) {
        try {
            EMU.beginTransaction();
            contract.setUsedOptions(optionDao.loadOptionsByIds(optionsIds));
            contractDao.create(contract);
            EMU.commit();
            logger.info("New contract is created. Id = "+contract.getId());
            return contract;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract loadByKey(Integer key, Map<String, Object> hints) {
        Contract contract = contractDao.read(key, hints);
        EMU.closeEntityManager();
        return contract;
    }

    @Override
    public void setBlock(Integer id, Integer blockLevel) {
        try {
            EMU.beginTransaction();
            Contract contract = contractDao.read(id);
            Integer oldBlock = contract.getIsBlocked();
            contract.setIsBlocked(blockLevel);
            EMU.commit();
            logger.info("Set block level for id = "+contract.getId()+". Old block level = "+oldBlock+". New level = "+blockLevel);
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public Contract updateContract(Integer contractId, Integer tariffId, List<Integer> optionIds) {
        EntityGraph<Contract> graph = getEntityGraph();
        graph.addAttributeNodes("usedOptions");

        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        try {
            EMU.beginTransaction();
            Set<Option> options = optionDao.loadOptionsByIds(optionIds);
            Contract contract = contractDao.read(contractId, hints);
            String oldContractData = contract.toString();

            BigDecimal cost = options.stream().filter(e -> !contract.getUsedOptions().contains(e)).map(Option::getConnectCost).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal res = contract.getBalance().subtract(cost);
            contract.setBalance(res);

            contract.setTariff(tariffDao.read(tariffId));
            contract.setUsedOptions(options);
            EMU.commit();
            logger.info("Contract updated. Old contract: "+oldContractData+". New contract: "+contract.toString());
            return contract;
        } catch (RuntimeException re) {
            if (EMU.getEntityManager() != null && EMU.getEntityManager().isOpen())
                EMU.rollback();
            throw re;
        } finally {
            EMU.closeEntityManager();
        }
    }

    @Override
    public List<Contract> load(Map<String, Object> kwargs) {
        List<Contract> contracts = contractDao.read(kwargs);
        EMU.closeEntityManager();
        return contracts;
    }

    @Override
    public long count(Map<String, Object> kwargs) {
        long count = contractDao.count(kwargs);
        EMU.closeEntityManager();
        return count;
    }

    @Override
    public List<Contract> loadAll() {
        return load(new HashMap<>());
    }
}
