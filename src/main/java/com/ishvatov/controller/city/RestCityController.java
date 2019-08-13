package com.ishvatov.controller.city;

import com.ishvatov.service.inner.city.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Basic rest controller.
 *
 * @author Sergey Khvatov
 */
@Controller
@RequestMapping("employee/city")
public class RestCityController {
    /**
     * Autowired service to access DAO layer.
     */
    @Autowired
    private CityService cityService;

    /**
     * Gets all the trucks from the database.
     *
     * @return list of trucks from database.
     */
    @GetMapping(value = "/list")
    public @ResponseBody List<String> listAllTrucks() {
        return cityService.getAllCityNames();
    }

}
