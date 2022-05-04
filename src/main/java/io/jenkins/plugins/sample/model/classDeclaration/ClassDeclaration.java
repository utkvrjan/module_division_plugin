package io.jenkins.plugins.sample.model.classDeclaration;

import com.alibaba.fastjson.annotation.JSONField;
import io.jenkins.plugins.sample.model.classBodyDeclaration.ClassBody;
import io.jenkins.plugins.sample.model.classBodyDeclaration.ClassBodyMember;
import lombok.Data;

@Data
//Java类定义
public class ClassDeclaration implements FileTypeDeclaration, ClassBodyMember {

    //修饰符
    @JSONField(name = "class修饰符",ordinal = 1)
    private String modifiers;

    //类名
    @JSONField(name = "类名",ordinal = 2)
    private String className;

    //类运用的泛型
    @JSONField(name = "类运用的泛型",ordinal = 3)
    private String genericType;

    //父类名称
    @JSONField(name = "父类名称",ordinal = 4)
    private String superClassName;

    //实现的接口名称
    @JSONField(name = "实现的接口",ordinal = 5)
    private String[] implInterFaces;

    //类体
    @JSONField(name = "类体",ordinal = 6)
    private ClassBody classBody;


}
