package io.jenkins.plugins.sample.utils;

import com.alibaba.fastjson.JSONObject;
import hudson.model.TaskListener;
import io.jenkins.plugins.sample.model.Folder;
import io.jenkins.plugins.sample.service.impl.FileServiceImpl;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;

public class GitUtils {



    private static FileServiceImpl fileService = new FileServiceImpl();
    private static String localPath;
    private static Git git;
    private static TaskListener listener;

    public static TaskListener getListener() {
        return listener;
    }

    public static void setListener(TaskListener listener) {
        GitUtils.listener = listener;
    }

    public static String cloneResposity(String gitURL, String gitBranch, String projectName, TaskListener tasklistener) {
        listener = tasklistener;
        try {
            //代码指定存储目录
            localPath = projectName;
            //System.out.println("============localPath==========" + localPath);
            FileRepository localRepo = new FileRepository(localPath + "/.git");
            git = new Git(localRepo);
            File localPathFile = new File(localPath);
            if (!localPathFile.exists()) {
                return gitClone(gitURL, gitBranch, localPath);
            } else {
                return gitPull(gitBranch);
            }
        } catch (Exception e) {
            listener.getLogger().println(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }



    /**
     * 如果没有该代码目录,执行git clone
     */
    private static String gitClone(String gitURL, String gitBranch, String localPath) throws Exception {
        Git git = Git.cloneRepository().setURI(gitURL).setBranch(gitBranch).call();
        listener.getLogger().println("git拿到了");
        return fetchSrc();
    }

    /**
     * 如果有代码,git pull
     */
    private static String gitPull(String branch) throws Exception {
        listener.getLogger().println("正在从"+branch+"分支中拉取项目文件");
        PullResult pullResult = git.pull().setRemoteBranchName(branch).call();
        String fetchedFrom = pullResult.getFetchedFrom();
        listener.getLogger().println("获取的表单："+fetchedFrom);
        return fetchSrc();

    }

    private static String fetchSrc() throws IOException {
        Repository repository = git.getRepository();
        File directory =repository.getWorkTree();
        listener.getLogger().println("directory拿到了");
        File[] files = directory.listFiles();
        for(File file : files) {
            if(file.getName().equals("src")) {
                Folder res = fileService.traverseFolder(file);
                String jsonString = JSONObject.toJSONString(res);
                //listener.getLogger().println("模块划分结果："+jsonString);
                //returnDir(file, 0); // 调用遍历方法
                return jsonString;
            }
        }
        return "";
    }


    private static void returnDir(File file, int i) {
        // 判断是否是文件 如果是文件 终止遍历
        if (file.isFile()) {
            //System.out.println(file.getName());
            return;
        }
        // 如果是文件夹  转化为files数组
        File[] files = file.listFiles();
        // 遍历数组
        for (File file1 : files) {
            // 让文件有层次感
            for (int i1 = 0; i1 < i; i1++) {
                listener.getLogger().print("---");
            }
            // 输出文件名称
            listener.getLogger().println(file1.getName());
            // 如果文件还是一个文件夹 进行递归
            if (file1.isDirectory()) {
                returnDir(file1, i + 1);
            }
        }
    }

}
