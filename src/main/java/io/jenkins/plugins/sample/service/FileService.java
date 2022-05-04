package io.jenkins.plugins.sample.service;

import io.jenkins.plugins.sample.model.Folder;
import io.jenkins.plugins.sample.model.classDeclaration.CompilationUnit;

import java.io.File;
import java.io.IOException;

public interface FileService {

    public Folder traverseFolder(File folder) throws IOException;

    public CompilationUnit filePartition(File file) throws IOException;

    public Folder processingFolder(String path);
}
