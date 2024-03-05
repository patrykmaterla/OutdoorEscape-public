package com.example.outdoorescape

import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.time.Duration
import java.util.Date

data class ItemBrowseActivitiesViewModel(val icon: Int, val title: String, val distance: Float, val duration: Duration, val steps: Int, val date: LocalDateTime)