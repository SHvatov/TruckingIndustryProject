package com.ishvatov.service.truck;

import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.entity.TruckEntity;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service("truckService")
@Transactional
public class TruckServiceImpl implements TruckService {

    /**
     * DAO used in this service
     * to access the DB.
     */
    @Autowired
    private TruckDao truckDao;

    /**
     * Mapper used in this service
     * to convert objects from DAO to DTO.
     */
    @Autowired
    private DozerBeanMapper mapper;

    /**
     * Finds entity by it's id in the DB.
     *
     * @param id id of the entity in DB.
     * @return object with this unique id.
     */
    public TruckDto findById(int id) {
        TruckEntity entity = truckDao.findById(id);
        return entity == null ? null : mapper.map(entity, TruckDto.class);
    }

    /**
     * Adds entity to the DB.
     *
     * @param entity new entity to add.
     */
    public void save(@NotNull TruckDto entity) {
        truckDao.save(mapper.map(entity, TruckEntity.class));
    }

    /**
     * Deletes entity from the DB.
     *
     * @param entity entity to delete.
     */
    public void delete(@NotNull TruckDto entity) {
        truckDao.delete(mapper.map(entity, TruckEntity.class));
    }

    /**
     * Updates entity from the DB.
     *
     * @param entity entity to delete.
     */
    public void update(@NotNull TruckDto entity) {
        TruckEntity truck = truckDao.findByUniqueKey(entity.getTruckRegistrationNumber());
        if (truck != null) {
            truck.setTruckCapacity(entity.getTruckCapacity());
            truck.setTruckCurrentCity(entity.getTruckCurrentCity());
            truck.setDriverShiftSize(entity.getDriverShiftSize());
            truck.setTruckRegistrationNumber(entity.getTruckRegistrationNumber());
            truck.setTruckCondition(entity.getTruckCondition());
        }
    }

    /**
     * Checks if the input key is unique or not.
     *
     * @param key key to check.
     * @return true, if this key is unique in the DB, false otherwise.
     */
    public boolean isUniqueKey(String key) {
        return findByUniqueKey(key) == null;
    }

    /**
     * Finds entity by it's unique id.
     *
     * @param key unique key of the id.
     * @return Unique entity with this id or null.
     */
    public TruckDto findByUniqueKey(String key) {
        TruckEntity entity = truckDao.findByUniqueKey(key);
        return entity == null ? null : mapper.map(entity, TruckDto.class);
    }

    /**
     * Deletes entity by it's unique id.
     *
     * @param key unique key of the entity.
     */
    public void deleteByUniqueKey(String key) {
        TruckEntity entity = truckDao.findByUniqueKey(key);
        if (entity != null) {
            truckDao.delete(entity);
        }
    }

    /**
     * Finds all the entities in the DB.
     *
     * @return list with all the entities.
     */
    public List<TruckDto> findAll() {
        List<TruckDto> dtoList = new ArrayList<TruckDto>();
        for (TruckEntity entity: truckDao.findAll()) {
            dtoList.add(mapper.map(entity, TruckDto.class));
        }
        return dtoList;
    }

    /**
     * Finds the entities with this shift size.
     *
     * @param shiftSize driver's shift size.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    public List<TruckDto> findByDriverShiftSize(int shiftSize) {
        List<TruckDto> dtoList = new ArrayList<TruckDto>();
        for (TruckEntity entity: truckDao.findByDriverShiftSize(shiftSize)) {
            dtoList.add(mapper.map(entity, TruckDto.class));
        }
        return dtoList;
    }

    /**
     * Finds the entities with capacity in range(min, max).
     *
     * @param min minimal capacity.
     * @param max maximal capacity.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    public List<TruckDto> findByCapacity(double min, double max) {
        List<TruckDto> dtoList = new ArrayList<TruckDto>();
        for (TruckEntity entity: truckDao.findByCapacity(min, max)) {
            dtoList.add(mapper.map(entity, TruckDto.class));
        }
        return dtoList;
    }

    /**
     * Finds the entities with this condition.
     *
     * @param status condition of the truck
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    public List<TruckDto> findByCondition(byte status) {
        List<TruckDto> dtoList = new ArrayList<TruckDto>();
        for (TruckEntity entity: truckDao.findByCondition(status)) {
            dtoList.add(mapper.map(entity, TruckDto.class));
        }
        return dtoList;
    }

    /**
     * Finds the entities with this city.
     *
     * @param city truck's location.
     * @return empty collection, if no instances were found,
     * List of {@link TruckDto} objects otherwise.
     */
    public List<TruckDto> findByCity(String city) {
        List<TruckDto> dtoList = new ArrayList<TruckDto>();
        for (TruckEntity entity: truckDao.findByCity(city)) {
            dtoList.add(mapper.map(entity, TruckDto.class));
        }
        return dtoList;
    }
}
