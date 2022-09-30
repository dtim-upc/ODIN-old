package edu.upc.essi.dtim.odin.auth.user;

import edu.upc.essi.dtim.odin.config.vocabulary.Namespaces;

public enum UserVocabulary {

    HAS_USERNAME(Namespaces.User.val()+"/hasUserName"),
    HAS_FIRSTNAME(Namespaces.User.val()+"/hasFirstName"),
    HAS_LASTNAME(Namespaces.User.val()+"/hasLastName"),
    HAS_PASSWORD(Namespaces.User.val()+"/hasPassword"),
    HAS_ROLES(Namespaces.User.val()+"/hasRoles");

    private String element;

    UserVocabulary(String element) {
        this.element = element;
    }

    public String val() {
        return element;
    }

}


