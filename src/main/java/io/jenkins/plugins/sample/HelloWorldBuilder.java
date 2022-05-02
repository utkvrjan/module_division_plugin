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
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.servlet.ServletException;
import java.io.IOException;
import jenkins.tasks.SimpleBuildStep;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;

public class HelloWorldBuilder extends Builder implements SimpleBuildStep {

    private final String gitURL;
    private boolean useFrench;
    private  String classNameCheckList;

    @DataBoundConstructor
    public HelloWorldBuilder(String gitURL) {
        this.gitURL = gitURL;
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

        listener.getLogger().println("接下来的模块划分操作，面向此项目展开：" + gitURL + "!");
        if(!StringUtils.isBlank(classNameCheckList)) {
            listener.getLogger().println("当前限制的类名有：" + classNameCheckList + "!");
        }
    }

    @Symbol("greet")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {

        public FormValidation doCheckGitURL(@QueryParameter String value, @QueryParameter boolean useFrench)
                throws IOException, ServletException {
            if (value.length() == 0)
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_missingName());
            if (!value.endsWith(".git"))
                return FormValidation.error(Messages.HelloWorldBuilder_DescriptorImpl_errors_misaddress());
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
