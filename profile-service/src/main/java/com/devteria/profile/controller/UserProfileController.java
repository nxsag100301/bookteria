package com.devteria.profile.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devteria.profile.dto.ApiResponse;
import com.devteria.profile.dto.request.ProfileCreationRequest;
import com.devteria.profile.dto.response.UserProfileResponse;
import com.devteria.profile.service.UserProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @PostMapping
    public ApiResponse<UserProfileResponse> createUser(@RequestBody ProfileCreationRequest request) {
        UserProfileResponse res = userProfileService.createUser(request);

        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .result(res)
                .build();
    }

    @GetMapping("/{profileId}")
    public ApiResponse<UserProfileResponse> getProfileById(@PathVariable String profileId) {
        UserProfileResponse res = userProfileService.getProfileById(profileId);
        return ApiResponse.<UserProfileResponse>builder()
                .message("Success")
                .result(res)
                .build();
    }
}
