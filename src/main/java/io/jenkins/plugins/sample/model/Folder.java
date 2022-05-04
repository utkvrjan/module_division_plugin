package io.jenkins.plugins.sample.model;

import com.alibaba.fastjson.annotation.JSONField;
import io.jenkins.plugins.sample.model.classDeclaration.CompilationUnit;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Folder implements Serializable {
    @JSONField(name = "文件夹名称",ordinal = 1)
    private String folderName;

    @JSONField(name = "子文件夹列表",ordinal = 3)
    private List<Folder> childFolders;

    @JSONField(name = "文件列表",ordinal = 2)
    private List<CompilationUnit> documents;

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder() {}

}
