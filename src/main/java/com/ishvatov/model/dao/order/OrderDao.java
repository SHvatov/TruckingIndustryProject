package com.ishvatov.model.dao.order;

import com.ishvatov.model.dao.BaseDaoInterface;
import com.ishvatov.model.entity.buisness.CargoEntity;
import com.ishvatov.model.entity.buisness.CityEntity;
import com.ishvatov.model.entity.buisness.OrderEntity;
import com.ishvatov.model.entity.enum_types.CargoActionType;
import com.ishvatov.utils.Pair;

import java.util.List;
import java.util.Map;

/**
 * Defines basic interface to work with truck entities in the database.
 *
 * @author Sergey Khvatov.
 */
public interface OrderDao extends BaseDaoInterface<String, OrderEntity> {

    /**
     * Finds all the cargo entities that are associated
     * with order with this UID.
     *
     * @param UID UID of the order.
     * @return list with all the cargo in this order.
     */
    Map<CityEntity, List<Pair<CargoEntity, CargoActionType>>> findAllCargo(String uid);
}
