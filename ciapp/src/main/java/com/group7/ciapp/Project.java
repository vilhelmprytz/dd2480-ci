package com.group7.ciapp;

import org.eclipse.jgit.api.Git;
import java.io.File;

public class Project {
    private String url;
    private String commitHash;
    private Git git;

    public Project(String url, String commitHash) {
        this.url = url;
        this.commitHash = commitHash;
    }

    // create main method that uses JGit to clone the repository
    public static void main(String url, String commitHash) {
    }

    public boolean start() {
        System.out.println("Repo URL: " + url);
        System.out.println("Commit: " + commitHash);

        String path = System.getProperty("user.home") + "/cirepo"; // clone repo inside users home directory

        System.out.println("Cloning repository to: " + path);
        try {
            git = Git.cloneRepository().setURI(url).setDirectory(new File(path)).call();
            // checkout specific commit
            // "git checkout < commit >"
            git.checkout().setName(commitHash).call();

            git.close();
        } catch (Exception e) {
            System.out.println("Error cloning repository");
            // print exception code
            System.out.println(e);
            return false;
        }

        // run tests for specified project after cloning
        // return boolean based on test results
        boolean result = runMavenTests(path);

        // TODO: delete the cloned repository, forcefully the whole directory using java

        // if tests pass, return true
        // if tests fail, return false
        return result;
    }

    /**
     * Runs Maven tests. If all tests pass, return true, else, return false.
     * 
     * @param path (String) The path to where the repo is cloned
     */
    private boolean runMavenTests(String path) {
        int exitcode = -1;
        try {
            ProcessBuilder builder = new ProcessBuilder("mvn", "package");
            builder.directory(new File(path + "/ciapp"));
            builder.redirectErrorStream(true); // merges stdout and stderr
            Process process = builder.start();

            exitcode = process.waitFor(); // wait for process to finish running tests

        } catch (Exception e) {

        }
        if (exitcode == 0) {
            // DEBUG
            System.out.println("Tests passed");
            return true;
        } else {
            System.out.println("Tests did not pass");
            return false;
        }
    }
}
