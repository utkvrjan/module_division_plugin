package io.jenkins.plugins.sample.utils;

import hudson.model.TaskListener;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.FetchResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class GitUtils {
    private static String localPath;
    private static Git git;
    private static TaskListener listener;
    public static void cloneResposity(String gitURL, String gitBranch, String projectName, TaskListener tasklistener) {
        listener = tasklistener;
        try {
            //代码指定存储目录
            localPath = projectName;
            System.out.println("============localPath==========" + localPath);
            FileRepository localRepo = new FileRepository(localPath + "/.git");
            git = new Git(localRepo);
            File localPathFile = new File(localPath);
            if (!localPathFile.exists()) {
                gitClone(gitURL, gitBranch, localPath);
            } else {
                gitPull(gitBranch);
            }
        } catch (Exception e) {
            listener.getLogger().println(e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     * 如果没有该代码目录,执行git clone
     */
    private static void gitClone(String gitURL, String gitBranch, String localPath) throws Exception {
        Git git = Git.cloneRepository().setURI(gitURL).setBranch(gitBranch).call();
        listener.getLogger().println("git拿到了");
        fetchSrc();
    }

    /**
     * 如果有代码,git pull
     */
    private static void gitPull(String branch) throws Exception {
        listener.getLogger().println("正在从"+branch+"分支中pull项目文件");
        PullResult pullResult = git.pull().setRemoteBranchName(branch).call();
        String fetchedFrom = pullResult.getFetchedFrom();
        listener.getLogger().println("获取的表单："+fetchedFrom);
        fetchSrc();

    }

    private static void fetchSrc() {
        Repository repository = git.getRepository();
        File directory =repository.getWorkTree();
        listener.getLogger().println("directory拿到了");
        File[] files = directory.listFiles();
        for(File file : files) {
            if(file.getName().equals("src")) {
                returnDir(file, 0); // 调用遍历方法
                break;
            }
        }
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
