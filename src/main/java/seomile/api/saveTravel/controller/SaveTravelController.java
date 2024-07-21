package seomile.api.saveTravel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import seomile.api.member.repository.MemberRepository;
import seomile.api.member.entity.Member;
import seomile.api.saveTravel.dto.SaveTravelDTO;
import seomile.api.saveTravel.entity.SaveTravel;
import seomile.api.saveTravel.service.SaveTravelService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookmark")
public class SaveTravelController {
    @Autowired
    private SaveTravelService saveTravelService;
    @Autowired
    private MemberRepository memberRepository;

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
}
