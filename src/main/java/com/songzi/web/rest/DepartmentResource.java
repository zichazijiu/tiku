package com.songzi.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.songzi.domain.Department;
import com.songzi.repository.DepartmentRepository;
import com.songzi.service.DepartmentSerivce;
import com.songzi.service.dto.DepartmentDTO;
import com.songzi.service.dto.UserDTO;
import com.songzi.web.rest.errors.BadRequestAlertException;
import com.songzi.web.rest.util.HeaderUtil;
import com.songzi.web.rest.util.PaginationUtil;
import com.songzi.web.rest.vm.DepartmentQueryVM;
import com.songzi.web.rest.vm.DepartmentUserVM;
import com.songzi.web.rest.vm.DepartmentVM;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Department.
 */
@RestController
@RequestMapping("/api")
public class DepartmentResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentResource.class);

    private static final String ENTITY_NAME = "department";

    private final DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentSerivce departmentSerivce;

    public DepartmentResource(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    /**
     * POST  /departments : Create a new department.
     *
     * @param departmentVM the department to create
     * @return the ResponseEntity with status 201 (Created) and with body the new department, or with status 400 (Bad Request) if the department has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/departments")
    @Timed
    @ApiOperation(value = "新建机构")
    public ResponseEntity<DepartmentDTO> createDepartment(@Valid @RequestBody DepartmentVM departmentVM) throws URISyntaxException {
        log.debug("REST request to save Department : {}", departmentVM);
        if (departmentVM.getId() != null) {
            throw new BadRequestAlertException("新建机构ID必须为空", ENTITY_NAME, "ID必须为空");
        }
        DepartmentDTO result = departmentSerivce.insert(departmentVM);
        return ResponseEntity.created(new URI("/api/departments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /departments : Updates an existing department.
     *
     * @param departmentVM the department to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated department,
     * or with status 400 (Bad Request) if the department is not valid,
     * or with status 500 (Internal Server Error) if the department couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/departments")
    @Timed
    @ApiOperation(value = "更新机构")
    public ResponseEntity<DepartmentDTO> updateDepartment(@Valid @RequestBody DepartmentVM departmentVM) throws URISyntaxException {
        log.debug("REST request to update Department : {}", departmentVM);
        if (departmentVM.getId() == null) {
            throw new BadRequestAlertException("更新机构ID不能为空", ENTITY_NAME, "ID不能为空");
        }
        DepartmentDTO result = departmentSerivce.update(departmentVM);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * GET  /departments : get all the departments.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of departments in body
     */
    @GetMapping("/departments")
    @Timed
    @ApiOperation(value = "查询所有机构")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments(DepartmentQueryVM departmentQueryVM) {
        log.debug("REST request to get a page of Departments {}", departmentQueryVM);
        List<DepartmentDTO> page = departmentSerivce.getAll(departmentQueryVM);
        //HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/departments");
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    /**
     * GET  /departments/:id : get the "id" department.
     *
     * @param id the id of the department to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the department, or with status 404 (Not Found)
     */
    @GetMapping("/departments/{id}")
    @Timed
    @ApiOperation(value = "查询指定机构")
    public ResponseEntity<Department> getDepartment(@PathVariable Long id) {
        log.debug("REST request to get Department : {}", id);
        Department department = departmentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(department));
    }

    /**
     * DELETE  /departments/:id : delete the "id" department.
     *
     * @param id the id of the department to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/departments/{id}")
    @Timed
    @ApiOperation(value = "删除指定机构")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        log.debug("REST request to delete Department : {}", id);
        departmentSerivce.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * GET 获取子部门信息
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/departments/child/1")
    @ApiOperation(value = "查询1级子机构")
    public ResponseEntity<List<Department>> get1LevelChildDepartment(@RequestParam Long departmentId) {
        List<Department> departmentList = departmentSerivce.getChildDepartmentById(departmentId, 1);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(departmentList));
    }

    /**
     * 更新机构的用户
     * @param departmentUserVM
     * @return
     */
    @PutMapping("/departments/users")
    @Timed
    @ApiOperation(value = "更新机构的用户")
    public ResponseEntity<Void> updateDepartmentUser(@RequestBody DepartmentUserVM departmentUserVM) {
        departmentSerivce.updateDepartmentUser(departmentUserVM.getDepartmentId(), departmentUserVM.getUserIds());
        return ResponseEntity.ok().build();
    }

    /**
     * 根据部门ID获取用户信息
     *
     * @param pageable
     * @param deptId
     * @return
     */
    @GetMapping("/departments/users")
    @ApiOperation("查询机构的用户")
    public ResponseEntity<List<UserDTO>> getAllUsersByDepartment(Pageable pageable, @RequestParam Long deptId) {
        final Page<UserDTO> page = departmentSerivce.findAllByDepartment(pageable, deptId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users/department");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
