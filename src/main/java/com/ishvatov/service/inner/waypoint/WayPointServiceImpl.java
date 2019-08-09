package com.ishvatov.service.inner.waypoint;

import com.ishvatov.exception.CustomProjectException;
import com.ishvatov.exception.DAOException;
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
import com.ishvatov.service.inner.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Basic {@link WayPointService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("waypointService")
@Transactional
public class WayPointServiceImpl extends AbstractService<Integer, WayPointEntity, WayPointDto> implements WayPointService {

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
                               CargoDao cargoDao, OrderDao orderDao, Mapper<WayPointEntity, WayPointDto> mapper) {
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
     * @throws NullPointerException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void save(WayPointDto dtoObj) {
        validateRequiredFields(dtoObj);
        CityEntity cityEntity = cityDao.findByUniqueKey(dtoObj.getWaypointCityUID());
        if (cityEntity == null) {
            throw new DAOException(getClass(), "save", "Entity with such UID does not exist");
        }

        CargoEntity cargoEntity = cargoDao.findByUniqueKey(dtoObj.getWaypointCargoUID());
        if (cargoEntity == null) {
            throw new DAOException(getClass(), "save", "Entity with such UID does not exist");
        }

        OrderEntity orderEntity = orderDao.findByUniqueKey(dtoObj.getWaypointOrderUID());
        if (orderEntity == null) {
            throw new DAOException(getClass(), "save", "Entity with such UID does not exist");
        }

        if (wayPointDao.exists(cityEntity.getId(), orderEntity.getId(),
            cargoEntity.getId(), dtoObj.getCargoAction())) {
            throw new DAOException(getClass(), "save", "Entity with such unique parameters already exists");
        } else {
            WayPointEntity wayPointEntity = new WayPointEntity();

            wayPointEntity.setId(cargoEntity.hashCode() + orderEntity.hashCode() + cityEntity.hashCode());
            wayPointEntity.setCargoAction(dtoObj.getCargoAction());

            cityEntity.addWayPoint(wayPointEntity);
            cargoEntity.addWayPoint(wayPointEntity);
            orderEntity.addWayPoint(wayPointEntity);

            wayPointDao.save(wayPointEntity);
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
     * @throws NullPointerException if DTO field, which is corresponding to
     *                             the not nullable field in the Entity object is null.
     */
    @Override
    public void update(WayPointDto dtoObj) {
        throw new CustomProjectException(getClass(), "update", "Cargo does not support update");
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(Integer key) {
        WayPointEntity wayPointEntity = wayPointDao.findById(key);
        if (wayPointEntity != null) {
            CityEntity cityEntity = wayPointEntity.getWaypointCityEntity();
            if (cityEntity != null) {
                cityEntity.removeWayPoint(wayPointEntity);
            }

            CargoEntity cargoEntity = wayPointEntity.getWaypointCargoEntity();
            if (cargoEntity != null) {
                cargoEntity.removeWayPoint(wayPointEntity);
            }

            OrderEntity orderEntity = wayPointEntity.getWaypointOrderEntity();
            if (orderEntity != null) {
                orderEntity.removeWayPoint(wayPointEntity);
            }

            wayPointDao.delete(wayPointEntity);
        }
    }
    
    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(WayPointDto dto) {
        if (dto == null || dto.getCargoAction() == null || dto.getWaypointOrderUID() == null
            || dto.getWaypointCityUID() == null
            || dto.getWaypointCargoUID() == null){
            throw new NullPointerException();
        }
    }
}
