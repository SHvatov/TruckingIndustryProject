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
}
