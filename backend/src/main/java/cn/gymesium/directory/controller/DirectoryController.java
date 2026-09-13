package cn.gymesium.directory.controller;

import cn.gymesium.directory.dto.PageResponse;
import cn.gymesium.directory.dto.ProfileResponse;
import cn.gymesium.directory.service.DirectoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/directory")
public class DirectoryController {

    private final DirectoryService directoryService;

    public DirectoryController(DirectoryService directoryService) {
        this.directoryService = directoryService;
    }

    @GetMapping
    public PageResponse<ProfileResponse> search(
            @RequestParam(required = false) Long majorId,
            @RequestParam(required = false) Integer enrollYear,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return directoryService.search(majorId, enrollYear, keyword, page, size);
    }
}
