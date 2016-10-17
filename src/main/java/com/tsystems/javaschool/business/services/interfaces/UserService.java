package com.tsystems.javaschool.business.services.interfaces;

import com.tsystems.javaschool.db.entities.User;

/**
 * Created by alex on 04.10.16.
 *
 * Interface for user service
 */
public interface UserService {
    /**
     * Changes password of user by old password
     * @param id id of user to be changed
     * @param oldPassword old password
     * @param newPassword new password
     * @return is password changed or not
     */
    Boolean changePassword(Integer id, String oldPassword, String newPassword);

    /**
     * Changes password of user by temporary codej
     * @param email email of user to be changed
     * @param code temporary code
     * @param newPassword new password
     * @return is password changed or not
     */
    Boolean changePasswordWithCode(String email, String code, String newPassword);

    /**
     * Generate temporary code for user
     * Password expires in 10 minutes
     * @param email email of user
     * @return is code created or not
     */
    Boolean generateTempPassword(String email);
}
