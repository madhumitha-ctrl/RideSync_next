package com.ridesync.backend.controller;

import com.ridesync.backend.dto.RecommendationResponse;
import com.ridesync.backend.service.RecommendationService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rides")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/recommend")
    public List<RecommendationResponse> recommend(@RequestParam Long userId) {
        return recommendationService.recommend(userId);
    }
}
