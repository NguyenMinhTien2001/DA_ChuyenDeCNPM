package com.noithat.WebNoiThat.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.noithat.WebNoiThat.dao.RoleDao;
import com.noithat.WebNoiThat.entity.Role;
import com.noithat.WebNoiThat.model.RoleDTO;
import com.noithat.WebNoiThat.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Override
	public List<RoleDTO> findAll() {
		List<Role> roles = roleDao.findAll();
		List<RoleDTO> roleDTOs = new ArrayList<RoleDTO>();
		for (Role role : roles) {
			RoleDTO roleDTO = new RoleDTO();
			roleDTO.setRoleId(role.getRoleId());
			roleDTO.setRoleName(role.getRoleName());
			roleDTOs.add(roleDTO);
		}
		return roleDTOs;
	}
	
	
	
}