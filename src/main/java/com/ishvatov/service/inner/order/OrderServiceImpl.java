package com.ishvatov.service.inner.order;

import com.ishvatov.exception.DAOException;
import com.ishvatov.exception.ValidationException;
import com.ishvatov.mapper.Mapper;
import com.ishvatov.model.dao.cargo.CargoDao;
import com.ishvatov.model.dao.city.CityDao;
import com.ishvatov.model.dao.city_map.CityMapDao;
import com.ishvatov.model.dao.driver.DriverDao;
import com.ishvatov.model.dao.order.OrderDao;
import com.ishvatov.model.dao.truck.TruckDao;
import com.ishvatov.model.dao.waypoint.WayPointDao;
import com.ishvatov.model.dto.DriverDto;
import com.ishvatov.model.dto.OrderDto;
import com.ishvatov.model.dto.TruckDto;
import com.ishvatov.model.dto.WayPointDto;
import com.ishvatov.model.entity.AbstractEntity;
import com.ishvatov.model.entity.buisness.*;
import com.ishvatov.model.entity.enum_types.TruckStatusType;
import com.ishvatov.service.inner.AbstractService;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Basic {@link OrderService} interface implementation.
 *
 * @author Sergey Khvatov
 */
@Service("orderService")
@Transactional
public class OrderServiceImpl extends AbstractService<String, OrderEntity, OrderDto> implements OrderService {

    /**
     * Autowired DAO field.
     */
    private TruckDao truckDao;

    /**
     * Autowired DAO field.
     */
    @Autowired
    private CargoDao cargoDao;


    /**
     * Autowired DAO field.
     */
    @Autowired
    private CityMapDao cityMapDao;

    /**
     * Autowired DAO field.
     */
    @Autowired
    private CityDao cityDao;

    /**
     * Autowired DAO field.
     */
    @Autowired
    private Mapper<TruckEntity, TruckDto> truckDtoMapper;

    /**
     * Autowired DAO field.
     */
    @Autowired
    private Mapper<DriverEntity, DriverDto> driverDtoMapper;

    /**
     * Autowired DAO field.
     */
    private WayPointDao wayPointDao;

    /**
     * Autowired DAO field.
     */
    private OrderDao orderDao;

    /**
     * Autowired DAO field.
     */
    private DriverDao driverDao;

    /**
     * Default class constructor, that is used
     * to inject DAO interface implementation and
     * initialize the super class.
     *
     * @param mapper   {@link Mapper} implementation.
     * @param truckDao autowired {@link TruckDao} impl.
     * @param wayPointDao  autowired {@link WayPointDao} impl.
     * @param orderDao autowired {@link OrderDao} impl.
     */
    @Autowired
    public OrderServiceImpl(TruckDao truckDao, WayPointDao wayPointDao,
                            DriverDao driverDao, OrderDao orderDao,
                            Mapper<OrderEntity, OrderDto> mapper) {
        super(orderDao, mapper);
        this.wayPointDao = wayPointDao;
        this.orderDao = orderDao;
        this.truckDao = truckDao;
        this.driverDao = driverDao;
    }

    /**
     * Finds all suitable for this order trucks.
     *
     * @param wayPointDtoList list of the waypoints.
     * @return list of suitable for this order trucks.
     */
    @Override
    public List<TruckDto> findSuitableTrucks(List<WayPointDto> wayPointDtoList) {
        double maxMass = 0;
        double currentMass = 0;
        for (WayPointDto wayPointDto : wayPointDtoList) {
            CargoEntity cargoDto = Optional
                .ofNullable(cargoDao.findByUniqueKey(wayPointDto.getCargoId()))
                .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));

            switch (wayPointDto.getAction()) {
                case LOADING:
                    currentMass += cargoDto.getMass();
                    break;
                case UNLOADING:
                    currentMass -= cargoDto.getMass();
                    break;
            }

