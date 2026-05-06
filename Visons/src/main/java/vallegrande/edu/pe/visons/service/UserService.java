package vallegrande.edu.pe.visons.service;

import java.util.List;
import java.util.Optional;

import vallegrande.edu.pe.visons.rest.RoleResponse;
import vallegrande.edu.pe.visons.rest.UserResponse;
import vallegrande.edu.pe.visons.rest.UserRoleRequest;
import vallegrande.edu.pe.visons.rest.UserUpsertRequest;

public interface UserService {

    List<UserResponse> findAll();

    Optional<UserResponse> findById(Integer id);

    UserResponse save(UserUpsertRequest request);

    UserResponse update(Integer id, UserUpsertRequest request);

    UserResponse toggleStatus(Integer id);

    List<UserResponse> findByRoleId(Integer roleId);

    List<RoleResponse> findRolesByUserId(Integer id);

    UserResponse assignRole(Integer id, UserRoleRequest request);
}