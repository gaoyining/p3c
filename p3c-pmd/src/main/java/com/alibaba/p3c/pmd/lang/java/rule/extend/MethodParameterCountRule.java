package com.alibaba.p3c.pmd.lang.java.rule.extend;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;

import java.util.List;

/**
 * @Author : gaoyining@bobcfc.com
 * @Description :
 * @Date : Created in 11:31 2020/5/23
 * @Modify by :
 */
public class MethodParameterCountRule extends AbstractAliRule {

    private static final String METHOD_XPATH = "//MethodDeclarator";

    private static final Integer PARAMETER_COUNT_LIMIT = 5;


    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        try {
            // 找到所方法节点
            List<Node> methodNodes = node.findChildNodesWithXPath(METHOD_XPATH);
            if (methodNodes != null && methodNodes.size() > 0) {
                for (Node methodNode : methodNodes) {
                    // 找到每个方法的参数列表声明
                    List<ASTFormalParameters> formalParameters = methodNode.findChildrenOfType(ASTFormalParameters.class);
                    if (formalParameters.get(0).getParameterCount() >= PARAMETER_COUNT_LIMIT) {
                        // 违反规则提示信息，第二个参数是提示信息位置，第三个参数是提示信息key，第四个参数用来替换提示信息
                        // 中的占位符，这里获取的节点image属性就是方法名称
                        addViolationWithMessage(data, methodNode,
                                "java.extend.MethodParameterCountRule.violation.msg",
                                new Object[]{methodNode.getImage()});
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }
}
