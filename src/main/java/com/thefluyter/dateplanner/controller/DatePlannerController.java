package com.thefluyter.dateplanner.controller;

import com.thefluyter.dateplanner.model.Friend;
import com.thefluyter.dateplanner.service.DatePlannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
@Slf4j
public class DatePlannerController {

    @Value("${date-planner.friend.json.path}")
    private String jsonPath;

    private final DatePlannerService datePlannerService;

    public DatePlannerController(DatePlannerService datePlannerService) {
        this.datePlannerService = datePlannerService;
    }

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/plan-date")
    public String planDateDetails(Model model) {
        Friend friend = datePlannerService.planDate(jsonPath);
        log.info("Friend to plan date with: {}", friend.getName());
        model.addAttribute("friend", friend);
        return "plan-date";
    }
}
