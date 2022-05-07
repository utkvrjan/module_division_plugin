package io.jenkins.plugins.sample;

import hudson.Launcher;
import hudson.Extension;
import hudson.FilePath;
import hudson.util.FormValidation;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.Builder;
import hudson.tasks.BuildStepDescriptor;
import io.jenkins.plugins.sample.utils.GitUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String gitURL;
    private boolean useFrench;
    private  String classNameCheckList;
    private final String gitBranch;
    private final String projectName;


    @DataBoundConstructor
    public HelloWorldBuilder(String gitURL, String gitBranch, String projectName) {
        this.gitURL = gitURL;
        this.gitBranch = gitBranch;
        this.projectName = projectName;
    }

    public String getClassNameCheckList() {
        return classNameCheckList;
    }

    public String getGitBranch() {
        return gitBranch;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getGitURL() {
        return gitURL;
    }

    public boolean isUseFrench() {
        return useFrench;
    }

    @DataBoundSetter
    public void setUseFrench(boolean useFrench) {
        this.useFrench = useFrench;
    }

    @DataBoundSetter
    public void setClassNameCheckList(String classNameCheckList) {
        this.classNameCheckList = classNameCheckList;
    }

    @Override
    public void perform(Run<?, ?> run, FilePath workspace, Launcher launcher, TaskListener listener) throws InterruptedException, IOException {
        /** 获取当前系统时间*/
        long startTime = System.currentTimeMillis();
        setClassNameDisable(classNameCheckList);
        listener.getLogger().println("文件读取操作======接下来的模块划分操作，面向此项目展开：" + gitURL);
        if(!StringUtils.isBlank(classNameCheckList)) {
            listener.getLogger().println("文件读取操作======当前限制的类名有：" + classNameCheckList);
        }
        String res = GitUtils.cloneResposity(gitURL, gitBranch, projectName, listener);
        if(workspace.child("module_division.json").exists()) {
            workspace.child("module_division.json").delete();
        }
        workspace.createTempFile("module_division",".json");
        workspace.child("module_division.json").write(res,"UTF8");
        /** 获取当前的系统时间，与初始时间相减就是程序运行的毫秒数，除以1000就是秒数*/
        long endTime = System.currentTimeMillis();
        double usedTime = (double)(endTime-startTime)/1000;
        listener.getLogger().println("模块划分成功，并把json数据放入workspace中。总用时："+usedTime+"秒");

    }

    private void setClassNameDisable(String classNameCheckList) {
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotBlank(classNameCheckList)) {
            String[] names = classNameCheckList.split(",");
            for (String name : names) {
                set.add(name);
            }
        }
        GitUtils.setClassNameDisable(set);
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
        private String gitURL;

        public FormValidation doCheckGitURL(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            if (!value.endsWith(".git"))
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_misaddress());
            gitURL = value;
            return FormValidation.ok();
        }
        public FormValidation doCheckGitBranch(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            return FormValidation.ok();
        }
        public FormValidation doCheckProjectName(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            String str = "/"+value+".git";
            if (!gitURL.endsWith(str))
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_inconformity());
            return FormValidation.ok();
        }
        public FormValidation doCheckClassNameCheckList(@QueryParameter String classNameCheckList)
                throws IOException, ServletException {
            if (classNameCheckList.length() == 0)
                return FormValidation.warning(Messages.HelloWorldBuilder_DescriptorImpl_warnings_forget());
            if (classNameCheckList.endsWith(","))
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_mistake());
            return FormValidation.ok();
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Override
        public String getDisplayName() {
            return Messages.HelloWorldBuilder_DescriptorImpl_DisplayName();
        }

    }

}
