package com.alibaba.p3c.pmd.lang.java.rule.extend;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTFormalParameters;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTPrimaryPrefix;

import java.util.List;

/**
 * @Author : gaoyining@bobcfc.com
 * @Description :
 * @Date : Created in 11:31 2020/5/24
 * @Modify by :
 */
public class ConnectionClosedRule extends AbstractAliRule {

    private static final String PRIMARYPREFIX_XPATH = "//Name";

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        try {

            boolean openFlag = false;
            boolean closedFlage = false;
            String conParamName = null;

            // 找到所方法节点
            List<Node> nodes = node.findChildNodesWithXPath(PRIMARYPREFIX_XPATH);
            if (nodes != null && nodes.size() > 0) {
                for (Node node1 : nodes) {
                    ASTName astName = (ASTName)node1;
                    if("DriverManager.getConnection".equals(astName.getImage())){
                        openFlag = true;
                        break;
                    }else{
                        conParamName = astName.getImage();
                    }
                }

                for (Node node1 : nodes) {
                    ASTName astName = (ASTName)node1;
                    if((conParamName + ".close").equals(astName.getImage())){
                        closedFlage = true;
                        break;
                    }
                }

                boolean resultFlag = openFlag && closedFlage || !openFlag && !closedFlage ;

                if (!resultFlag) {
                    // 违反规则提示信息，第二个参数是提示信息位置，第三个参数是提示信息key，第四个参数用来替换提示信息
                    // 中的占位符，这里获取的节点image属性就是方法名称
                    addViolationWithMessage(data, null,
                            "java.extend.ConnectionClosedRule.rule.msg",
                            new Object[]{conParamName});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }
}
