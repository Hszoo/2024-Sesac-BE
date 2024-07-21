package seomile.api.saveTravel.service;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seomile.api.member.entity.Member;
import seomile.api.saveTravel.dto.SaveTravelDTO;
import seomile.api.member.repository.MemberRepository;
import seomile.api.saveTravel.repository.SaveTravelRepository;
import seomile.api.travel.entity.Travel;
import seomile.api.saveTravel.entity.SaveTravel;

import seomile.api.travel.service.TravelService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor

public class SaveTravelService {
    private final SaveTravelRepository saveTravelRepository;
    private final TravelService travelService;
    private final MemberRepository memberRepository;

    //여행지 저장
    public SaveTravelDTO saveTravel(SaveTravelDTO saveTravelDTO) {
        Member member = memberRepository.findById(saveTravelDTO.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        Travel travel = travelService.findOrCreateTravel(saveTravelDTO.getTravCode());

        SaveTravel saveTravel = new SaveTravel();
        saveTravel.setUser(member);
        saveTravel.setTravel(travel);

        SaveTravel savedBookmark = saveTravelRepository.save(saveTravel);
        return new SaveTravelDTO(savedBookmark.getBookmarkId(), saveTravelDTO.getUserId(), saveTravelDTO.getTravCode());
    }
    //사용자 저장 여행지 조회

    public List<SaveTravelDTO> getTravelsByUserId(Long userId) {
        Optional<Member> optionalMember = memberRepository.findById(userId);

        //사용자 존재 여부에 따라 여행 정보 조회
        return optionalMember
                .map(member -> {
                    List<SaveTravel> savedTravels = saveTravelRepository.findByUserId(member.getId());
                    return savedTravels.stream()
                            .map(saveTravel -> new SaveTravelDTO(
                                    saveTravel.getBookmarkId(),
                                    saveTravel.getUser().getId(),
                                    saveTravel.getTravel().getTravCode()
                            ))
                            .collect(Collectors.toList());
                })
                .orElse(Collections.emptyList());
    }

}
