package edu.upc.essi.dtim.odin.auth.user;

import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;

public enum UserVocabulary {

    HAS_USERNAME(Namespaces.USER.val()+"/hasUserName"),
    HAS_FIRSTNAME(Namespaces.USER.val()+"/hasFirstName"),
    HAS_LASTNAME(Namespaces.USER.val()+"/hasLastName"),
    HAS_PASSWORD(Namespaces.USER.val()+"/hasPassword"),
    HAS_ROLES(Namespaces.USER.val()+"/hasRoles");

    private String element;

    UserVocabulary(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }

}


