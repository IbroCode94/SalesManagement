package org.sales.salesmanagement.enums;

public enum UserAction {
    DOCUMENT_CREATED("Document Created"),
    DOCUMENT_DELETED("Document Deleted"),
    TEMPLATE_CREATED("Template Created"),
    PIN_CREATED("Created Pin"),
    USER_DELETED("User Deleted"),
    USER_CREATED("User Created"),
    TEMPLATE_UPLOAD("User Uploaded Template"),
    SIGNATURE_UPLOAD("User Uploaded A Signature");

    private final String actionName;

    UserAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}
