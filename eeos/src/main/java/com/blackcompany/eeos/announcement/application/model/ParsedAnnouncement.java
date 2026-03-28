package com.blackcompany.eeos.announcement.application.model;

import java.time.LocalDate;

public record ParsedAnnouncement(String title, String body, LocalDate deadline) {}
