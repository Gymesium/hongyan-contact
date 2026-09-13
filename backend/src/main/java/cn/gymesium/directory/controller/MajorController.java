package cn.gymesium.directory.controller;

import cn.gymesium.directory.dto.MajorDto;
import cn.gymesium.directory.service.MajorService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/majors")
public class MajorController {

    private final MajorService majorService;

    public MajorController(MajorService majorService) {
        this.majorService = majorService;
    }

    @GetMapping
    public List<MajorDto> list() {
        return majorService.listEnabled();
    }
}
