package com.ishvatov.service.inner.cargo;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.cargo.CargoDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dto.CargoDto;
import com.ishvatov.model.entity.buisness.CargoEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.service.inner.AbstractService;
import com.ishvatov.service.inner.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
     * @throws DAOException        if entity with this UID already exists
     * @throws ValidationException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void save(CargoDto dtoObj) {
        validateRequiredFields(dtoObj, true);
        CargoEntity entity = new CargoEntity();
        updateImpl(dtoObj, entity);
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
     * @throws DAOException        if entity with this UID already exists
     * @throws ValidationException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void update(CargoDto dtoObj) {
        validateRequiredFields(dtoObj, false);
        CargoEntity cargoEntity = Optional
            .of(cargoDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));
        updateImpl(dtoObj, cargoEntity);
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     * @throws ValidationException if key is null.
     */
    @Override
    public void delete(Integer key) {
        Optional<CargoEntity> cargoEntity = Optional.ofNullable(
            cargoDao.findByUniqueKey(
                Optional.ofNullable(key).orElseThrow(
                    () -> new ValidationException(getClass(), "find", "Key is null")
                )
            )
        );

        cargoEntity.ifPresent(entity -> {
            Set<WayPointEntity> set = entity.getAssignedWaypoints()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
            set.forEach(entity::removeWayPoint);
            cargoDao.delete(entity);
        });
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(CargoDto dto, CargoEntity entity) {
        if (!dto.getCargoStatus().equals(entity.getCargoStatus())) {
            entity.setCargoStatus(dto.getCargoStatus());
        }

        if (!dto.getCargoMass().equals(entity.getCargoMass())) {
            entity.setCargoMass(dto.getCargoMass());
        }

        if (!dto.getCargoName().equals(entity.getCargoName())) {
            entity.setCargoName(dto.getCargoName());
        }
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto    DTO object.
     * @param isSave defines, whether this method is called from
     *               save method or not.
     */
    private void validateRequiredFields(CargoDto dto, boolean isSave) {
        if (dto == null || dto.getCargoName() == null
            || dto.getCargoStatus() == null || dto.getCargoMass() == null) {
            throw new ValidationException();
        }

        if (!isSave && dto.getId() == null) {
            throw new ValidationException();
        }
    }
}
