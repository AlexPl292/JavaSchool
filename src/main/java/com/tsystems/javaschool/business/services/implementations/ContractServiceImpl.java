package com.tsystems.javaschool.business.services.implementations;

import com.tsystems.javaschool.business.services.interfaces.ContractService;
import com.tsystems.javaschool.db.entities.Contract;
import com.tsystems.javaschool.db.entities.Option;
import com.tsystems.javaschool.db.implemetations.ContractDaoImpl;
import com.tsystems.javaschool.db.implemetations.OptionDaoImpl;
import com.tsystems.javaschool.db.implemetations.TariffDaoImpl;
import com.tsystems.javaschool.db.interfaces.ContractDao;
import com.tsystems.javaschool.util.EMU;
import org.apache.log4j.Logger;

import javax.persistence.EntityGraph;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by alex on 24.08.16.
 */
public class ContractServiceImpl implements ContractService{

    private ContractDao contractDao = ContractDaoImpl.getInstance();
    private final static Logger logger = Logger.getLogger(ContractServiceImpl.class);


    private ContractServiceImpl() {}

    private static class ContractServiceHolder {
        private static final ContractServiceImpl instance = new ContractServiceImpl();
        private ContractServiceHolder() {}
    }

    public static ContractServiceImpl getInstance() {
        return ContractServiceHolder.instance;
    }

    @Override
    public void addNew(Contract contract) {
        try {
            EMU.beginTransaction();
            contractDao.create(contract);
            EMU.commit();
            logger.info("New contract created. Id = "+contract.getId());
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
            logger.info("Contract removed. Id = "+key);
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
            contract.setUsedOptions(OptionDaoImpl.getInstance().loadOptionsByIds(optionsIds));
            contractDao.create(contract);
            EMU.commit();
            logger.info("New contract created. Id = "+contract.getId());
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
    public Contract updateContract(Integer contract_id, Integer tariff_id, List<Integer> optionIds) {
        EntityGraph<Contract> graph = ContractServiceImpl.getInstance().getEntityGraph();
        graph.addAttributeNodes("usedOptions");

        Map<String, Object> hints = new HashMap<>();
        hints.put("javax.persistence.loadgraph", graph);
        try {
            EMU.beginTransaction();
            Set<Option> options = OptionDaoImpl.getInstance().loadOptionsByIds(optionIds);
            Contract contract = contractDao.read(contract_id, hints);
            String oldContractData = contract.toString();

            BigDecimal cost = options.stream().filter(e -> !contract.getUsedOptions().contains(e)).map(Option::getConnectCost).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal res = contract.getBalance().subtract(cost);
            contract.setBalance(res);

            contract.setTariff(TariffDaoImpl.getInstance().read(tariff_id));
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
