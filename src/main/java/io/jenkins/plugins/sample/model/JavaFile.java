package io.jenkins.plugins.sample.model;

import io.jenkins.plugins.sample.model.classBodyDeclaration.ConstructorDeclaration;
import io.jenkins.plugins.sample.model.classBodyDeclaration.MethodDeclaration;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class JavaFile implements Serializable {

    private String fileName;

    private String classDeclaration;

    private List<MethodDeclaration> methodDeclarations;

    private List<ConstructorDeclaration> constructorDeclarations;

    public JavaFile(String fileName,List<MethodDeclaration> methodDeclarations) {
        this.fileName = fileName;
        this.methodDeclarations = methodDeclarations;
    }

    public JavaFile() {

    }
}
