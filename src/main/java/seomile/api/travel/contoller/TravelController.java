package seomile.api.travel.contoller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import seomile.api.travel.dto.TravelListDTO;
import seomile.api.travel.service.TravelService;

import java.util.List;

@RestController
@RequestMapping("/travel")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @GetMapping("/list")
    public List<TravelListDTO> getTravelList(@RequestParam(required = false) List<Integer> categories) {
        return travelService.fetchTravelInfo(categories);
    }
}