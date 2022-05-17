package io.jenkins.plugins.sample.model.classDeclaration;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
//文件总体
public class CompilationUnit {

    //当前文件所在包
    @JSONField(name = "当前文件所在包",ordinal = 2)
    private String filePackage;

    //引入的包
    @JSONField(name = "当前文件引入的包",ordinal = 3)
    private List<String> importList;

    @JSONField(name = "文件类型",ordinal = 4)
    private String fileType;  // class , interface , enum

    @JSONField(name = "类定义",ordinal = 5)
    private FileTypeDeclaration fileTypeDeclaration;

    @JSONField(name = "当前文件名称",ordinal = 1)
    private String fileName;

    private transient String fileAbsolutePath;

    public CompilationUnit(String fileName) {
        this.fileName = fileName;
    }
    public CompilationUnit(String fileName,String fileAbsolutePath) {
        this.fileName = fileName;
        this.fileAbsolutePath = fileAbsolutePath;
    }
}
