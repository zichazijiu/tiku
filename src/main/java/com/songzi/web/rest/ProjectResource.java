package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Project;
import com.songzi.domain.enumeration.Status;
import com.songzi.repository.ProjectRepository;
import com.songzi.service.ProjectService;
import com.songzi.service.dto.ProjectDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.ProjectQueryVM;
import com.songzi.web.rest.vm.ProjectVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing Project.
 */
@RestController
@RequestMapping("/api")
public class ProjectResource {

    private final Logger log = LoggerFactory.getLogger(ProjectResource.class);

    private static final String ENTITY_NAME = "project";

    private final ProjectRepository projectRepository;

    private final ProjectService projectService;

    public ProjectResource(ProjectRepository projectRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    /**
     * POST  /projects : Create a new project.
     *
     * @param projectVM the project to create
     * @return the ResponseEntity with status 201 (Created) and with body the new project, or with status 400 (Bad Request) if the project has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/projects")
    @Timed
    @ApiOperation(value = "新建项目")
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectVM projectVM) throws URISyntaxException {
        log.debug("REST request to save Project : {}", projectVM);
        if (projectVM.getId() != null) {
            throw new BadRequestAlertException("新建项目ID必须为空", ENTITY_NAME, "ID必须为空");
        }
        Project result = projectService.insert(projectVM);
        return ResponseEntity.created(new URI("/api/projects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /projects : Updates an existing project.
     *
     * @param projectVM the project to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated project,
     * or with status 400 (Bad Request) if the project is not valid,
     * or with status 500 (Internal Server Error) if the project couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/projects")
    @Timed
    @ApiOperation(value = "更新项目")
    public ResponseEntity<Project> updateProject(@Valid @RequestBody ProjectVM projectVM) throws URISyntaxException {
        log.debug("REST request to update Project : {}", projectVM);
        if (projectVM.getId() == null) {
            throw new BadRequestAlertException("更新项目ID不能为空", ENTITY_NAME, "ID不能为空");
        }
        Project result = projectService.update(projectVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects")
    @Timed
    @ApiOperation(value = "查询所有项目")
    public ResponseEntity<List<ProjectDTO>> getAllProjects(ProjectQueryVM projectQueryVM,Pageable pageable) {
        log.debug("REST request to get a page of Projects {}{}",projectQueryVM,pageable);
        Page<ProjectDTO> page = projectService.findAll(projectQueryVM,pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /projects : get all the projects.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of projects in body
     */
    @GetMapping("/projects/current")
    @ApiOperation(value = "已测试：获取项目列表")
    @Timed
    public ResponseEntity<List<ProjectDTO>> getProjects4CurrentUser(Pageable pageable) {
        log.debug("REST request to get a page of Projects for current user");
        Page<ProjectDTO> page = projectService.findAllWithExamine(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/projects/current");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


    /**
     * GET  /projects/:id : get the "id" project.
     *
     * @param id the id of the project to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the project, or with status 404 (Not Found)
     */
    @GetMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        log.debug("REST request to get Project : {}", id);
        Project project = projectRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
    }

    /**
     * DELETE  /projects/:id : delete the "id" project.
     *
     * @param id the id of the project to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/projects/{id}")
    @Timed
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        log.debug("REST request to delete Project : {}", id);
        projectRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/projects/{projectId}/subjects")
    @Timed
    @ApiOperation(value = "给项目添加考核项")
    public ResponseEntity addSubject(@PathVariable(value = "projectId") Long projectId,@RequestParam(value = "subjectIdList") List<Long> subjectIdList){
        log.debug("给项目添加考核项 {}{}",projectId,subjectIdList);
        projectService.addSubject(projectId,subjectIdList);
        Map map = new HashMap();
        map.put("successFlag","1");
        map.put("message","all success");
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(map));
    }


    @PostMapping("/projects/publish/{projectId}")
    @Timed
    @ApiOperation(value = "发布项目")
    public ResponseEntity publishProject(@PathVariable(value = "projectId") Long projectId){
        Project project = projectService.publish(projectId);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
    }

    @PostMapping("/projects/audited/{projectId}/{status}")
    @Timed
    @ApiOperation(value = "评审项目")
    public ResponseEntity audited(@PathVariable(value = "projectId") Long projectId,@PathVariable(value = "status") Status status){
        Project project = projectService.audited(projectId,status);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(project));
    }
}
