package com.uneeddevs.finances.util;

import com.uneeddevs.finances.enums.ProfileRole;
import com.uneeddevs.finances.model.Profile;
import com.uneeddevs.finances.model.User;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.UUID;

public class UserUtil {

    private UserUtil(){}

    public static User authenticated() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch(Exception e) {
            return null;
        }
    }

    public static UUID authenticatedUUID() {
        final User user = authenticated();
        if(Objects.isNull(user))
            return null;
        return user.getId();
    }

    public static boolean hasAuthority(ProfileRole profileRole) {
        final User user = authenticated();
        if(Objects.isNull(user))
            return false;
        return user.getProfiles()
                .stream().map(Profile::getRoleName)
                .anyMatch(role -> role.replace("ROLE_", "").equalsIgnoreCase(profileRole.name()));
    }

    public static String authenticatedUsername() {
        final User user = authenticated();
        if(Objects.isNull(user))
            return Strings.EMPTY;
        return user.getEmail();
    }

}
