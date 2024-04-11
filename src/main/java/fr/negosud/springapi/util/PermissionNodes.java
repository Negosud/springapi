package fr.negosud.springapi.util;

import fr.negosud.springapi.model.entity.PermissionNode;

import java.util.ArrayList;
import java.util.List;

public final class PermissionNodes {

    /**
     * Don't look at this method : it's just a piece of french engineering right here
     * It does the job still...
     */
    public static List<PermissionNode> cleanPermissionNodeList(List<PermissionNode> permissionNodes) {
        List<PermissionNode> cleanPermissionNodeList = new ArrayList<>(permissionNodes);
        for (PermissionNode cleanedPermissionNode : permissionNodes) {
            PermissionNode checkedPermissionNode = cleanedPermissionNode;
            while (checkedPermissionNode.getParentPermissionNode() != null) {
                if (permissionNodes.contains(checkedPermissionNode.getParentPermissionNode())) {
                    cleanPermissionNodeList.remove(cleanedPermissionNode);
                    break;
                } else {
                    checkedPermissionNode = checkedPermissionNode.getParentPermissionNode();
                }
            }
        }
        return cleanPermissionNodeList;
    }

    public static List<String> asStrings(List<PermissionNode> permissionNodes) {
        List<String> strings = new ArrayList<>();
        for (PermissionNode permissionNode : permissionNodes) {
            strings.add(permissionNode.toString());
        }
        return strings;
    }

    public static List<String> getSubPermissions(PermissionNode permissionNode) {
        List<String> subPermissions = new ArrayList<>();
        subPermissions.add(permissionNode.toString());
        while (permissionNode.getParentPermissionNode() != null) {
            subPermissions.add(permissionNode.getParentPermissionNode().toString());
            permissionNode = permissionNode.getParentPermissionNode();
        }
        return subPermissions;
    }
}
