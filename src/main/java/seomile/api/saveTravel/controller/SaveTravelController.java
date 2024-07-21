package seomile.api.saveTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seomile.api.member.repository.MemberRepository;
import seomile.api.member.entity.Member;
import seomile.api.saveTravel.dto.SaveTravelDTO;
import seomile.api.saveTravel.entity.SaveTravel;
import seomile.api.saveTravel.service.SaveTravelService;
import seomile.api.travel.dto.TravelDTO;

import java.util.List;

import java.util.Optional;

@RestController
@RequestMapping("/bookmark")
public class SaveTravelController {
    @Autowired
    private SaveTravelService saveTravelService;
    @Autowired
    private MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(SaveTravelService.class);
    @PostMapping
    public ResponseEntity<SaveTravelDTO> saveTravel(@RequestBody SaveTravelDTO saveTravelDTO) {
        SaveTravelDTO savedTravelDTO = saveTravelService.saveTravel(saveTravelDTO);
        return ResponseEntity.ok(savedTravelDTO);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<SaveTravelDTO>> getTravelsByUserId(@PathVariable("userId") Long userId) {
        List<SaveTravelDTO> travelDTOs = saveTravelService.getTravelsByUserId(userId);
        return ResponseEntity.ok(travelDTOs);
    }
    @GetMapping("/{userId}/travel/{travCode}")
    public ResponseEntity<TravelDTO> getTravelDetail(
            @PathVariable("userId") Long userId,
            @PathVariable("travCode") String travCode) {

        try {
            TravelDTO travelDetail = saveTravelService.getDetailInfoByUserId(userId, travCode);
            return ResponseEntity.ok(travelDetail); // 200 OK 응답 반환
        } catch (RuntimeException e) {
            logger.error("Error fetching travel detail for userId: {} and travCode: {}", userId, travCode, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found 반환
        }
    }

}
