package com.ishvatov.model.dao.order;

import com.ishvatov.model.dao.AbstractDao;
import com.ishvatov.model.entity.buisness.CargoEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.buisness.WayPointEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;
import com.ishvatov.utils.Pair;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * {@link OrderDao} implementation.
 *
 * @author Sergey Khvatov
 */
@Repository("orderDao")
public class OrderDaoImpl extends AbstractDao<String, OrderEntity> implements OrderDao {

    /**
     * Finds all the cargo entities that are associated
     * with order with this UID.
     *
     * @param UID UID of the order.
     * @return list with all the cargo in this order.
     */
    @Override
    public Map<CityEntity, List<Pair<CargoEntity, CargoActionType>>> findAllCargo(String uid) {
        OrderEntity orderEntity = findByUniqueKey(uid);
        Set<WayPointEntity> wayPointEntities = orderEntity.getAssignedWaypoints();

        Map<CityEntity, List<Pair<CargoEntity, CargoActionType>>> resultMap = new HashMap<>();
        for (WayPointEntity wayPointEntity : wayPointEntities) {
            Pair<CargoEntity, CargoActionType> pair = new Pair<>(
                wayPointEntity.getWaypointCargoEntity(),
                wayPointEntity.getCargoAction()
            );

            if (!resultMap.containsKey(wayPointEntity.getWaypointCityEntity())) {
                resultMap.put(wayPointEntity.getWaypointCityEntity(), new ArrayList<>());
            }
            resultMap.get(wayPointEntity.getWaypointCityEntity()).add(pair);
        }

        return resultMap;
    }
}
