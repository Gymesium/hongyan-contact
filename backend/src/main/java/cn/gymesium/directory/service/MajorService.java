package cn.gymesium.directory.service;

import cn.gymesium.directory.common.ApiException;
import cn.gymesium.directory.common.SanitizeUtil;
import cn.gymesium.directory.dto.MajorDto;
import cn.gymesium.directory.dto.MajorRequest;
import cn.gymesium.directory.entity.Major;
import cn.gymesium.directory.repository.MajorRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MajorService {

    private final MajorRepository majors;

    public MajorService(MajorRepository majors) {
        this.majors = majors;
    }

    @Transactional(readOnly = true)
    public List<MajorDto> listEnabled() {
        return majors.findByEnabledTrueOrderByNameAsc().stream().map(MajorDto::from).toList();
    }

    @Transactional(readOnly = true)
    public List<MajorDto> listAll() {
        return majors.findAllByOrderByNameAsc().stream().map(MajorDto::from).toList();
    }

    @Transactional
    public MajorDto create(MajorRequest req) {
        if (majors.existsByCode(req.code())) {
            throw ApiException.badRequest("专业代码已存在");
        }
        Major major = new Major();
        major.setCode(SanitizeUtil.clean(req.code()));
        major.setName(SanitizeUtil.clean(req.name()));
        major.setEnabled(req.enabled() == null || req.enabled());
        return MajorDto.from(majors.save(major));
    }

    @Transactional
    public MajorDto update(Long id, MajorRequest req) {
        Major major = majors.findById(id).orElseThrow(() -> ApiException.notFound("专业不存在"));
        major.setCode(SanitizeUtil.clean(req.code()));
        major.setName(SanitizeUtil.clean(req.name()));
        if (req.enabled() != null) {
            major.setEnabled(req.enabled());
        }
        return MajorDto.from(major);
    }

    @Transactional
    public void disable(Long id) {
        Major major = majors.findById(id).orElseThrow(() -> ApiException.notFound("专业不存在"));
        major.setEnabled(false);
    }
}
