package com.leherryacademy.LeHerryAcademy.reponsitory;

import com.leherryacademy.LeHerryAcademy.model.Person;
import com.leherryacademy.LeHerryAcademy.model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Integer> {

    Roles getByRoleName(String roleName);
}
