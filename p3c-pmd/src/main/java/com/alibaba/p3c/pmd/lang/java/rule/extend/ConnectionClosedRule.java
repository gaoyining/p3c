package com.alibaba.p3c.pmd.lang.java.rule.extend;

import com.alibaba.p3c.pmd.lang.java.rule.AbstractAliRule;
import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTName;

import java.util.List;

/**
 * @Author : gaoyining@bobcfc.com
 * @Description :
 * @Date : Created in 11:31 2020/5/24
 * @Modify by :
 */
public class ConnectionClosedRule extends AbstractAliRule {

    private static final String NAME_XPATH = "//Name";

    @Override
    public Object visit(ASTCompilationUnit node, Object data) {
        try {

            boolean openFlag = false;
            boolean closedFlag = false;

            // 找到所方法节点
            List<Node> nameNodes = node.findChildNodesWithXPath(NAME_XPATH);
            if (nameNodes != null && nameNodes.size() > 0) {
                for (int y =0 ; y < nameNodes.size() ; y++) {
                    ASTName astName1 = (ASTName)nameNodes.get(y);
                    if("ConnectionManager.getConnection".equals(astName1.getImage())){
//                        for (Node node2 : nodes) {
//                            ASTName astName2 = (ASTName)node2;
//                            if("ConnectionManager.releaseConnection")
//                            if((conParamName + ".close").equals(astName2.getImage())){
//                                closedFlage = true;
//                            }
//                        }

                        // 打开了数据源
                        openFlag = true;

                        for (int i = 0 ; i < nameNodes.size() ; i++){
                            ASTName astName2 = (ASTName)nameNodes.get(i);
                            if("ConnectionManager.releaseConnection".equals(astName2.getImage())){
                                // 关闭数据源
                                closedFlag = true;
                                break;
                            }
                        }

                        // 以下两种情况不报错
                        // 1、打开数据源，同时关闭数据源
                        // 2、没有打开数据源
                        boolean b = (openFlag && closedFlag) && (!closedFlag && !closedFlag);

                        if(!b){
                            addViolationWithMessage(data, nameNodes.get(y-1),
                                    "java.extend.ConnectionClosedRule.rule.msg",
                                    new Object[]{((ASTName)nameNodes.get(y-1)).getImage()});
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.visit(node, data);
    }
}
