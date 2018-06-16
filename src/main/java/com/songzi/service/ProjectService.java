package com.songzi.service;

import com.songzi.config.Constants;
import com.songzi.domain.Authority;
import com.songzi.domain.Examine;
import com.songzi.domain.Project;
import com.songzi.domain.User;
import com.songzi.domain.enumeration.DeleteFlag;
import com.songzi.repository.AuthorityRepository;
import com.songzi.repository.ExamineRepository;
import com.songzi.repository.ProjectRepository;
import com.songzi.repository.UserRepository;
import com.songzi.security.AuthoritiesConstants;
import com.songzi.security.SecurityUtils;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.service.util.RandomUtil;
import com.songzi.web.rest.vm.ProjectVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class ProjectService {

    private final Logger log = LoggerFactory.getLogger(ProjectService.class);

    private ProjectRepository projectRepository;
    private ExamineRepository examineRepository;
    private UserService userService;

    public ProjectService(ProjectRepository projectRepository, ExamineRepository examineRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.examineRepository = examineRepository;
        this.userService = userService;
    }

    public Page<ProjectDTO> findAllWithExamine(Pageable pageable) {
        return projectRepository.findAllByDelFlagWithExamine(DeleteFlag.NORMAL, pageable);
    }
}
