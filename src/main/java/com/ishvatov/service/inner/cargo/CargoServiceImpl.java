package com.ishvatov.service.inner.cargo;

import com.ishvatov.exception.DAOException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.cargo.CargoDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.buisness.CargoEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.service.inner.AbstractService;
import com.ishvatov.service.inner.order.OrderService;
import com.ishvatov.service.inner.waypoint.WayPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

/**
 * Basic {@link OrderService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("cargoService")
@Transactional
public class CargoServiceImpl extends AbstractService<Integer, CargoEntity, CargoDto> implements CargoService {

    /**
     * Autowired DAO field.
     */
    private CargoDao cargoDao;

    /**
     * Autowired service.
     */
    private WayPointService wayPointService;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper   {@link Mapper} implementation.
     * @param cargoDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public CargoServiceImpl(CargoDao cargoDao, Mapper<CargoEntity, CargoDto> mapper) {
        super(cargoDao, mapper);
        this.cargoDao = cargoDao;
    }

    /**
     * Adds entity to the DB. Check if entity already exists.
     *
     * @param dtoObj new entity to add.
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void save(CargoDto dtoObj) {
        validateRequiredFields(dtoObj);
        CargoEntity entity = new CargoEntity();
        entity.setCargoMass(dtoObj.getCargoMass());
        entity.setCargoName(dtoObj.getCargoName());
        entity.setCargoStatus(dtoObj.getCargoStatus());
        cargoDao.save(entity);
    }

    /**
     * Updates data in the database. If fields in teh DTO
     * are not null, then update them. If are null, then
     * if corresponding filed in the Entity is nullable,
     * then set it to null and remove all connections,
     * otherwise throw NPE.
     *
     * @param dtoObj values to update in the entity.
     * @throws DAOException         if entity with this UID already exists
     * @throws NullPointerException if DTO field, which is corresponding to
     *                              the not nullable field in the Entity object is null.
     */
    @Override
    public void update(CargoDto dtoObj) {
        validateRequiredFields(dtoObj);
        CargoEntity cargoEntity = cargoDao.findByUniqueKey(dtoObj.getUniqueIdentificator());
        if (cargoEntity == null) {
            throw new DAOException(getClass(), "update", "Entity with such UID does not exist");
        } else {
            updateImpl(dtoObj, cargoEntity);
        }
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(Integer key) {
        if (Objects.isNull(key)) {
            throw new NullPointerException();
        }

        // todo should i check if null
        CargoEntity cargoEntity = cargoDao.findByUniqueKey(key);
        if (cargoEntity != null) {
            Set<WayPointEntity> wayPointEntitySet = cargoEntity.getAssignedWaypoints();
            if (wayPointEntitySet != null && !wayPointEntitySet.isEmpty()) {
                for (WayPointEntity wayPointEntity : wayPointEntitySet) {
                    if (wayPointEntity != null) {
                        wayPointService.delete(wayPointEntity.getId());
                    }
                }
            }
            cargoDao.delete(cargoEntity);
        }
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(CargoDto dto, CargoEntity entity) {
        entity.setId(dto.getId());
        entity.setCargoStatus(dto.getCargoStatus());
        entity.setCargoMass(dto.getCargoMass());
        entity.setCargoName(dto.getCargoName());
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(CargoDto dto) {
        if (dto == null || dto.getCargoName() == null || dto.getCargoStatus() == null
            || dto.getCargoMass() == null || dto.getId() == null) {
            throw new NullPointerException();
        }
    }
}
