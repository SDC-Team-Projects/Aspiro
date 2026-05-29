package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.response.AnalyticsResponse;

public interface AnalyticsService {

    AnalyticsResponse getUserAnalytics(String userEmail);
}