            if (Double.compare(currentMass, maxMass) > 0) {
                maxMass = currentMass;
            }
        }

        final double finalMass = maxMass;
        return truckDao.findAll()
            .stream()
            .filter(Objects::nonNull)
            .filter(entity -> (Double.compare(entity.getCapacity(), finalMass) >= 0)
                    && (entity.getStatus() == TruckStatusType.IN_ORDER)
                    && (entity.getOrder() == null))
            .map(truckDtoMapper::map)
            .collect(Collectors.toList());
    }

    /**
     * Finds all suitable for this order drivers.
     *
     * @param truckId        UID of the truck.
     * @param wayPointDtoList list of the waypoints.
     * @return list of suitable for this order trucks.
     */
    @Override
    public List<DriverDto> findSuitableDrivers(String truckId, List<WayPointDto> wayPointDtoList) {
        // build graph
        Graph<Integer, DefaultEdge> map = cityMapDao.buildMap();
        DijkstraShortestPath<Integer, DefaultEdge> dijkstraShortestPath = new DijkstraShortestPath<>(map);

        // get the truck
        TruckEntity truckEntity = Optional.ofNullable(truckDao.findByUniqueKey(truckId))
            .orElseThrow(() -> new DAOException(getClass(), "findSuitableDrivers", "Entity with such UID does not exist"));

        // get shift size in seconds
        int shiftSize = truckEntity.getShiftSize() * 3600;

        // find the city of the truck
        CityEntity currentCity = truckEntity.getCity();

        // find time that will be spent on this trip
        int totalTime = 0;
        for (WayPointDto wayPointDto : wayPointDtoList) {
            CityEntity nextCity = Optional.ofNullable(cityDao.findByUniqueKey(wayPointDto.getCityId()))
                .orElseThrow(() -> new DAOException(getClass(), "findSuitableDrivers", "Entity with such UID does not exist"));

            // find shortest path
            List<Integer> citiesToVisit = dijkstraShortestPath
                .getPath(currentCity.getId(), nextCity.getId())
                .getVertexList();

            // find total time
            if (!citiesToVisit.isEmpty()) {
                Integer start = citiesToVisit.get(0);
                for (int i = 1; i < citiesToVisit.size(); i++) {
                    // get info about path
                    CityMapEntity cityMapEntity =
                        Optional.ofNullable(cityMapDao.findBetween(start, citiesToVisit.get(i)))
                        .orElseThrow(() -> new DAOException(getClass(), "updateOrder", "Entity with such UID does not exist"));

                    double distance = cityMapEntity.getDistance();
                    double speed = cityMapEntity.getAverageSpeed();
                    totalTime += (int) (distance / speed * 3600);
                    start = citiesToVisit.get(i);
                }
            }
            currentCity = nextCity;
        }

        // find all drivers in the same city and with no order
        List<DriverEntity> driverList = driverDao.findAll()
            .stream()
            .filter(entity -> entity.getCity().equals(truckEntity.getCity())
                && entity.getOrder() == null)
            .collect(Collectors.toList());

        // result list
        List<DriverDto> result = new ArrayList<>();

        // 176 hours in seconds
        final int MAX_SHIFT = 176 * 3600;

        // find suitable drivers
        for (DriverEntity driver : driverList) {
            // if it is obvious
            if (driver.getWorkedHours() + totalTime <= MAX_SHIFT) {
                result.add(driverDtoMapper.map(driver));
            } else {
                // else calculate estimate time
                // and check if drivers worked hours will be set to 0
                // due to the fact of new month
                int workedHours = driver.getWorkedHours();  // current worked hours
                int orderHours = 0;                         // number of hours spent in the trip
                boolean suits = true;                       // flag
                // current month
                int month = Calendar.getInstance().get(Calendar.MONTH);
                while (orderHours < totalTime) {
                    orderHours += shiftSize;
                    workedHours += shiftSize;
                    Calendar.getInstance().add(Calendar.DATE, 1);
                    if (Calendar.getInstance().get(Calendar.MONTH) != month) {
                        workedHours = 0;
                        month = Calendar.getInstance().get(Calendar.MONTH);
                    } else if (workedHours > MAX_SHIFT) {
                        suits = false;
                    }
                }

                if (suits) {
                    result.add(driverDtoMapper.map(driver));
                }
            }
        }
        return result;
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
    public void save(OrderDto dtoObj) {
        validateRequiredFields(dtoObj);
        if (exists(dtoObj.getUniqueIdentificator())) {
            throw new DAOException(getClass(), "save", "Entity with such UID already exists");
        } else {
            OrderEntity entity = new OrderEntity();
            updateImpl(dtoObj, entity);
            orderDao.save(entity);
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
    public void update(OrderDto dtoObj) {
        validateRequiredFields(dtoObj);
        OrderEntity entity = Optional.ofNullable(
            orderDao.findByUniqueKey(dtoObj.getUniqueIdentificator()))
            .orElseThrow(() -> new DAOException(getClass(), "update", "Entity with such UID does not exist"));
        updateImpl(dtoObj, entity);
    }

    /**
     * Deletes entity from the DB if it exists.
     *
     * @param key UID of the entity.
     */
    @Override
    public void delete(String key) {
        Optional<OrderEntity> orderEntity = Optional.ofNullable(
            orderDao.findByUniqueKey(Optional.ofNullable(key)
                    .orElseThrow(() -> new ValidationException(getClass(), "find", "Key is null")))
        );

        orderEntity.ifPresent(entity -> {
            clearAssignedDrivers(entity);
            removeTruck(entity);
            clearAssignedWaypoints(entity);
            orderDao.delete(entity);
        });
    }

    /**
     * Update method implementation.
     *
     * @param dto    DTO object.
     * @param entity Entity object.
     */
    private void updateImpl(OrderDto dto, OrderEntity entity) {
        if (!dto.getUniqueIdentificator().equals(entity.getUniqueIdentificator())) {
            entity.setUniqueIdentificator(dto.getUniqueIdentificator());
        }

        if (!dto.getLastUpdated().equals(entity.getLastUpdated())) {
            entity.setLastUpdated(dto.getLastUpdated());
        }

        if (!dto.getStatus().equals(entity.getStatus())) {
            entity.setStatus(dto.getStatus());
        }

        if (dto.getTruckId() != null) {
            updateTruck(dto.getTruckId(), entity);
        } else {
            removeTruck(entity);
        }

        Optional.ofNullable(dto.getAssignedDrivers()).ifPresent(driversSet -> {
            if (!driversSet.isEmpty()) {
                updateAssignedDrivers(driversSet
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()), entity);
            } else {
                clearAssignedDrivers(entity);
            }
        });

        Optional.ofNullable(dto.getAssignedWaypoints()).ifPresent(waypointsSet -> {
            if (!waypointsSet.isEmpty()) {
                updateAssignedWaypoints(waypointsSet
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()), entity);
            } else {
                clearAssignedWaypoints(entity);
            }
        });
    }

    /**
     * Updates the drivers set of the entity.
     *
     * @param assignedDrivers set of UIDs of the drivers.
     * @param entity        Entity object.
     */
    private void updateAssignedDrivers(Set<String> assignedDrivers, OrderEntity entity) {
        Set<String> currentDriversUIDSet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .map(AbstractEntity::getUniqueIdentificator)
            .collect(Collectors.toSet());

        if (!currentDriversUIDSet.equals(assignedDrivers)) {
            // clear the driver set
            clearAssignedDrivers(entity);
            // for each uid in the set
            assignedDrivers.forEach(uid -> {
                // try get the driver entity from DB
                DriverEntity driverEntity = Optional.ofNullable(driverDao.findByUniqueKey(uid))
                    .orElseThrow(
                        () -> new DAOException(getClass(), "updateAssignedDrivers", "Entity with such UID does not exist")
                    );
                // if driver has already been assigned to a truck
                // then remove him
                Optional.ofNullable(driverEntity.getOrder())
                    .ifPresent(e -> e.removeDriver(driverEntity));
                // add driver to this truck
                entity.addDriver(driverEntity);
            });
        }
    }

    /**
     * Clears the drivers set of the entity.
     *
     * @param entity Entity object.
     */
    private void clearAssignedDrivers(OrderEntity entity) {
        Set<DriverEntity> driverEntitySet = entity.getAssignedDrivers()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        driverEntitySet.forEach(entity::removeDriver);
    }

    /**
     * Updates the trucks set of the entity.
     *
     * @param truckUID    UID of the truck.
     * @param entity        Entity object.
     */
    private void updateTruck(String truckUID, OrderEntity entity) {
        String currentTruckUID = Optional
            .ofNullable(entity.getAssignedTruck())
            .map(AbstractEntity::getUniqueIdentificator)
            .orElse("");
        if (!currentTruckUID.equals(truckUID)) {
            TruckEntity truckEntity = Optional
                .ofNullable(truckDao.findByUniqueKey(truckUID))
                .orElseThrow(() -> new DAOException(getClass(), "updateTruck", "Entity with such UID does not exist"));

            Optional.ofNullable(entity.getAssignedTruck()).ifPresent(e -> {
                e.setOrder(null);
                entity.setAssignedTruck(null);
            });

            entity.setAssignedTruck(truckEntity);
            truckEntity.setOrder(entity);
        }
    }

    /**
     * Clears the trucks set of the entity.
     *
     * @param entity Entity object.
     */
    private void removeTruck(OrderEntity entity) {
        Optional.ofNullable(entity.getAssignedTruck()).ifPresent(e -> {
            e.setOrder(null);
            entity.setAssignedTruck(null);
        });
    }

    /**
     * Updates the waypoints set of the entity.
     *
     * @param assignedWaypoints set of UIDs of the drivers.
     * @param entity        Entity object.
     */
    private void updateAssignedWaypoints(Set<Integer> assignedWaypoints, OrderEntity entity) {
        Set<Integer> currentWaypointsIDSet = entity.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .map(WayPointEntity::getId)
            .collect(Collectors.toSet());

        if (!currentWaypointsIDSet.equals(assignedWaypoints)) {
            // clear the driver set
            clearAssignedWaypoints(entity);
            // for each uid in the set
            assignedWaypoints.forEach(uid -> {
                // try get the driver entity from DB
                WayPointEntity wayPointEntity = Optional.ofNullable(wayPointDao.findById(uid))
                    .orElseThrow(
                        () -> new DAOException(getClass(), "updateAssignedWaypoints", "Entity with such UID does not exist")
                    );
                // if driver has already been assigned to a truck
                // then remove him
                Optional.ofNullable(wayPointEntity.getOrder())
                    .ifPresent(e -> e.removeWayPoint(wayPointEntity));
                // add driver to this truck
                entity.addWayPoint(wayPointEntity);
            });
        }
    }

    /**
     * Clears the waypoints set of the entity.
     *
     * @param entity Entity object.
     */
    private void clearAssignedWaypoints(OrderEntity entity) {
        Set<WayPointEntity> waypointEntitySet = entity.getAssignedWaypoints()
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
        waypointEntitySet.forEach(entity::removeWayPoint);
    }

    /**
     * Validates the input DTO object and throws NPE,
     * if object or one of required fields is null.
     *
     * @param dto DTO object.
     */
    private void validateRequiredFields(OrderDto dto) {
        if (dto == null || dto.getUniqueIdentificator() == null || dto.getStatus() == null
            || dto.getLastUpdated() == null) {
            throw new ValidationException();
        }
    }
}
