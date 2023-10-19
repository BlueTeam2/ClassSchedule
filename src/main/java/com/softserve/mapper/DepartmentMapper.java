package com.softserve.mapper;

import com.softserve.dto.DepartmentDTO;
import com.softserve.entity.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    DepartmentDTO departmentToDepartmentDTO(Department department);

    Department departmentDTOToDepartment(DepartmentDTO departmentDTO);

    List<DepartmentDTO> departmentsToDepartmentDTOs(List<Department> departments);
}
