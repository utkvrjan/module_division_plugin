package io.jenkins.plugins.sample.model.classBodyDeclaration;

import com.alibaba.fastjson.annotation.JSONField;
import io.jenkins.plugins.sample.model.classDeclaration.ClassDeclaration;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ClassBody {
    //内部类定义
    @JSONField(name = "内部类定义",ordinal = 4)
    private List<ClassDeclaration> innerClassList;

    //内部接口定义
    //private List<InterfaceDeclaration> innerInterfaceList;

    //构造函数
    @JSONField(name = "类构造函数列表",ordinal = 1)
    private List<ConstructorDeclaration> constructorList;

    //静态函数
    @JSONField(name = "静态函数",ordinal = 2)
    private List<MethodDeclaration> staticMethodList;

    //非静态函数
    @JSONField(name = "非静态函数",ordinal = 3)
    private List<MethodDeclaration> nonStaticMethodList;

    public boolean addMember(ClassBodyMember member) {
        List<ConstructorDeclaration> constructorDeclarationList = this.getConstructorList();
        if(constructorDeclarationList == null) this.setConstructorList(new ArrayList<>());
        if(member instanceof MethodDeclaration) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) member;
            List<MethodDeclaration> staticMethodList = this.getStaticMethodList();
            List<MethodDeclaration> nonStaticMethodList = this.getNonStaticMethodList();
            if(staticMethodList == null) this.setStaticMethodList(new ArrayList());
            if(nonStaticMethodList == null) this.setNonStaticMethodList(new ArrayList());
            if(methodDeclaration.getModifiers().contains("static")){
                this.getStaticMethodList().add(methodDeclaration);
            } else {
                this.getNonStaticMethodList().add(methodDeclaration);
            }
            return true;
        }
        if(member instanceof ConstructorDeclaration) {
            ConstructorDeclaration constructorDeclaration = (ConstructorDeclaration) member;
            if(this.getConstructorList() == null) this.setConstructorList(new ArrayList<>());
            if(constructorDeclaration != null) {
                this.getConstructorList().add(constructorDeclaration);
            }
            return true;
        }
        if(member instanceof ClassDeclaration) {
            ClassDeclaration classDeclaration = (ClassDeclaration) member;
            if(this.getInnerClassList() == null) this.setInnerClassList(new ArrayList<>());
            if(classDeclaration != null) {
                this.getInnerClassList().add(classDeclaration);
            }
            return true;
        }
        return false;
    }
}
