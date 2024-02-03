package com.timestored.swingxx;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.util.Enumeration;
import java.util.Set;

public class JTreeHelper {
    public static void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        expandAll(tree, new TreePath(root), expand);
    }

    public static void expandAll(JTree tree, TreePath parent, boolean expand) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<? extends TreeNode> e = node.children(); e.hasMoreElements(); ) {
                TreeNode n = e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public static void setFolderExpansions(JTree tree, Set<String> curExpandedFolders) {
        Preconditions.checkNotNull(tree);
        Preconditions.checkNotNull(curExpandedFolders);
        if (curExpandedFolders.size() > 0) {
            for (int row = tree.getRowCount(); row >= 0; row--) {
                TreePath path = tree.getPathForRow(row);
                if (path != null) {
                    DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Object o = (mtn != null) ? mtn.getUserObject() : null;
                    if (o != null && o instanceof IdentifiableNode) {
                        IdentifiableNode fn = (IdentifiableNode) o;
                        String id = fn.getId();
                        if (id != null && id.length() > 0 && curExpandedFolders.contains(id)) {
                            tree.expandRow(row);
                        } else {
                            tree.collapseRow(row);
                        }
                    }
                }
            }
        }
    }

    public static Set<String> getExpandedFolders(JTree tree) {
        Set<String> curExpandedFolders = Sets.newHashSet();
        if (tree != null) {
            for (int row = tree.getRowCount(); row >= 0; row--) {
                TreePath path = tree.getPathForRow(row);
                if (path != null) {
                    DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) path.getLastPathComponent();
                    Object o = (mtn != null) ? mtn.getUserObject() : null;
                    if (o != null && o instanceof IdentifiableNode && tree.isExpanded(row)) {
                        curExpandedFolders.add(((IdentifiableNode) o).getId());
                    }
                }
            }
        }
        return curExpandedFolders;
    }

    public interface IdentifiableNode {
        String getId();
    }
}


/* Location:              C:\Users\Admin\Downloads\jpad\jpad.jar!\com\timestored\swingxx\JTreeHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */