package com.ishvatov.service.inner.waypoint;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.cargo.CargoDao;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.waypoint.WayPointDao;
import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.buisness.CargoEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Basic {@link WayPointService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("waypointService")
@Transactional
public class WayPointServiceImpl extends AbstractService<Integer, WayPointEntity, WayPointDto>
    implements WayPointService {

    /**
     * Autowired DAO field.
     */
    private CityDao cityDao;

    /**
     * Autowired DAO field.
     */
    private OrderDao orderDao;

    /**
     * Autowired DAO field.
     */
    private CargoDao cargoDao;

    /**
     * Autowired DAO field.
     */
    private WayPointDao wayPointDao;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper   {@link Mapper} implementation.
     * @param cargoDao autowired {@link CargoDao} impl.
     * @param cityDao  autowired {@link CityDao} impl.
     * @param orderDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public WayPointServiceImpl(CityDao cityDao, WayPointDao wayPointDao,
                               CargoDao cargoDao, OrderDao orderDao,
                               Mapper<WayPointEntity, WayPointDto> mapper) {
        super(wayPointDao, mapper);
        this.cityDao = cityDao;
        this.orderDao = orderDao;
        this.cargoDao = cargoDao;
        this.wayPointDao = wayPointDao;
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
    public void save(WayPointDto dtoObj) {
        validateRequiredFields(dtoObj);

        CityEntity cityEntity = Optional.ofNullable(cityDao.findByUniqueKey(dtoObj.getCityId()))
            .orElseThrow(() -> new DAOException(getClass(), "save", "Entity with such UID does not exist"));
        CargoEntity cargoEntity = Optional.ofNullable(cargoDao.findByUniqueKey(dtoObj.getCargoId()))
            .orElseThrow(() -> new DAOException(getClass(), "save", "Entity with such UID does not exist"));
        OrderEntity orderEntity = Optional.ofNullable(orderDao.findByUniqueKey(dtoObj.getOrderId()))
            .orElseThrow(() -> new DAOException(getClass(), "save", "Entity with such UID does not exist"));

        if (exists(orderEntity.getUniqueIdentificator(), cargoEntity.getId(),
            cityEntity.getUniqueIdentificator(), dtoObj.getAction())) {
            throw new DAOException(getClass(), "save", "Entity with such unique parameters already exists");
        } else {
            WayPointEntity wayPointEntity = new WayPointEntity();

            // save entity
            wayPointEntity.setAction(dtoObj.getAction());
            wayPointEntity.setStatus(dtoObj.getStatus());
            wayPointDao.save(wayPointEntity);

            // add it to the system
            cityEntity.addWayPoint(wayPointEntity);
            cargoEntity.addWayPoint(wayPointEntity);
            orderEntity.addWayPoint(wayPointEntity);
        }
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
    public void update(WayPointDto dtoObj) {
        WayPointEntity wayPointEntity = Optional.ofNullable(wayPointDao.findById(dtoObj.getId()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such unique parameters already exists"));
        wayPointEntity.setStatus(dtoObj.getStatus());
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(Integer key) {
        Optional<WayPointEntity> wayPointEntity = Optional.ofNullable(
            wayPointDao.findById(Optional.ofNullable(key)
                .orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null")))
        );

        wayPointEntity.ifPresent(entity -> {
            Optional.ofNullable(entity.getCargo()).ifPresent(e -> e.removeWayPoint(entity));
            Optional.ofNullable(entity.getCity()).ifPresent(e -> e.removeWayPoint(entity));
            Optional.ofNullable(entity.getOrder()).ifPresent(e -> e.removeWayPoint(entity));
            wayPointDao.delete(entity);
        });
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(WayPointDto dto) {
        if (dto == null || dto.getAction() == null
            || dto.getOrderId() == null
            || dto.getCityId() == null
            || dto.getCargoId() == null
            || dto.getStatus() == null) {
            throw new ValidationException();
        }
    }

    /**
     * Checks if such waypoint exists.
     *
     * @param orderUID   UID of the order.
     * @param cargoID    UID of the cargo.
     * @param cityUID    UID of the city.
     * @param actionType Action type.
     * @return true or false.
     */
    private boolean exists(String orderUID, Integer cargoID, String cityUID, CargoActionType actionType) {
        List<WayPointDto> allWaypoints = findAll();
        for (WayPointDto wayPointDto : allWaypoints) {
            if (cityUID.equals(wayPointDto.getCityId())
                && cargoID.equals(wayPointDto.getCargoId())
                && orderUID.equals(wayPointDto.getOrderId())
                && actionType.equals(wayPointDto.getAction())) {
                return true;
            }
        }
        return false;
    }
}
