package com.steg.smartmonitoring.user.service;

import com.steg.smartmonitoring.user.dto.CreateUserRequest;
import com.steg.smartmonitoring.user.dto.UpdateUserRequest;
import com.steg.smartmonitoring.user.dto.UserDTO;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.steg.smartmonitoring.user.entity.Role;

/**
 * Service pour gérer les utilisateurs
 */
public interface UserService {

    /**
     * Récupérer tous les utilisateurs
     * @return Liste des utilisateurs
     */
    List<UserDTO> getAllUsers();

    Page<UserDTO> getAllUsers(Pageable pageable);

    Page<UserDTO> searchUsers(String username, String email, Role role, Pageable pageable);

    /**
     * Récupérer un utilisateur par son ID
     * @param id ID de l'utilisateur
     * @return DTO de l'utilisateur
     */
    UserDTO getUserById(Long id);

    /**
     * Créer un nouvel utilisateur
     * @param request Données du nouvel utilisateur
     * @return DTO de l'utilisateur créé
     */
    UserDTO createUser(CreateUserRequest request);

    /**
     * Modifier un utilisateur
     * @param id ID de l'utilisateur
     * @param request Données à modifier
     * @return DTO de l'utilisateur modifié
     */
    UserDTO updateUser(Long id, UpdateUserRequest request);

    /**
     * Supprimer un utilisateur
     * @param id ID de l'utilisateur
     */
    void deleteUser(Long id);

}
